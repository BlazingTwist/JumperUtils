package jumperutils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import jumperutils.global.utils.Update;

public class JumperUtilsCMD implements CommandExecutor, TabCompleter{
	public final JumperUtils jumperUtils;
	
	public JumperUtilsCMD(JumperUtils jumperUtils) {
		this.jumperUtils = jumperUtils;
		jumperUtils.getCommand("jumperUtils").setExecutor(this);
	}
	
	static final List<String> subCommands = Arrays.asList("update");
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		if(args.length <= 1) {
			return subCommands;
		}
		return null;
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
