package jumperutils.cannonactivator.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Switch;
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

public class PlayerInteractListener implements Listener{
	public static final HashMap<Material, ButtonType> typeData = ButtonTypes.getTypeData();
	public final CannonActivator cannonActivator;
	
	public PlayerInteractListener(CannonActivator cannonActivator) {
		this.cannonActivator = cannonActivator;
		cannonActivator.main.getServer().getPluginManager().registerEvents(this, cannonActivator.main);
	}
	
	@EventHandler
	public void onHoeInteract(PlayerInteractEvent e) {
		if(!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			return;
		}
		Player p = e.getPlayer();
		if(p == null) {
			System.out.println("player in PlayerInteractListener was null (what the heck?!)");
			return;
		}
		Material item = p.getInventory().getItemInMainHand().getType();
		if(item == null) {
			System.out.println("players item in PlayerInteractListener was null (what the heck?!)");
			return;
		}
		if(!item.equals(Material.DIAMOND_HOE)) {
			return;
		}
		e.setCancelled(true);
		handleActivatorTrigger(p);
	}
	
	public void handleActivatorTrigger(Player p) {
		ButtonData currentBlock = cannonActivator.blockBreakListener.blockdata.get(p);
		if(currentBlock == null) {
			p.sendMessage("§cNo Block Selected!");
			return;
		}
		
		World currentWorld = currentBlock.world;
		if(currentWorld != p.getWorld()) {
			p.sendMessage("§cCan't (or rather: won't) activate a block in a different world!");
			return;
		}
		
		Block block = currentWorld.getBlockAt(currentBlock.x, currentBlock.y, currentBlock.z);
		Material blockmaterial = block.getType();
		if(!typeData.containsKey(blockmaterial)) {
			p.sendMessage("§cThe Material §b" + blockmaterial.toString() + " §cis not recognized as an Activator! §b" + currentBlock.locationToString() + " §ahas been blockupdated.");
			forceBlockUpdate(block);
			return;
		}
		
		ButtonType selectedType = typeData.get(blockmaterial);
		switch(selectedType) {
		case LEVER:
			doLeverActivation(currentBlock, block, p);
			break;
		case WOOD_BUTTON:
			doButtonActivation(currentBlock, block, p, 30);
			break;
		case STONE_BUTTON:			
			doButtonActivation(currentBlock, block, p, 20);
			break;
		default:
			p.sendMessage("§cCould not determine Activator-Type, please use a different Activator-Block");
			System.out.println("My lazy ass forgot to add: " + selectedType.toString() + " to the switch in PlayerInteractListener");
			return;
		}
	}
	
	public void doLeverActivation(ButtonData buttonData, Block targetBlock, Player p) {
		Switch targetData = (Switch)targetBlock.getBlockData();
		doPowering(targetBlock, targetData, p);
		p.sendMessage("§aLever at location §b" + buttonData.locationToString() + " §ahas been " + (targetData.isPowered() ? "activated" : "deactivated"));
	}
	
	public void doButtonActivation(ButtonData buttonData, Block targetBlock, Player p, int ticks) {
		Switch targetData = (Switch)targetBlock.getBlockData();
		doPowering(targetBlock, targetData, true, p);
		cannonActivator.main.getServer().getScheduler().runTaskLater(cannonActivator.main, new Runnable() {
			public void run() {
				doPowering(targetBlock, targetData, false, p);
			}
		}, ticks);
		p.sendMessage("§aButton at location §b" + buttonData.locationToString() + " §awill be activated for the next §b" + ticks + " §aticks!");
	}
	
	public void doPowering(Block targetBlock, Switch targetData, Player p) {
		doPowering(targetBlock, targetData, !targetData.isPowered(), p);
	}
	
	public void doPowering(Block targetBlock, Switch targetData, boolean power, Player p) {
		targetData.setPowered(power);
		targetBlock.setBlockData(targetData, true);
		Block attachedBlock;
		switch(targetData.getFace()) {
		case CEILING:
			attachedBlock = targetBlock.getRelative(0, 1, 0);
			break;
		case FLOOR:
			attachedBlock = targetBlock.getRelative(0, -1, 0);
			break;
		case WALL:
			attachedBlock = targetBlock.getRelative(targetData.getFacing().getOppositeFace());
			break;
		default:
			p.sendMessage("§cCould not determine Face-Value §b" + targetData.getFace().toString() + " of Activator-Block, attached Blocks will §4not §cbe updated!");
			return;
		}
		forceBlockUpdate(attachedBlock);
	}
	
	public void forceBlockUpdate(Block block) {
		BlockState state = block.getState();
		BlockData prevData = block.getBlockData();
		Material prevType = state.getType();
		//change state and change back to clear the blockData
		state.setType(Material.CHEST); //using chest to store items of potential containerblocks
		state.update(true, false);
		state.setType(prevType);
		//update without physics as we haven't reinstated the blockData yet
		state.update(true, true);
		//actually do physics update
		block.setBlockData(prevData, true);
	}
}
