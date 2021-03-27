package jumperutils.cannonactivator.listeners;

import java.util.HashMap;

import net.minecraft.server.v1_15_R1.BlockPosition;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Observer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import jumperutils.cannonactivator.CannonActivator;
import jumperutils.cannonactivator.utils.ButtonData;
import jumperutils.cannonactivator.utils.ButtonTypes;
import jumperutils.cannonactivator.utils.ButtonTypes.ButtonType;

public class PlayerInteractListener implements Listener {
	public static final HashMap<Material, ButtonType> typeData = ButtonTypes.getTypeData();
	public final CannonActivator cannonActivator;

	public PlayerInteractListener(CannonActivator cannonActivator) {
		this.cannonActivator = cannonActivator;
		cannonActivator.main.getServer().getPluginManager().registerEvents(this, cannonActivator.main);
	}

	@EventHandler
	public void onHoeInteract(PlayerInteractEvent e) {
		if (e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			return;
		}
		Player p = e.getPlayer();
		Material item = p.getInventory().getItemInMainHand().getType();
		if (!item.equals(Material.DIAMOND_HOE)) {
			return;
		}
		e.setCancelled(true);
		handleActivatorTrigger(p);
	}

	public void handleActivatorTrigger(Player p) {
		ButtonData currentBlock = cannonActivator.blockBreakListener.blockData.get(p);
		if (currentBlock == null) {
			p.sendMessage("§cNo Block Selected!");
			return;
		}

		World currentWorld = currentBlock.world;
		if (currentWorld != p.getWorld()) {
			p.sendMessage("§cCan't (or rather: won't) activate a block in a different world!");
			return;
		}

		Block block = currentWorld.getBlockAt(currentBlock.x, currentBlock.y, currentBlock.z);
		Material blockMaterial = block.getType();
		if (!typeData.containsKey(blockMaterial)) {
			p.sendMessage("§cThe Material §b" + blockMaterial.toString() + " §cis not recognized as an Activator! §b" + currentBlock.locationToString() + " §ahas been block-updated.");
			triggerPhysicsUpdate(block);
			return;
		}

		ButtonType selectedType = typeData.get(blockMaterial);
		switch (selectedType) {
			case LEVER:
				doLeverActivation(currentBlock, block, p);
				break;
			case WOOD_BUTTON:
				doButtonActivation(currentBlock, block, p, 31);
				break;
			case STONE_BUTTON:
				doButtonActivation(currentBlock, block, p, 21);
				break;
			case OBSERVER:
				doObserverActivation(currentBlock, block, p);
				break;
			default:
				p.sendMessage("§cCould not determine Activator-Type, please use a different Activator-Block");
				System.out.println("My lazy ass forgot to add: " + selectedType.toString() + " to the switch in PlayerInteractListener");
		}
	}

	public void doLeverActivation(ButtonData buttonData, Block targetBlock, Player p) {
		Powerable targetData = (Powerable) targetBlock.getBlockData();
		doPowering(targetBlock, targetData);
		p.sendMessage("§aLever at location §b" + buttonData.locationToString() + " §ahas been " + (targetData.isPowered() ? "activated" : "deactivated"));
	}

	public void doButtonActivation(ButtonData buttonData, Block targetBlock, Player p, int ticks) {
		Powerable targetData = (Powerable) targetBlock.getBlockData();
		doPowering(targetBlock, targetData, true);
		cannonActivator.main.getServer().getScheduler().runTaskLater(cannonActivator.main, () -> doPowering(targetBlock, targetData, false), ticks);
		p.sendMessage("§aButton at location §b" + buttonData.locationToString() + "§a will be activated for the next §b" + ticks + "§a ticks!");
	}

	public void doObserverActivation(ButtonData buttonData, Block targetBlock, Player p) {
		Observer targetData = (Observer) targetBlock.getBlockData();
		doPowering(targetBlock, targetData, true);
		cannonActivator.main.getServer().getScheduler().runTaskLater(cannonActivator.main, () -> doPowering(targetBlock, targetData, false), 2);
		p.sendMessage("§aObserver at location §b" + buttonData.locationToString() + "§a triggered.");
	}

	public void doPowering(Block targetBlock, Powerable targetData) {
		doPowering(targetBlock, targetData, !targetData.isPowered());
	}

	public void doPowering(Block targetBlock, Powerable targetData, boolean power) {
		targetData.setPowered(power);
		BlockState state = targetBlock.getState();

		state.update(true, true);
		targetBlock.setBlockData(targetData, true);

		forceBlockUpdate(targetBlock.getRelative(0, 1, 0));
		forceBlockUpdate(targetBlock.getRelative(0, -1, 0));
		forceBlockUpdate(targetBlock.getRelative(1, 0, 0));
		forceBlockUpdate(targetBlock.getRelative(-1, 0, 0));
		forceBlockUpdate(targetBlock.getRelative(0, 0, 1));
		forceBlockUpdate(targetBlock.getRelative(0, 0, -1));
	}

	public void forceBlockUpdate(Block block) {
		((CraftWorld) block.getWorld()).getHandle().applyPhysics(
				new BlockPosition(block.getX(), block.getY(), block.getZ()),
				CraftMagicNumbers.getBlock(block.getType()));
	}

	private void triggerPhysicsUpdate(Block block) {
		BlockState state = block.getState();
		BlockData blockData = block.getBlockData();
		Material prevType = state.getType();

		if (state instanceof Container) {
			if (prevType == Material.BARREL) {
				state.setType(Material.CHEST);
			} else {
				state.setType(Material.BARREL);
			}
		} else {
			if (prevType == Material.STONE) {
				state.setType(Material.DIRT);
			} else {
				state.setType(Material.STONE);
			}
		}
		state.update(true, false);
		state.setType(prevType);
		state.update(true, true);

		block.setBlockData(blockData, true);
	}
}
