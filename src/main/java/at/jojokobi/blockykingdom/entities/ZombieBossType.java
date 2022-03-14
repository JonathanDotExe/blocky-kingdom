package at.jojokobi.blockykingdom.entities;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class ZombieBossType implements CustomEntityType<ZombieBoss>{
	
	private static ZombieBossType instance;

	public ZombieBossType() {
		
	}
	
	public static ZombieBossType getInstance () {
		if (instance == null) {
			instance = new ZombieBossType();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "zombie_boss";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public ZombieBoss createInstance(Location loc, EntityHandler handler) {
		return new ZombieBoss(loc, handler);
	}

}
