package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.entities.EliteGoblin;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class GoblinLibraryRoom extends Structure {


	private EntityHandler handler;
	private LootInventory loot = new LootInventory();

	public GoblinLibraryRoom(EntityHandler handler) {
		super(9, 9, 7, 0, 1);
		this.handler = handler;
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 2, 5));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(Hammer.class), 1, 1));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_CHESTPLATE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_HELMET), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_BOOTS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.LEATHER_LEGGINGS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.6, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.GOLD_INGOT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 5));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.STRING), 1, 3));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.BOOK), 1, 5));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.PAPER), 1, 16));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.MAP), 1, 1));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.FILLED_MAP), 1, 1));
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return new Random(generateValueBeasedSeed(place, place.getWorld().getSeed()))
				.nextInt(Math.max(1, super.calculatePlacementY(width, length, place) - 5)) + 5;
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
					if (x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getWidth() - 1) {
						material = Material.STONE_BRICKS;
					}
					else if (((x == 1 || x == getWidth() - 1) && (z == 1 || z == getLength() - 1)) && (x < getWidth()/2 - 1 || x > getWidth()/2 + 1) && (z < getLength()/2 - 1 || z > getLength()/2 + 1)) {
						material = Material.BOOKSHELF;
					}
					else if (random.nextInt(24) == 0){
						material = Material.COBWEB;
					}
					place.getBlock().setType(material);
				}
			}
		}
		//Doors
		place.setY(loc.getY() + 1);
		place.setX(loc.getX());
		place.setZ(loc.getZ() + getLength()/2);
		FurnitureGenUtil.generateDoor(place, Material.SPRUCE_DOOR, BlockFace.WEST, false);
		place.setX(loc.getX() + getWidth() - 1);
		FurnitureGenUtil.generateDoor(place, Material.SPRUCE_DOOR, BlockFace.EAST, false);
		place.setX(loc.getX() + getWidth()/2);
		place.setZ(loc.getZ());
		FurnitureGenUtil.generateDoor(place, Material.SPRUCE_DOOR, BlockFace.NORTH, false);
		place.setZ(loc.getZ() + getLength() - 1);
		FurnitureGenUtil.generateDoor(place, Material.SPRUCE_DOOR, BlockFace.SOUTH, false);
		// Chest
		place = loc.clone().add(getWidth()/2, 1, getLength()/2);
		
		place.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) place.getBlock().getState();
		loot.fillInventory(chest.getBlockInventory(), random, null);
		//Top
		place.add(0, getLength() - 3, 0);
		place.getBlock().setType(Material.SEA_LANTERN);
		//Entity
		place.setY(loc.getY() + 2);
		handler.addSavedEntity(new EliteGoblin(place, handler));
		
		return Arrays.asList(new StructureInstance<>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "goblin_library_room";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<>(this, location, getWidth(), getHeight(), getLength());
	}

}
