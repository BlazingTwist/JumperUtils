package jumperutils;

import jumperutils.cannontester.CannonTester;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import jumperutils.cannonactivator.CannonActivator;
import jumperutils.cannontracer.CannonTracer;

public class JumperUtils extends JavaPlugin{
	private CannonActivator cannonActivator;
	private CannonTracer cannonTracer;
	private CannonTester cannonTester;
	private JumperUtilsCMD jumperUtilsCMD;
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

	public CannonTester getCannonTester() {
		return cannonTester;
	}

	public JumperUtilsCMD getJumperUtilsCMD() {
		return jumperUtilsCMD;
	}
	
	public void onEnable() {
		cannonActivator = new CannonActivator(this);
		cannonTracer = new CannonTracer(this);
		cannonTester = new CannonTester(this);
		jumperUtilsCMD = new JumperUtilsCMD(this);
	}
	
	public static PluginManager getPluginManager(){
		return pluginManager;
	}
	
	public static JumperUtils getInstance() {
		return instance;
	}
}
