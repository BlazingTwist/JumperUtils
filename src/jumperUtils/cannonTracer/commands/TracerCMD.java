package jumperutils.cannontracer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jumperutils.cannontracer.CannonTracer;
import jumperutils.cannontracer.utils.UserSettings;
import jumperutils.global.utils.Update;

public class TracerCMD implements CommandExecutor{
	public final CannonTracer cannonTracer;
	
	public TracerCMD(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getCommand("tracer").setExecutor(this);
	}	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return handleConsoleCommands(args);
		}
		Player p = (Player) sender;
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("update")) {
				Update.updatePlugin(p);
				return true;
			}else if(args[0].equalsIgnoreCase("register")) {
				if(cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
					p.sendMessage("§cYou are already registered");
				}else {
					cannonTracer.tntSpawnListener.getPlayerSettings(true).put(p, new UserSettings());
					p.sendMessage("§aYou are now registered");
					p.sendMessage("[JumperCannonTracer][SettingsRequest]");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("unregister")) {
				if(cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
					cannonTracer.tntSpawnListener.getPlayerSettings(true).remove(p);
					p.sendMessage("§aYou are no longer registered");
				}else {
					p.sendMessage("§cYou are not registered");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("list")) {
				p.sendMessage(cannonTracer.tntSpawnListener.getPlayerSettings().keySet().toString());
				return true;
			}
		}
		return false;
	}
	
	public boolean handleConsoleCommands(String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("update")) {
				Update.updatePlugin(null);
				return true;
			}
		}
		return false;
	}
}
