package at.jojokobi.blockykingdom.entities;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class GhostType implements CustomEntityType<Ghost>{
	
	private static GhostType instance;

	public GhostType() {
		
	}
	
	public static GhostType getInstance () {
		if (instance == null) {
			instance = new GhostType();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "ghost";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Ghost createInstance(Location loc, EntityHandler handler) {
		return new Ghost(loc, handler);
	}

}
