package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public class RecruiterType implements CustomEntityType<KingdomVillager<?>>{
	
	private static RecruiterType instance;

	public RecruiterType() {
		
	}

	public static RecruiterType getInstance () {
		if (instance == null) {
			instance = new RecruiterType();
		}
		return instance;
	}
	
	@Override
	public String getIdentifier() {
		return "recruiter";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Recruiter createInstance(Location loc, EntityHandler handler) {
		return new Recruiter(loc, handler);
	}

}
