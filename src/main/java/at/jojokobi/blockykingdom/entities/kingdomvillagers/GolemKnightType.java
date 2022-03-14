package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class GolemKnightType implements CustomEntityType<GolemKnight>{
	
	private static GolemKnightType instance;

	public GolemKnightType() {
		
	}

	public static GolemKnightType getInstance () {
		if (instance == null) {
			instance = new GolemKnightType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "golem_knight";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public GolemKnight createInstance(Location loc, EntityHandler handler) {
		return new GolemKnight(loc, handler);
	}

}
