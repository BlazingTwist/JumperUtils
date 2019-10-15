package jumperUtils;

import org.bukkit.plugin.java.JavaPlugin;

import jumperUtils.cannonActivator.CannonActivator;
import jumperUtils.cannonTracer.CannonTracer;

public class Main extends JavaPlugin{
	private CannonActivator cannonActivator;
	private CannonTracer cannonTracer;
	
	public CannonActivator getCannonActivator() {
		return cannonActivator;
	}
	
	public CannonTracer getCannonTracer() {
		return cannonTracer;
	}
	
	public void onEnable() {
		System.out.println("JumperUtils just started running");
		cannonActivator = new CannonActivator(this);
		cannonTracer = new CannonTracer(this);
		
	}
}
