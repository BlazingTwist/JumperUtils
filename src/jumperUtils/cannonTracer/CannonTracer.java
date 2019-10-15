package jumperutils.cannontracer;

import jumperutils.JumperUtils;
import jumperutils.cannontracer.commands.TracerCMD;
import jumperutils.cannontracer.events.TickManager;
import jumperutils.cannontracer.listeners.PlayerLeaveListener;
import jumperutils.cannontracer.listeners.ServerChatListener;
import jumperutils.cannontracer.listeners.TntSpawnListener;

public class CannonTracer {
	public final JumperUtils main;
	public final TntSpawnListener tntSpawnListener;
	public final PlayerLeaveListener playerLeaveListener;
	public final ServerChatListener serverChatListener;
	public final TickManager tickEvent;
	public TracerCMD tracerCMD;
	
	public CannonTracer(JumperUtils main){
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
