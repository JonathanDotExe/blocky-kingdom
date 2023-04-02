package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.entities.Goblin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnerHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class GoblinHut extends Structure {

	private CustomEntitySpawnerHandler handler;

	private static final int ROOF_HEIGHT = 2;

	private LootInventory loot = new LootInventory();

	public GoblinHut(CustomEntitySpawnerHandler handler) {
		super(6, 6, 4 + ROOF_HEIGHT, 1000);
		this.handler = handler;
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.WHEAT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.BREAD), 1, 2));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_CHESTPLATE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_HELMET), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_BOOTS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_LEGGINGS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.WOODEN_SWORD), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.WOODEN_AXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_INGOT), 1, 5));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.GOLD_INGOT), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 5));
	}

	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return super.canGenerate(chunk, seed) && chunk.getWorld().getEnvironment() == Environment.NORMAL;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.OAK_PLANKS));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		// Walls
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					Material block = Material.AIR;
					if (y >= getHeight() - 1 - ROOF_HEIGHT) {
						int layersFree = Math.max(0, y - (getHeight() - ROOF_HEIGHT));
						if (x >= layersFree && x <= getWidth() - layersFree - 1) {
							block = Material.HAY_BLOCK;
						}
					} else if ((x == 0 || x == getWidth() - 1) && (z == 0 || z == getLength() - 1)) {
						block = Material.OAK_LOG;
					} else if (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1 || y == 0) {
						block = Material.OAK_PLANKS;
					}

					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					if (block != Material.AIR) {
						place.getBlock().setType(block);
					}
				}
			}
		}
		// Spawner
		place.setX(loc.getX() + getWidth() / 2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength() / 2);
		place.getBlock().setType(Material.SPAWNER);
		CreatureSpawner spawner = (CreatureSpawner) place.getBlock().getState();
		spawner.setSpawnedType(EntityType.ZOMBIE);
		handler.makeCustomSpawner(spawner, Goblin.GOBLIN_SPAWN_KEY);
		spawner.update();

		// Chest
		place.setX(loc.getX() + 1);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + 1);
		place.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) place.getBlock().getState();
		loot.fillInventory(chest.getBlockInventory(), random, null);

		// Door
		place.setX(loc.getX() + getWidth() - 1);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + 2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.EAST, false, true);
		return Arrays.asList(new StructureInstance<GoblinHut>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "goblin_hut";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Structure>(this, location, getWidth(), getHeight(), getLength());
	}

}
