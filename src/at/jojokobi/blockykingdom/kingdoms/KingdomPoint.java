package at.jojokobi.blockykingdom.kingdoms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.VillagerCategory;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomPoint implements ConfigurationSerializable {

	private static final String WORLD_TAG = "world";
	private static final String X_TAG = "x";
	private static final String Z_TAG = "z";

	private World world;
	private int x;
	private int z;

	public KingdomPoint(Location place) {
		this(place.getWorld(), 0, 0);
		x = place.getBlockX() / Kingdom.KINGDOM_WIDTH;
		z = place.getBlockZ() / Kingdom.KINGDOM_LENGTH;
		if (place.getBlockX() < 0) {
			x--;
		}
		if (place.getBlockZ() < 0) {
			z--;
		}
	}

	public KingdomPoint(World world, int x, int z) {
		super();
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public Kingdom toKingdom() {
		return KingdomHandler.getInstance().getKingdom(this);
	}

	public Location toLocation() {
		return new Location(world, x * Kingdom.KINGDOM_WIDTH, 0, z * Kingdom.KINGDOM_LENGTH);
	}

	public void addVillager(KingdomVillager<?> villager) {
//		Kingdom kingdom = toKingdom();
		villager.setKingdomPoint(this);
//		kingdom.increaseVillagerCount(1);
	}
	
//	@Deprecated
//	public void countVillagers (Kingdom kingdom, EntityHandler handler) {
//		kingdom.setVillagerCount(countVillagers(handler));
//	}
	
	public int calcVillagerCount (EntityHandler handler) {
		int count = 0;
		for (CustomEntity<?> entity : handler.getEntities()) {
			if (entity instanceof KingdomVillager<?> && equals(((KingdomVillager<?>) entity).getKingdomPoint())) {
				count++;
			}
		}
		return count;
	}
	
	public Map<VillagerCategory, Integer> countVillagersByCatergory (EntityHandler entityHandler) {
		Map<VillagerCategory, Integer> count = new HashMap<>();
		for (KingdomVillager<?> villager : getVillagers(entityHandler)) {
			if (!count.containsKey(villager.getVillagerCategory())) {
				count.put(villager.getVillagerCategory(), 0);
			}
			count.put(villager.getVillagerCategory(), count.get(villager.getVillagerCategory()) + 1);
		}
		return count;
	}
	
	public List<KingdomVillager<?>> getVillagers (EntityHandler handler) {
		List<KingdomVillager<?>> villagers = new ArrayList<>();
		for (CustomEntity<?> entity : handler.getEntities()) {
			if (entity instanceof KingdomVillager<?> && equals(((KingdomVillager<?>) entity).getKingdomPoint())) {
				villagers.add((KingdomVillager<?>) entity);
			}
		}
		return villagers;
	}
	
	public boolean canAddVillager (VillagerCategory category, int count) {
		Kingdom kingdom = toKingdom();
		return count < category.getMaxAmount(kingdom.getLevel());
	}
	
	public boolean canAddVillager (VillagerCategory category, EntityHandler handler) {
		Integer count = countVillagersByCatergory(handler).get(category);
		return canAddVillager(category, count == null ? 0 : count);
	}
	
	public void calcHappiness (Kingdom kingdom, EntityHandler handler) {
		kingdom.setHappiness(calcHappiness(handler));
	}
	
	public double calcHappiness (EntityHandler handler) {
		double happiness = 0;
		for (CustomEntity<?> entity : handler.getEntities()) {
			if (entity instanceof KingdomVillager<?> && equals(((KingdomVillager<?>) entity).getKingdomPoint())) {
				happiness += ((KingdomVillager<?>) entity).getKingdomHappiness();
			}
		}
		return happiness;
	}

//	private void loadAllChunks() {
//		int width = Kingdom.KINGDOM_WIDTH / TerrainGenUtil.CHUNK_WIDTH;
//		int length = Kingdom.KINGDOM_LENGTH / TerrainGenUtil.CHUNK_LENGTH;
//		int startX = this.x * TerrainGenUtil.CHUNK_WIDTH;
//		int startZ = this.z * TerrainGenUtil.CHUNK_LENGTH;
//		
//		for (int x = 0; x < width; x++) {
//			for (int z = 0; z < length; z++) {
//				world.loadChunk(startX + x, startZ + z, false);
//			}
//		}
//	}

	public void saveToXML(Element element, Document document) {
		Element worldElement = document.createElement(WORLD_TAG);
		worldElement.setTextContent(world.getName());
		Element xElement = document.createElement(X_TAG);
		xElement.setTextContent(x + "");
		Element zElement = document.createElement(Z_TAG);
		zElement.setTextContent(z + "");

		element.appendChild(worldElement);
		element.appendChild(xElement);
		element.appendChild(zElement);
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(WORLD_TAG, world.getName());
		map.put(X_TAG, x);
		map.put(Z_TAG, z);
		return map;
	}

	public static KingdomPoint fromXML(Element element) {
		KingdomPoint point = new KingdomPoint(null, 0, 0);
		Node worldTag = element.getElementsByTagName(WORLD_TAG).item(0);
		if (worldTag != null) {
			point.world = Bukkit.getWorld(worldTag.getTextContent());
		}

		Node xTag = element.getElementsByTagName(X_TAG).item(0);
		if (xTag != null) {
			try {
				point.x = Integer.parseInt(xTag.getTextContent());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		Node zTag = element.getElementsByTagName(Z_TAG).item(0);
		if (zTag != null) {
			try {
				point.z = Integer.parseInt(zTag.getTextContent());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (point.getWorld() == null) {
			point = null;
		}

		return point;
	}
	
	public static KingdomPoint deserialize (Map<String, Object> map) {
		KingdomPoint point = new KingdomPoint(null, 0, 0);
		//World
		if (map.get(WORLD_TAG) != null) {
			point.world = Bukkit.getWorld(map.get(WORLD_TAG) + "");
		}
		//X
		try {
			point.x = Integer.parseInt(map.get(X_TAG) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		//Z
		try {
			point.z = Integer.parseInt(map.get(Z_TAG) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return point.world == null ? null : point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KingdomPoint other = (KingdomPoint) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "KingdomPoint [world=" + (world.getName()) + ", x=" + x + ", z=" + z + "]";
	}

}
