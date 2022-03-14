package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class ArcherType implements CustomEntityType<Archer>{
	
	private static ArcherType instance;

	public ArcherType() {
		
	}

	public static ArcherType getInstance () {
		if (instance == null) {
			instance = new ArcherType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "archer";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Archer createInstance(Location loc, EntityHandler handler) {
		return new Archer(loc, handler);
	}

}
