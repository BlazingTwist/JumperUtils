package jumperutils.cannontracer.utils;

import java.util.HashMap;

public class UserSettings {
	public final HashMap<String, Float> observedEntities = new HashMap<>();
	public boolean listenToEntitySpawns = false;
	public int maxRange = -1;

	public void interpretConfigMessage(String message) {
		observedEntities.clear();
		String[] valueSets = message.split("\\|");
		String[] keyVal;
		String[] values;
		for (String valueSet : valueSets) {
			keyVal = valueSet.split("=");
			switch (keyVal[0]) {
				case "logIDs":
					listenToEntitySpawns = Boolean.parseBoolean(keyVal[1]);
					break;
				case "range":
					maxRange = Integer.parseInt(keyVal[1]);
					break;
				case "[entity]":
					values = keyVal[1].split(";");
					observedEntities.put(values[0], Float.parseFloat(values[1]));
					break;
			}
		}
	}
}