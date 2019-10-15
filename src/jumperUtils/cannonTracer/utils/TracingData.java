package jumperUtils.cannonTracer.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class TracingData {
	public long timeOfCreation;
	public HashMap<Integer, ArrayList<Long>> ticksAlive = new HashMap<>();
	public double x, y, z;
	
	public void initialize(double x, double y, double z) {
		this.x = x;
		this.y = y + 0.49;
		this.z = z;
	}
	
	public TracingData(double x, double y, double z, int entityID) {
		initialize(x, y, z);
		this.ticksAlive.clear();
		long time = System.currentTimeMillis();
		this.timeOfCreation = time;
		addTick(entityID, time);
	}
	
	public void addTick(int entityID, long time) {
		if(!ticksAlive.containsKey(entityID)){
			ticksAlive.put(entityID, new ArrayList<Long>());
		}
		ArrayList<Long> list = ticksAlive.get(entityID);
		if(!list.contains(time)) {
			list.add(time);
		}
	}
	
	/*public TracingData(String ID, double x, double y, double z, int entityID, ArrayList<Long> ticksAlive) {
		initialize(ID, x, y, z, entityID);
		this.ticksAlive.clear();
		for(long tick : ticksAlive) {
			this.ticksAlive.add(tick);
		}
	}*/
	
	public boolean isNewData(double x, double y, double z) {
		if(this.x == x && this.y == (y+0.49) && this.z == z) {
			return false;
		}
		return true;
	}
}
