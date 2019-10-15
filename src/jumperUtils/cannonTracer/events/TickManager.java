package jumperutils.cannontracer.events;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Entity;

import jumperutils.cannontracer.CannonTracer;
import jumperutils.cannontracer.utils.EntityDataChain;
import jumperutils.cannontracer.utils.EntityDataChainLink;
import jumperutils.cannontracer.utils.SimpleLocation;

public class TickManager implements Runnable{
	public CannonTracer cannonTracer;
	private HashMap<String, Float> memoryTime;
	
	public TickManager(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getScheduler().scheduleSyncRepeatingTask(cannonTracer.main, this, 1, 1);
	}
	
	//runs every server tick
	public void run() {
		Set<Entity> trackedEntities = cannonTracer.tntSpawnListener.trackedEntities;
		HashMap<String, HashMap<Integer, EntityDataChain>> tracingHistory = cannonTracer.tntSpawnListener.tracingHistory;
		if(cannonTracer.tntSpawnListener.settingsChanged) {
			cannonTracer.tntSpawnListener.updateTracingHistory();
			memoryTime = cannonTracer.tntSpawnListener.buildMaxMemoryTime();
			cannonTracer.tntSpawnListener.settingsChanged = false;
		}
		if(trackedEntities == null || trackedEntities.size() <= 0) {
			return;
		}
		//remove too old chains
		try {
			for(String key : tracingHistory.keySet()) {
				long memoryLength = new Long((int)(memoryTime.get(key) * 1000l));
				HashMap<Integer, EntityDataChain> entityData = tracingHistory.get(key);
				for(Iterator<Integer> it = entityData.keySet().iterator(); it.hasNext(); ) {
					int entityID = it.next();
					if(System.currentTimeMillis() - entityData.get(entityID).timeOfCreation >= memoryLength) {
						it.remove();
					}
				}
			}
		}catch(ConcurrentModificationException e) {
			System.out.println("G-Good! ...Not like I wanted it to work or anything! ...b-baka");
			e.printStackTrace();
		}
		
		//write new data
		try {
			for(Iterator<Entity> it = trackedEntities.iterator(); it.hasNext(); ) {
				Entity entity = it.next();
				if(entity.isDead()) {
					it.remove();
					continue;
				}
				if(entity.getTicksLived() <= 0) {
					continue;
				}
				String entityName = entity.getClass().getSimpleName();
				SimpleLocation location = new SimpleLocation(entity.getLocation());
				HashMap<Integer, EntityDataChain> entityData = tracingHistory.get(entityName);
				if(!entityData.containsKey(entity.getEntityId())) {
					EntityDataChain dataChain = new EntityDataChain(new EntityDataChainLink(location));
					entityData.put(entity.getEntityId(), dataChain);
				}else {
					entityData.get(entity.getEntityId()).addLinkAtEnd(new EntityDataChainLink(location));
				}
			}
		}catch(ConcurrentModificationException e) {
			System.out.println("G-Good! ...Not like I wanted it to work or anything! ...b-baka");
			e.printStackTrace();
		}
	}
}
