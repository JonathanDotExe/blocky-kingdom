package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.entities.EliteGoblin;
import at.jojokobi.blockykingdom.items.CloudParticle;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnerHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class EliteGoblinSpawnerRoom extends Structure {

	private CustomEntitySpawnerHandler handler;
	private LootInventory loot = new LootInventory();

	public EliteGoblinSpawnerRoom(CustomEntitySpawnerHandler handler) {
		super(9, 9, 7, 1200, 1);
		this.handler = handler;
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 3, 8));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.WHEAT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.BREAD), 1, 2));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_CHESTPLATE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_HELMET), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_BOOTS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_LEGGINGS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_SWORD), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_AXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_INGOT), 3, 8));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.GOLD_INGOT), 5, 8));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 5));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.GOLD_BLOCK), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENDER_PEARL), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BLAZE_POWDER), 1, 3));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.SLIME_BALL), 1, 3));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(CloudParticle.class), 1, 5));
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return new Random(generateValueBeasedSeed(place, place.getWorld().getSeed()))
				.nextInt(Math.max(1, super.calculatePlacementY(width, length, place) - 5)) + 5;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
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
						material = random.nextInt(3) == 0 ? Material.IRON_BARS : Material.STONE_BRICKS;
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
		handler.makeCustomSpawner(spawner, EliteGoblin.ELITE_GOBLIN_SPAWN_KEY);
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
		return "elite_goblin_spawner_room";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<>(this, location, getWidth(), getHeight(), getLength());
	}

}
