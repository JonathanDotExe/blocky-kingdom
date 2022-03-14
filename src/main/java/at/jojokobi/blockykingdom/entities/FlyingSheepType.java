package at.jojokobi.blockykingdom.entities;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class FlyingSheepType implements CustomEntityType<FlyingSheep>{
	
	private static FlyingSheepType instance;

	public FlyingSheepType() {
		
	}
	
	public static FlyingSheepType getInstance () {
		if (instance == null) {
			instance = new FlyingSheepType();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "flying_sheep";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public FlyingSheep createInstance(Location loc, EntityHandler handler) {
		return new FlyingSheep(loc, handler);
	}

}
