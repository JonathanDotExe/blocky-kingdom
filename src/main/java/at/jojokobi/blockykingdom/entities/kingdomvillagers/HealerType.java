package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class HealerType implements CustomEntityType<Healer>{
	
	private static HealerType instance;

	public HealerType() {
		
	}

	public static HealerType getInstance () {
		if (instance == null) {
			instance = new HealerType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "healer";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Healer createInstance(Location loc, EntityHandler handler) {
		return new Healer(loc, handler);
	}


}
