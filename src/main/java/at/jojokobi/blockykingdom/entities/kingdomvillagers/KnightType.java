package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KnightType implements CustomEntityType<Knight>{
	
	private static KnightType instance;

	public KnightType() {
		
	}

	public static KnightType getInstance () {
		if (instance == null) {
			instance = new KnightType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "knight";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Knight createInstance(Location loc, EntityHandler handler) {
		return new Knight(loc, handler);
	}

}
