package jumperutils.cannonactivator;

import jumperutils.JumperUtils;
import jumperutils.cannonactivator.command.CannonActivatorCMD;
import jumperutils.cannonactivator.listeners.BlockBreakListener;
import jumperutils.cannonactivator.listeners.PlayerInteractListener;

public class CannonActivator {
	public final JumperUtils main;
	public final BlockBreakListener blockBreakListener;
	public final PlayerInteractListener playerInteractListener;
	public final CannonActivatorCMD cannonActivatorCMD;
	
	public CannonActivator(JumperUtils main) {
		this.main = main;
		blockBreakListener = new BlockBreakListener(this);
		playerInteractListener = new PlayerInteractListener(this);
		cannonActivatorCMD = new CannonActivatorCMD(this);
	}
}
