package jumperutils.cannontester.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import jumpercommons.SimpleLocation;
import jumpercommons.TestCannonMessageObject;
import jumperutils.cannontester.CannonTester;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CannonTesterCMD implements CommandExecutor, TabExecutor {
	public final CannonTester cannonTester;

	public CannonTesterCMD(CannonTester cannonTester) {
		this.cannonTester = cannonTester;

		PluginCommand cannonTesterCommand = cannonTester.main.getCommand("cannontester");
		if (cannonTesterCommand == null) {
			System.err.println("JumperUtils CannonTesterCommand was unable to find command 'cannontester'!");
		} else {
			cannonTesterCommand.setExecutor(this);
		}
	}

	static final List<String> subCommands = Collections.singletonList("edit");

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length <= 1) {
			return subCommands;
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("edit")) {
				Block targetBlock = p.getTargetBlockExact(10);
				if (targetBlock == null || targetBlock.getType() != Material.COMMAND_BLOCK) {
					p.sendMessage("You have to look at a command-block to edit a testCannon.");
					return true;
				}

				CommandBlock block = (CommandBlock) targetBlock.getState();
				String blockCommand = block.getCommand();
				if (!blockCommand.isEmpty()) {
					if (!blockCommand.startsWith("[TestCannonData]")) {
						p.sendMessage("This command-block is not a CannonTester-Block, use a different one or clear its content.");
						return true;
					}
					blockCommand = blockCommand.substring("[TestCannonData]".length());
				}

				byte[] indexBytes = new byte[]{Integer.valueOf(0).byteValue()};
				Location blockLocation = block.getLocation();
				TestCannonMessageObject messageObject = new TestCannonMessageObject(new SimpleLocation(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()), blockCommand);
				String messageString;
				try {
					messageString = cannonTester.objectMapper.writeValueAsString(messageObject);
				} catch (JsonProcessingException e) {
					p.sendMessage("Failed building testCannonData, check server log.");
					e.printStackTrace();
					return true;
				}

				byte[] messageBytes = ArrayUtils.addAll(indexBytes, messageString.getBytes(StandardCharsets.UTF_8));
				p.sendPluginMessage(cannonTester.main, "jumperutils:testcannondata", messageBytes);
				return true;
			}
		}

		return false;
	}
}
