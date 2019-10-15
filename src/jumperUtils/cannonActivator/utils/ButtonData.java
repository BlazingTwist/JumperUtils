package jumperutils.cannonactivator.utils;

import org.bukkit.World;

public class ButtonData {
	public World world;
	public int x,y,z;
	
	public ButtonData(World world, int x, int y, int z){
		setLocation(world, x, y, z);
	}
	
	public void setLocation(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String locationToString() {
		return x+", "+y+", "+z;
	}
}
