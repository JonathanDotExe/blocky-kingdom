package at.jojokobi.blockykingdom.kingdoms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.VillagerCategory;

public class Kingdom implements ConfigurationSerializable{
	
	public static final int KINGDOM_WIDTH = 512;
	public static final int KINGDOM_LENGTH = 512;
	
	private static final String NAME_KEY = "name";
	private static final String CENTER_X_KEY = "centerX";
	private static final String CENTER_Z_KEY = "centerZ";
	private static final String STATE_KEY = "state";
	private static final String OWNERS_KEY = "owners";
	private static final String HAPPINESS_KEY = "happiness";
	private static final String LEVEL_KEY = "level";
	
	private String name = "Kingdom";
	private int centerX;
	private int centerZ;
	private KingdomState state = KingdomState.UNCLAIMED;
	private List<UUID> owners = new ArrayList<>();
	private double happiness = 0;
	private int level = 1;
	private boolean claimable = true;
	

	public Kingdom(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(int centerZ) {
		this.centerZ = centerZ;
	}

	public KingdomState getState() {
		return state;
	}

	public void setState(KingdomState state) {
		this.state = state;
	}
	
	public void addOwner (UUID owner) {
		if (state == KingdomState.UNCLAIMED) {
			state = KingdomState.GOOD;
		}
		owners.add(owner);
	}
	
	public boolean isOwner (UUID owner) {
		return owners.contains(owner);
	}
	
	public List<UUID> getOwners () {
		return new ArrayList<>(owners);
	}

//	public int maxVillagerCount () {
//		return 5 + level * 5;
//	}
	
	public double getHappiness() {
		return happiness;
	}

	public void setHappiness(double happiness) {
		this.happiness = happiness;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		
		map.put(NAME_KEY, getName());
		map.put(CENTER_X_KEY, getCenterX());
		map.put(CENTER_Z_KEY, getCenterZ());
		map.put(STATE_KEY, state.toString());
		map.put(HAPPINESS_KEY, happiness);
		map.put(LEVEL_KEY, level);
		
		List<String> owners = new LinkedList<>();
		for (UUID owner : this.owners) {
			owners.add(owner.toString());
		}
		map.put(OWNERS_KEY, owners);
		
		return map;
	}
	
	public static Kingdom valueOf (Map<String, Object> map) {
		//Name
		String name = "Kingdom";
		if (map.get(NAME_KEY) != null) {
			name = map.get(NAME_KEY).toString();
		}
		Kingdom kingdom = new Kingdom(name);
		
		//Center X
		try {
			kingdom.setCenterX(Integer.parseInt(map.get(CENTER_X_KEY).toString()));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		//Center Z
		try {
			kingdom.setCenterZ(Integer.parseInt(map.get(CENTER_Z_KEY).toString()));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		//State
		try {
			kingdom.setState(KingdomState.valueOf(map.get(STATE_KEY).toString()));
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		//Happiness
		try {
			if (map.get(HAPPINESS_KEY) != null) {
				kingdom.setHappiness(Double.parseDouble(map.get(HAPPINESS_KEY).toString()));
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		//Happiness
		try {
			if (map.get(LEVEL_KEY) != null) {
				kingdom.setLevel(Integer.parseInt(map.get(LEVEL_KEY).toString()));
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		//Owners
		if (map.get(OWNERS_KEY) instanceof List<?>) {
			for (Object owner : (List<?>) map.get(OWNERS_KEY)) {
				try {
					kingdom.addOwner(UUID.fromString(owner.toString()));
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		return kingdom;
	}
	
	public boolean isClaimable() {
		return claimable;
	}

	public void setClaimable(boolean claimable) {
		this.claimable = claimable;
	}
	
	public boolean claimable () {
		return claimable && owners.isEmpty();
	}

	public int getLevelUpPrice () {
		return 1000 * level;
	}
	
	public double getLevelUpHappiness () {
		double sum = 0;
		for (VillagerCategory category : VillagerCategory.values()) {
			sum += category.getMaxAmount(level);
		}
		return sum/2.0;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
