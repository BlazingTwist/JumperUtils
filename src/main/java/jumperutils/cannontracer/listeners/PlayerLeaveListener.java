package jumperutils.cannontracer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import jumperutils.cannontracer.CannonTracer;

public class PlayerLeaveListener implements Listener {
	public final CannonTracer cannonTracer;
	
	public PlayerLeaveListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}
	
	@EventHandler
	public void onPlayerLeft(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
			cannonTracer.tntSpawnListener.getPlayerSettings().remove(p);
			cannonTracer.tntSpawnListener.notifySettingsChanged();
		}
	}
}
