package jumperUtils.cannonTracer.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.google.common.collect.Sets;

import jumperUtils.cannonTracer.CannonTracer;
import jumperUtils.cannonTracer.utils.EntityDataChain;
import jumperUtils.cannonTracer.utils.EntityDataChainLink;
import jumperUtils.cannonTracer.utils.UserSettings;

public class TntSpawnListener implements Listener{
	public final CannonTracer cannonTracer;
	public Set<Entity> trackedEntities = Sets.<Entity>newHashSet();
	public HashMap<String, HashMap<Integer, EntityDataChain>> tracingHistory = new HashMap<>();
	private HashMap<Player, UserSettings> playerSettings = new HashMap<>();
	public boolean settingsChanged = false;
	
	public HashMap<Player, UserSettings> getPlayerSettings(){
		return playerSettings;
	}
	public HashMap<Player, UserSettings> getPlayerSettings(boolean settingsUpdate){
		if(settingsUpdate) {
			settingsChanged = true;
		}
		return playerSettings;
	}
	
	public TntSpawnListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}
	
	@EventHandler
	public void onTntSpawn(EntitySpawnEvent e) {
		Entity entity = e.getEntity();
		if(entity == null) {
			return;
		}
		String entityName = entity.getClass().getSimpleName();
		for(Player p : playerSettings.keySet()) {
			if(!p.isOnline()) {
				continue;
			}
			if(!playerSettings.get(p).listenToEntitySpawns) {
				continue;
			}
			p.sendMessage("§aFound entity §b"+entityName+" §aat location §b"+locationToString(entity.getLocation()));
		}
		if(entityIsListenedTo(entityName) && getMemoryTime(entityName) > 0) {
			trackedEntities.add(entity);
		}
	}
	
	public String locationToString(Location loc) {
		return loc.getX()+", "+loc.getY()+", "+loc.getZ();
	}
	
	public HashMap<String, Float> buildMaxMemoryTime(){
		HashMap<String, Float> result = new HashMap<>();
		for(UserSettings settings : playerSettings.values()) {
			for(String key : settings.observedEntities.keySet()) {
				if(!result.containsKey(key)) {
					result.put(key, settings.observedEntities.get(key));
				}else {
					if(result.get(key) < settings.observedEntities.get(key)) {
						result.put(key, settings.observedEntities.get(key));
					}
				}
			}
		}
		return result;
	}
	
	public boolean entityIsListenedTo(String entityID) {
		for(UserSettings settings : playerSettings.values()) {
			if(settings.observedEntities.containsKey(entityID)) {
				return true;
			}
		}
		return false;
	}
	
	public float getMemoryTime(String entityID) {
		float max = 0;
		for(UserSettings settings : playerSettings.values()) {
			if(!settings.observedEntities.containsKey(entityID)) {
				continue;
			}
			if(settings.observedEntities.get(entityID) > max) {
				max = settings.observedEntities.get(entityID);
			}
		}
		return max;
	}
	
	public void updateTracingHistory() {
		for(Iterator<String> it = tracingHistory.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			if(!entityIsListenedTo(key)) {
				it.remove();
			}
		}
		Set<String> keys = Sets.<String>newHashSet();
		for(UserSettings settings : playerSettings.values()) {
			for(String key : settings.observedEntities.keySet()) {
				if(!keys.contains(key)) {
					keys.add(key);
					if(!tracingHistory.containsKey(key)) {
						tracingHistory.put(key, new HashMap<>());
					}
				}
			}
		}
	}
	
	public void handlePullRequest(Player p) {
		if(!playerSettings.containsKey(p)) {
			return;
		}
		long requestTime = System.currentTimeMillis();
		UserSettings settings = playerSettings.get(p);
		for(String key : settings.observedEntities.keySet()) {
			long memoryTime = settings.observedEntities.get(key).longValue() * 1000l;
			HashMap<Integer, EntityDataChain> entityData = tracingHistory.get(key);
			for(int entityID : entityData.keySet()) {
				EntityDataChain dataChain = entityData.get(entityID);
				if(dataChain.timeOfCreation + memoryTime < requestTime) {
					//data too old for user
					continue;
				}
				//requestTime - memoryTime == beginning
				//dataChain.timeOfCreation - beginning == respective chain tick
				// /1000 *20
				int relativeTick = (int)Math.round((dataChain.timeOfCreation + memoryTime - requestTime) / 50d);
				//target message: [JumperCannonTracer]EntityName|creationTick|x,y,z|x,y,z|x,y,z....
				String message = "[JumperCannonTracer]"+key+"|"+relativeTick+"|"+dataChain.start.location.getString();
				for(EntityDataChainLink chainLink = dataChain.start; !chainLink.isLastLink(); ) {
					chainLink = chainLink.nextLink;
					message += "|"+chainLink.location.getString();
				}
				p.sendMessage(message);
			}
		}
	}
	
	
}