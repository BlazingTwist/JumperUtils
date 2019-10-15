package jumperutils;

import org.bukkit.plugin.java.JavaPlugin;

import jumperutils.cannonactivator.CannonActivator;
import jumperutils.cannontracer.CannonTracer;

public class JumperUtils extends JavaPlugin{
	private CannonActivator cannonActivator;
	private CannonTracer cannonTracer;
	
	public CannonActivator getCannonActivator() {
		return cannonActivator;
	}
	
	public CannonTracer getCannonTracer() {
		return cannonTracer;
	}
	
	public void onEnable() {
		cannonActivator = new CannonActivator(this);
		cannonTracer = new CannonTracer(this);
		
	}
}
