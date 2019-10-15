package jumperUtils.cannonTracer;

import jumperUtils.Main;
import jumperUtils.cannonTracer.commands.TracerCMD;
import jumperUtils.cannonTracer.events.TickManager;
import jumperUtils.cannonTracer.listeners.PlayerLeaveListener;
import jumperUtils.cannonTracer.listeners.ServerChatListener;
import jumperUtils.cannonTracer.listeners.TntSpawnListener;

public class CannonTracer {
	public final Main main;
	public final TntSpawnListener tntSpawnListener;
	public final PlayerLeaveListener playerLeaveListener;
	public final ServerChatListener serverChatListener;
	public final TickManager tickEvent;
	public TracerCMD tracerCMD;
	
	public CannonTracer(Main main){
		this.main = main;
		tntSpawnListener = new TntSpawnListener(this);
		playerLeaveListener = new PlayerLeaveListener(this);
		serverChatListener = new ServerChatListener(this);
		tickEvent = new TickManager(this);
		registerCommands();
	}
	
	public void registerCommands() {
		tracerCMD = new TracerCMD(this);
	}
}
