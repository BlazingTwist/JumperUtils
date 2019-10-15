package jumperUtils.cannonActivator;

import jumperUtils.Main;
import jumperUtils.cannonActivator.listeners.BlockBreakListener;
import jumperUtils.cannonActivator.listeners.PlayerInteractListener;

public class CannonActivator {
	public final Main main;
	public final BlockBreakListener blockBreakListener;
	public final PlayerInteractListener playerInteractListener;
	
	public CannonActivator(Main main) {
		this.main = main;
		blockBreakListener = new BlockBreakListener(this);
		playerInteractListener = new PlayerInteractListener(this);
	}
}
