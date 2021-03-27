package jumperutils.cannontracer.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import jumperutils.cannontracer.CannonTracer;
import jumperutils.cannontracer.utils.UserSettings;

public class TracerCMD implements CommandExecutor, TabCompleter {
	public final CannonTracer cannonTracer;

	public TracerCMD(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;

		PluginCommand tracerCommand = cannonTracer.main.getCommand("tracer");
		if (tracerCommand == null) {
			System.err.println("JumperUtils TracerCommand was unable to find command 'tracer'!");
		} else {
			tracerCommand.setExecutor(this);
		}
	}

	static final List<String> subCommands = Arrays.asList("register", "unregister");

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
			return false;
		}
		Player p = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("register")) {
				if (cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
					p.sendMessage("§cYou are already registered");
				} else {
					cannonTracer.tntSpawnListener.getPlayerSettings().put(p, new UserSettings());
					cannonTracer.tntSpawnListener.notifySettingsChanged();
					p.sendMessage("§aYou are now registered");
					p.sendMessage("[JumperCannonTracer][SettingsRequest]");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("unregister")) {
				if (cannonTracer.tntSpawnListener.getPlayerSettings().containsKey(p)) {
					cannonTracer.tntSpawnListener.getPlayerSettings().remove(p);
					cannonTracer.tntSpawnListener.notifySettingsChanged();
					p.sendMessage("§aYou are no longer registered");
				} else {
					p.sendMessage("§cYou are not registered");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("players")) {
				if (cannonTracer.tntSpawnListener.getPlayerSettings().keySet().isEmpty()) {
					p.sendMessage("§cNo Players are registered yet.");
				} else {
					p.sendMessage(cannonTracer.tntSpawnListener.getPlayerSettings().keySet().toString());
				}
				return true;
			}
		}
		return false;
	}
}
