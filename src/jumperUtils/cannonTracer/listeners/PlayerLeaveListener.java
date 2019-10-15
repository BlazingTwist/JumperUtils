package jumperUtils.cannonTracer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import jumperUtils.cannonTracer.CannonTracer;

public class PlayerLeaveListener implements Listener {
	public final CannonTracer cannonTracer;
	
	public PlayerLeaveListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}
	
	@EventHandler
	public void onPlayerLeft(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(p == null) {
			System.out.println("player in PlayerLeaveListener was null (wtf how?)");
		}
		if(cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
			cannonTracer.tntSpawnListener.getPlayerSettings(true).remove(p);
		}
	}
}
