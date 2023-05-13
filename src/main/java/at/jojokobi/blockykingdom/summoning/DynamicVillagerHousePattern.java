package at.jojokobi.blockykingdom.summoning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class DynamicVillagerHousePattern implements SummoningPattern {
	
	private EntityHandler handler;
	private GenerationHandler genHandler;
	private Structure structure;
	private BiFunction<Location, EntityHandler, KingdomVillager<?>> villagerFunction;
	private Function<String, String> messageFunction;
	private Vector minSize;
	private Vector maxSize;
	private List<Furniture> furnitures;
	private Predicate<Block> wallPredicate;
	private double airInside = 0.5;
	private List<Vector> offsets = Arrays.asList(new Vector(-1, -1, -1));
	

	public DynamicVillagerHousePattern(EntityHandler handler, GenerationHandler genHandler, Structure structure,
			BiFunction<Location, EntityHandler, KingdomVillager<?>> villagerFunction,
			Function<String, String> messageFunction, Vector minSize, Vector maxSize, List<Furniture> furnitures,
			Predicate<Block> wallPredicate, double airInside) {
		super();
		this.handler = handler;
		this.genHandler = genHandler;
		this.structure = structure;
		this.villagerFunction = villagerFunction;
		this.messageFunction = messageFunction;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.furnitures = furnitures;
		this.wallPredicate = wallPredicate;
		this.airInside = airInside;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		Kingdom kingdom = KingdomHandler.getInstance().getKingdom(event.getBlock().getLocation());
		if ((furnitures.isEmpty() || furnitures.get(0).matches(event.getBlock())) && kingdom.isOwner(event.getPlayer().getUniqueId())) {
			System.out.println("DEtected furniture");
			for (Vector offset : offsets) {
				for (int width = minSize.getBlockX(); width <= maxSize.getBlockX(); width++) {
					for (int height = minSize.getBlockY(); height <= maxSize.getBlockY(); height++) {
						for (int length = minSize.getBlockZ(); length <= maxSize.getBlockZ(); length++) {
							if (matches(event.getBlock().getLocation().add(offset), width, height, length)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean matches (Location start, int width, int height, int length) {
		System.out.println("Checking " + width + "/" + height + "/" + length);
		boolean matches = genHandler.getInstanceInArea(start, width, height, length) == null;
		System.out.println("Area: " + matches);
		List<Furniture> furnitures = new ArrayList<Furniture>(this.furnitures);
		int airCount = 0;
		int totalRoomSpace = (width - 2) * (height - 2) * (length - 2);
		for (int x = 0; x < width && matches; x++) {
			for (int y = 0; y < height && matches; y++) {
				for (int z = 0; z < length && matches; z++) {
					Block block = start.getBlock().getRelative(x, y, z);
					//Wall
					if (x == 0 || x == width - 1 || y == 0 || y == height - 1 || z == 0 || z == length - 1) {
						matches = wallPredicate.test(block);
					}
					//Air
					else {
						if (!block.getType().isSolid()) {
							airCount++;
						}
						//Furniture
						for (Iterator<Furniture> iterator = furnitures.iterator(); iterator.hasNext();) {
							Furniture furniture = iterator.next();
							if (furniture.matches(block)) {
								iterator.remove();
								break;
							}
						}
					}
				}
			}
		}
		System.out.println(furnitures);
		System.out.println(matches + "/" + furnitures.isEmpty() + "/" + ((double) airCount/totalRoomSpace >= airInside));
		return matches && furnitures.isEmpty() && (double) airCount/totalRoomSpace >= airInside;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		KingdomPoint point = new KingdomPoint(event.getBlock().getLocation());
		KingdomVillager<?> villager = villagerFunction.apply(event.getBlock().getLocation().add(1, 1, 1), handler);
		if (!point.canAddVillager(villager.getVillagerCategory(), handler)) {
			event.getPlayer().sendMessage("Cool building but nobody wants to move here because of the low level. Increase your kingdom level to be able to increase your population.");
		}
		else {
			handler.addSavedEntity(villager);
			point.addVillager(villager);
			event.getPlayer().sendMessage(messageFunction.apply(villager.getName()));
			//Spawn house
			Location start = event.getBlock().getLocation().add(-1, -1, -1);
			StructureInstance<?> inst = structure.getStandardInstance(start);
			genHandler.addStructureInstance(inst);
		}
	}

}

interface Furniture {
	
	public boolean matches (Block block);
	
}

class SingleBlockFuniture implements Furniture{
	
	private Material material;

	public SingleBlockFuniture(Material material) {
		super();
		this.material = material;
	}

	@Override
	public boolean matches(Block block) {
		return block.getType() == material;
	}
	
}


class TableFurniture implements Furniture{
	

	public TableFurniture() {

	}

	@Override
	public boolean matches(Block block) {
		return block.getType() == Material.OAK_FENCE && block.getRelative(0, 1, 0).getType() == Material.OAK_PRESSURE_PLATE;
	}
	
}
