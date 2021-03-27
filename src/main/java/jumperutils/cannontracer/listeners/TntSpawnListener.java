package jumperutils.cannontracer.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.stream.Collectors;
import jumperutils.cannontracer.utils.SimpleLocation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.google.common.collect.Sets;

import jumperutils.cannontracer.CannonTracer;
import jumperutils.cannontracer.utils.EntityDataChain;
import jumperutils.cannontracer.utils.EntityDataChainLink;
import jumperutils.cannontracer.utils.UserSettings;

public class TntSpawnListener implements Listener {
	public final CannonTracer cannonTracer;

	public final HashMap<String, HashMap<Integer, EntityDataChain>> tracingHistory = new HashMap<>();
	public final Set<Entity> trackedEntities = Sets.newHashSet();
	public boolean settingsChanged = false;

	private final HashMap<Player, UserSettings> playerSettings = new HashMap<>();

	public HashMap<Player, UserSettings> getPlayerSettings() {
		return playerSettings;
	}

	public void notifySettingsChanged(){
		settingsChanged = true;
	}

	public TntSpawnListener(CannonTracer cannonTracer) {
		this.cannonTracer = cannonTracer;
		cannonTracer.main.getServer().getPluginManager().registerEvents(this, cannonTracer.main);
	}

	@EventHandler
	public void onTntSpawn(EntitySpawnEvent e) {
		Entity entity = e.getEntity();
		String entityName = entity.getClass().getSimpleName();
		for (Player p : playerSettings.keySet()) {
			if (!p.isOnline()) {
				continue;
			}
			if (!playerSettings.get(p).listenToEntitySpawns) {
				continue;
			}
			p.sendMessage("§aFound entity §b" + entityName + " §aat location §b" + locationToString(entity.getLocation()));
		}
		if (entityIsListenedTo(entityName) && getMemoryTime(entityName) > 0) {
			trackedEntities.add(entity);
			tracingHistory.get(entityName)
					.put(entity.getEntityId(),
							new EntityDataChain(new EntityDataChainLink(new SimpleLocation(entity.getLocation()))));
		}
	}

	@EventHandler
	public void onTntExplode(EntityExplodeEvent e) {
		Entity entity = e.getEntity();
		String entityName = entity.getClass().getSimpleName();
		if (entityIsListenedTo(entityName) && getMemoryTime(entityName) > 0) {
			HashMap<Integer, EntityDataChain> entityData = tracingHistory.get(entityName);
			if (entityData.containsKey(entity.getEntityId())) {
				entityData.get(entity.getEntityId()).addLinkAtEnd(new EntityDataChainLink(new SimpleLocation(entity.getLocation())));
			}
		}
	}

	public String locationToString(Location loc) {
		return loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
	}

	public HashMap<String, Float> buildMaxMemoryTime() {
		HashMap<String, Float> result = new HashMap<>();
		for (UserSettings settings : playerSettings.values()) {
			for (String key : settings.observedEntities.keySet()) {
				if (!result.containsKey(key)) {
					result.put(key, settings.observedEntities.get(key));
				} else {
					if (result.get(key) < settings.observedEntities.get(key)) {
						result.put(key, settings.observedEntities.get(key));
					}
				}
			}
		}
		return result;
	}

	public boolean entityIsListenedTo(String entityID) {
		for (UserSettings settings : playerSettings.values()) {
			if (settings.observedEntities.containsKey(entityID)) {
				return true;
			}
		}
		return false;
	}

	public float getMemoryTime(String entityID) {
		float max = 0;
		for (UserSettings settings : playerSettings.values()) {
			if (!settings.observedEntities.containsKey(entityID)) {
				continue;
			}
			if (settings.observedEntities.get(entityID) > max) {
				max = settings.observedEntities.get(entityID);
			}
		}
		return max;
	}

	public void updateTracingHistory() {
		tracingHistory.keySet().removeIf(key -> !entityIsListenedTo(key));
		for (UserSettings settings : playerSettings.values()) {
			for (String key : settings.observedEntities.keySet()) {
				if (!tracingHistory.containsKey(key)) {
					tracingHistory.put(key, new HashMap<>());
				}
			}
		}
	}

	public void handlePullRequest(Player p) {
		if (!playerSettings.containsKey(p)) {
			return;
		}
		long requestTime = System.currentTimeMillis();
		SimpleLocation playerPos = new SimpleLocation(p.getLocation());
		UserSettings settings = playerSettings.get(p);

		for (Map.Entry<String, Float> observerEntry : settings.observedEntities.entrySet()) {
			long memoryTime = observerEntry.getValue().longValue() * 1000L;

			List<EntityDataChain> relevantEntities = tracingHistory.get(observerEntry.getKey())
					.values().stream()
					.filter(x -> x.timeOfCreation + memoryTime >= requestTime) // ignore entities that are too old
					.filter(x -> x.isInRange(playerPos, settings.maxRange)) // ensure that tnt is in trace-range
					.collect(Collectors.toList());

			long firstCreationTime = relevantEntities.stream().map(x -> x.timeOfCreation).min(Long::compare).orElse(0L);

			for (EntityDataChain dataChain : relevantEntities) {
				int relativeTick = (int) Math.round((dataChain.timeOfCreation - firstCreationTime) / 50d);

				//NON normalizing version
				//requestTime - memoryTime == beginning
				//dataChain.timeOfCreation - beginning == respective chain tick
				// /1000 *20
				//int relativeTick = (int) Math.round((dataChain.timeOfCreation + memoryTime - requestTime) / 50d);

				//target message: [JumperCannonTracer]EntityName|creationTick|x,y,z|x,y,z|x,y,z....
				StringBuilder message = new StringBuilder("[JumperCannonTracer]" + observerEntry.getKey() + "|" + relativeTick + "|" + dataChain.start.location.getString());
				for (EntityDataChainLink chainLink = dataChain.start; !chainLink.isLastLink(); ) {
					chainLink = chainLink.nextLink;
					message.append("|").append(chainLink.location.getString());
				}
				p.sendMessage(message.toString());
			}
		}
	}


}