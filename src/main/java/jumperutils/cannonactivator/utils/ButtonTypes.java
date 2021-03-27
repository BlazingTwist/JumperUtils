package jumperutils.cannonactivator.utils;

import java.util.HashMap;

import org.bukkit.Material;

public class ButtonTypes {
	public enum ButtonType {
		LEVER,
		WOOD_BUTTON,
		STONE_BUTTON,
		OBSERVER
	}

	private static HashMap<Material, ButtonType> typeData = null;

	public static HashMap<Material, ButtonType> getTypeData() {
		if(typeData == null){
			typeData = new HashMap<>();
			typeData.put(Material.STONE_BUTTON, ButtonType.STONE_BUTTON);
			typeData.put(Material.ACACIA_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.BIRCH_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.DARK_OAK_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.JUNGLE_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.OAK_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.SPRUCE_BUTTON, ButtonType.WOOD_BUTTON);
			typeData.put(Material.LEVER, ButtonType.LEVER);
			typeData.put(Material.OBSERVER, ButtonType.OBSERVER);
		}
		return typeData;
	}
}
