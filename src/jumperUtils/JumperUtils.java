package jumperutils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import jumperutils.cannonactivator.CannonActivator;
import jumperutils.cannontracer.CannonTracer;

public class JumperUtils extends JavaPlugin{
	private CannonActivator cannonActivator;
	private CannonTracer cannonTracer;
	private static JumperUtils instance;
	private static PluginManager pluginManager;
	
	{
		instance = this;
		pluginManager = Bukkit.getPluginManager();
	}
	
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
	
	public static PluginManager getPluginManager(){
		return pluginManager;
	}
	
	public static JumperUtils getInstance() {
		return instance;
	}
}
