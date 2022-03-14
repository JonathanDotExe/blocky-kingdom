package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class TraderType implements CustomEntityType<Trader>{
	
	private static TraderType instance;

	public TraderType() {
		
	}

	public static TraderType getInstance () {
		if (instance == null) {
			instance = new TraderType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "trader";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Trader createInstance(Location loc, EntityHandler handler) {
		return new Trader(loc, handler);
	}

}
