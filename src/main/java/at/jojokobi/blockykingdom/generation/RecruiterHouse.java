package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Recruiter;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class RecruiterHouse extends Structure{

	private EntityHandler entityHandler;
	
	private LootInventory loot;
	
	public RecruiterHouse(EntityHandler entityHandler) {
		super(8, 8, 5, 0);
		this.entityHandler = entityHandler;
		
		loot = new LootInventory();
		
		loot.addItem(new LootItem(1, new ItemStack(Material.COAL), 1, 16));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 4));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.BOW), 1, 1));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.ARROW), 1, 10));	
		loot.addItem(new LootItem(0.2, new ItemStack(Material.CARROT), 1, 2));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_SWORD), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BOW), 1, 1));
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		setxModifier(81);
		setzModifier(-15155);
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		//Walls
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					BlockData data = Material.AIR.createBlockData();
					if (y == 0 || y == getHeight() - 1) {
						data = Material.SMOOTH_STONE_SLAB.createBlockData();
						((Slab) data).setType(Slab.Type.DOUBLE);
					}
					else if ((x == 0 || x == getWidth() - 1) && (z == 0 || z == getLength() - 1)) {
						data = Material.OAK_FENCE.createBlockData();
					}
					else if (y >= 2 && y < getHeight() - 1 && (x > 1 || x < getWidth() - 2) && (z == 0 || z == getLength() - 1)) {
						data = Material.GLASS.createBlockData();
					}
					else if (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1 ) {
						data = Material.COBBLESTONE.createBlockData();
					}
					
					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					if (data != null) {
						place.getBlock().setBlockData(data);
					}
				}
			}
		}
		
		//Recruiter
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength()/2);
		Recruiter recruiter = new Recruiter(place, entityHandler);
		entityHandler.addSavedEntity(recruiter);
		recruiter.gainXP(random.nextInt(15));
		new KingdomPoint(place).addVillager(recruiter);
		
		//Crafting table
		place.setX(loc.getX() + getWidth() - 2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength() - 2);
		place.getBlock().setType(Material.CRAFTING_TABLE);
		//Furnace
		place.add(0, 0, -1);
		place.getBlock().setType(Material.FURNACE);
		//Chest
		place.add(0, 0, -1);
		place.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) place.getBlock().getState();
		loot.fillInventory(chest.getBlockInventory(), random, null);
		
		//Chest
		place.add(0, 0, -1);
		place.getBlock().setType(Material.ANVIL);
		
		//Door
		place.setX(loc.getX());
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength()/2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.EAST, false, true);
		return Arrays.asList(new StructureInstance<RecruiterHouse>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "recruiter_house";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<RecruiterHouse>(this, location, getWidth(), getHeight(),getLength());
	}
}
