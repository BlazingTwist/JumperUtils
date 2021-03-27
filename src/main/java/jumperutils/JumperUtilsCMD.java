package jumperutils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import jumperutils.global.utils.Update;

public class JumperUtilsCMD implements CommandExecutor, TabCompleter {
	public final JumperUtils jumperUtils;

	public JumperUtilsCMD(JumperUtils jumperUtils) {
		this.jumperUtils = jumperUtils;

		PluginCommand jumperUtilsCommand = jumperUtils.getCommand("jumperUtils");
		if (jumperUtilsCommand == null) {
			System.err.println("JumperUtils JumperUtilsCommand was unable to find command 'jumperUtils'!");
		} else {
			jumperUtilsCommand.setExecutor(this);
		}
	}

	static final List<String> subCommands = Arrays.asList("update", "info");

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length <= 1) {
			return subCommands;
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.err.println("got command from non-player!");
			return handleConsoleCommands(args);
		}
		Player p = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("update")) {
				Update.updatePlugin(p);
				return true;
			} else if (args[0].equalsIgnoreCase("info")) {
				Update.messageRecipient(p, "JumperUtils version is: ", Update.version, true);
				return true;
			}
		}
		return false;
	}

	public boolean handleConsoleCommands(String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("update")) {
				Update.updatePlugin(null);
				return true;
			}
		}
		return false;
	}
}
