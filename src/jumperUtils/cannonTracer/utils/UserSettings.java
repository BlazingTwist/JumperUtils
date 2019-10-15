package jumperUtils.cannonTracer.utils;

import java.util.HashMap;

public class UserSettings {
	public HashMap<String, Float> observedEntities;
	public boolean listenToEntitySpawns;
	
	public UserSettings() {
		observedEntities = new HashMap<>();
		listenToEntitySpawns = false;
	}
	
	public void interpretConfigMessage(String message) {
		observedEntities.clear();
		String valueSets[] = message.split("\\|");
		String keyVal[];
		String values[];
		for(String valueSet : valueSets) {
			keyVal = valueSet.split("=");
			if(keyVal[0].equals("logIDs")){
				listenToEntitySpawns = Boolean.parseBoolean(keyVal[1]);
				System.out.println("detected logIDs, setting to: "+keyVal[1]);
			}else if(keyVal[0].equals("[entity]")) {
				values = keyVal[1].split(";");
				observedEntities.put(values[0], Float.parseFloat(values[1]));
				System.out.println("detected entity, setting to: "+values[0]+" | "+values[1]);
			}
		}
	}
}