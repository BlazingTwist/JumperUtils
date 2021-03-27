package jumperutils.cannontester.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.charset.StandardCharsets;
import jumpercommons.TestCannonMessageObject;
import jumperutils.cannontester.CannonTester;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
	public final CannonTester cannonTester;
	public static final String testCannonChannel = "jumperutils:testcannondata";

	public PluginMessageListener(CannonTester cannonTester) {
		this.cannonTester = cannonTester;
		Messenger messenger = cannonTester.main.getServer().getMessenger();
		messenger.registerIncomingPluginChannel(cannonTester.main, testCannonChannel, this);
		messenger.registerOutgoingPluginChannel(cannonTester.main, testCannonChannel);
	}

	@Override
	public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
		if (s.equals(testCannonChannel)) {
			String testCannonJson = new String(bytes, 1, bytes.length - 1, StandardCharsets.UTF_8);
			TestCannonMessageObject messageObject;
			try {
				messageObject = cannonTester.objectMapper.readValue(testCannonJson, TestCannonMessageObject.class);
			} catch (JsonProcessingException e) {
				player.sendMessage("Unable to parse testCannonData, check server logs.");
				System.err.println("unable to parse TestCannonJson: " + testCannonJson);
				e.printStackTrace();
				return;
			}

			Location cmdLocation = new Location(player.getWorld(), messageObject.getCmdLocation().x, messageObject.getCmdLocation().y, messageObject.getCmdLocation().z);
			Block targetCmdBlock = cmdLocation.getBlock();
			if (targetCmdBlock.getType() != Material.COMMAND_BLOCK) {
				player.sendMessage("Your target-block is no longer a commandBlock, setting command failed.");
				return;
			}

			cannonTester.main.getServer().getScheduler().runTask(cannonTester.main, () -> {
				CommandBlock block = (CommandBlock) targetCmdBlock.getState();
				block.setCommand("[TestCannonData]" + messageObject.getTestCannonString());
				block.update(true, true);
			});
		}
	}
}
