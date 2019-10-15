package jumperutils.cannonactivator;

import jumperutils.JumperUtils;
import jumperutils.cannonactivator.listeners.BlockBreakListener;
import jumperutils.cannonactivator.listeners.PlayerInteractListener;

public class CannonActivator {
	public final JumperUtils main;
	public final BlockBreakListener blockBreakListener;
	public final PlayerInteractListener playerInteractListener;
	
	public CannonActivator(JumperUtils main) {
		this.main = main;
		blockBreakListener = new BlockBreakListener(this);
		playerInteractListener = new PlayerInteractListener(this);
	}
}
