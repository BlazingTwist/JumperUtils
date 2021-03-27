package jumperutils.cannontester.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import jumpercommons.GNSVector;
import jumpercommons.TestCannon;
import jumpercommons.TestCannonCharge;
import jumperutils.cannontester.CannonTester;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.util.Vector;

public class ServerCommandListener implements Listener {
	public final CannonTester cannonTester;

	public ServerCommandListener(CannonTester cannonTester) {
		this.cannonTester = cannonTester;
		cannonTester.main.getServer().getPluginManager().registerEvents(this, cannonTester.main);
	}

	@EventHandler
	public void onServerCommandEvent(ServerCommandEvent e) {
		CommandSender sender = e.getSender();
		if (!(sender instanceof BlockCommandSender)) {
			return;
		}
		if (!e.getCommand().startsWith("[TestCannonData]")) {
			return;
		}
		String data = e.getCommand().substring("[TestCannonData]".length());
		Location blockLocation = ((BlockCommandSender) sender).getBlock().getLocation();
		try {
			TestCannon testCannon = cannonTester.objectMapper.readValue(data, TestCannon.class);
			GNSVector velocity = testCannon.getVelocity();
			Vector velocityVector = new Vector(velocity.x.get(), velocity.y.get(), velocity.z.get());

			World world = blockLocation.getWorld();
			Location spawnLocation = new Location(world, blockLocation.getX() + 0.5, blockLocation.getY(), blockLocation.getZ() + 0.5);

			GNSVector blockOffset = testCannon.getBlockOffset();
			spawnLocation.add(blockOffset.x.get(), blockOffset.y.get(), blockOffset.z.get());

			GNSVector pixelOffset = testCannon.getPixelOffset();
			spawnLocation.add(pixelOffset.x.get() / 16d, pixelOffset.y.get() / 16d, pixelOffset.z.get() / 16d);

			for (TestCannonCharge charge : testCannon.getCharges()) {
				if (!charge.getEnabled()) {
					continue;
				}
				if (charge.getDelay() > 0) {
					cannonTester.main.getServer().getScheduler().runTaskLater(
							cannonTester.main,
							() -> spawnTNT(world, spawnLocation, velocityVector, charge.getAmount()),
							charge.getDelay()
					);
				} else {
					spawnTNT(world, spawnLocation, velocityVector, charge.getAmount());
				}
			}

		} catch (JsonProcessingException jsonProcessingException) {
			cannonTester.main.getServer().broadcastMessage("TestCannon-Block at location " + blockLocation + " FAILED! (invalid content, check logs)");
			jsonProcessingException.printStackTrace();
		}
	}

	private void spawnTNT(World world, Location location, Vector velocity, int amount) {
		for (int i = 0; i < amount; i++) {
			TNTPrimed tnt = world.spawn(location, TNTPrimed.class);
			tnt.setFuseTicks(80);
			tnt.setVelocity(velocity);
		}
	}
}
