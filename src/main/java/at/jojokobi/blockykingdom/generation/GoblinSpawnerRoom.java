package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.entities.Goblin;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnerHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class GoblinSpawnerRoom extends Structure {

	private CustomEntitySpawnerHandler handler;
	private DimensionHandler dimHandler;
	private LootInventory loot = new LootInventory();

	public GoblinSpawnerRoom(CustomEntitySpawnerHandler handler, DimensionHandler dimHandler) {
		super(9, 9, 7, 800, 1);
		this.handler = handler;
		this.dimHandler = dimHandler;
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 2, 5));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(Hammer.class), 1, 1));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.WHEAT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.BREAD), 1, 2));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.COOKED_BEEF), 1, 3));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.GOLDEN_APPLE), 1, 1));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_CHESTPLATE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_HELMET), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_BOOTS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_LEGGINGS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.WOODEN_SWORD), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.WOODEN_AXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.6, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.GOLD_INGOT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 5));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.GOLD_BLOCK), 1, 1));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.STRING), 1, 3));
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return new Random(generateValueBeasedSeed(place, place.getWorld().getSeed()))
				.nextInt(Math.max(1, super.calculatePlacementY(width, length, place) - 5)) + 5;
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return super.canGenerate(chunk, seed) && chunk.getWorld().getEnvironment() == Environment.NORMAL && dimHandler.getDimension(chunk.getWorld()) == null;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		loc.add(0, -1, 0);
		Location place = loc.clone();
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		// Walls
		for (int x = 0; x < getWidth(); x++) {
			place.setX(loc.getX() + x);
			for (int y = 0; y < getHeight(); y++) {
				place.setY(loc.getY() + y);
				for (int z = 0; z < getLength(); z++) {
					place.setZ(loc.getZ() + z);
					Material material = Material.AIR;
					if (((x == 0 || x == getWidth() - 1)
							&& (z < getLength() / 2 - 1 || z > getLength() / 2 + 2 || y > 3))
							|| ((z == 0 || z == getLength() - 1)
									&& (x < getWidth() / 2 - 1 || x > getWidth() / 2 + 2 || y > 3))
							|| y == 0 || y == getHeight() - 1) {
						material = random.nextBoolean() ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE;
					}
					else if (y == 1 && random.nextInt(8) == 0) {
						material = Material.HAY_BLOCK;
					}
					place.getBlock().setType(material);
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
		
		return Arrays.asList(new StructureInstance<>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "goblin_spawner_room";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<>(this, location, getWidth(), getHeight(), getLength());
	}

}
