package jumperutils.cannontracer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import jumperutils.cannontracer.CannonTracer;

public class ServerChatListener implements Listener {
	public final CannonTracer cannonTracer;

	public ServerChatListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}

	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String text = e.getMessage();
		if (text.length() < 21) {
			return;
		}
		if (text.startsWith("[JumperCannonTracer]")) {
			String message = text.substring(20);
			if (message.length() > 8 && message.startsWith("[Config]")) {
				cannonTracer.tntSpawnListener.getPlayerSettings().get(p).interpretConfigMessage(message.substring(8));
				cannonTracer.tntSpawnListener.notifySettingsChanged();
				e.setCancelled(true);
				return;
			}
			if (message.equals("[PullDataRequest]")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						cannonTracer.tntSpawnListener.handlePullRequest(p);
					}
				}.runTaskAsynchronously(cannonTracer.main);
				e.setCancelled(true);
			}
		}
	}
}
