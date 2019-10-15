package jumperUtils.cannonTracer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import jumperUtils.cannonTracer.CannonTracer;

public class ServerChatListener implements Listener{
	public final CannonTracer cannonTracer;
	
	public ServerChatListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(p == null) {
			return;
		}
		String text = e.getMessage();
		if(text == null) {
			return;
		}
		if(text.length() < 21) {
			return;
		}
		if(text.substring(0, 20).equals("[JumperCannonTracer]")) {
			String message = text.substring(20);
			if(message.length() > 8 && message.substring(0, 8).equals("[Config]")){
				cannonTracer.tntSpawnListener.getPlayerSettings(true).get(p).interpretConfigMessage(message.substring(8));
				e.setCancelled(true);
				return;
			}
			if(message.equals("[PullDataRequest]")) {
				cannonTracer.tntSpawnListener.handlePullRequest(p);
				e.setCancelled(true);
				return;
			}
		}
	}
}
