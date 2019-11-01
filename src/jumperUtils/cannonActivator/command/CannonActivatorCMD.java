package jumperutils.cannonactivator.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import jumperutils.cannonactivator.CannonActivator;

public class CannonActivatorCMD implements CommandExecutor, TabCompleter{
	public final CannonActivator cannonActivator;
	
	public CannonActivatorCMD(CannonActivator cannonActivator) {
		this.cannonActivator = cannonActivator;
		cannonActivator.main.getCommand("cannonActivator").setExecutor(this);
	}
	
	static final List<String> subCommands = Arrays.asList("trigger", "select");
	
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
			return false;
		}
		Player p = (Player) sender;
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("trigger")) {
				cannonActivator.playerInteractListener.handleActivatorTrigger(p);
				return true;
			}else if(args[0].equalsIgnoreCase("select")) {
				cannonActivator.blockBreakListener.playerSelectsBlock(p, p.getTargetBlock(10));
				return true;
			}
		}
		return false;
	}
}
