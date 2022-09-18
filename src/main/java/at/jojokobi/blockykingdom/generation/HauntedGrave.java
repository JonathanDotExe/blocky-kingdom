package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.CursedFigure;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class HauntedGrave extends Structure{
	
	public static final String IDENTIFIER = "haunted_grave";
	
	private LootInventory loot = new LootInventory();
	private CursedFigure cursedFigure;

	public HauntedGrave(CursedFigure cursedFigure) {
		super(9, 9, 6, 800);
		
		this.cursedFigure = cursedFigure;
		
		loot.addItem(new LootItem(1, new ItemStack(Material.COAL), 1, 10));
		loot.addItem(new LootItem(1, new ItemStack(Material.ROTTEN_FLESH), 5, 10));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.DIAMOND), 1, 2));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 12));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 12));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 2));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.BOW), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.STONE_PICKAXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(1, new ItemStack(Material.BONE), 1, 5));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.STRING), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.ARROW), 1, 16));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.GOLDEN_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.EMERALD), 1, 3));
		
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, GrapplingHook.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.7, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 15));
		
		setxModifier(4122);
		setzModifier(1564);
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return super.canGenerate(chunk, seed) && chunk.getWorld().getEnvironment() == Environment.NORMAL;
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 2;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.STONE_BRICKS));
		return super.generateNaturally(place, seed);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		//Structure
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					Material type = Material.AIR;
					if (y < 3) {
						if (y > 0 && x >= getWidth()/2 - 1 && x <= getWidth()/2 + 1 && z >= 1 && z <= 3) {
							type = Material.OBSIDIAN;
						}
						else {
							type = Material.STONE_BRICKS;
						}
					}
					else if (x >= getWidth()/2 - 1 && x <= getWidth()/2 + 1 && z == 0) {
						type = Material.STONE_BRICKS;
					}
					
					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					
					place.getBlock().setType(type, false);
				}
			}
		}
		
		//Chest
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 2);
		place.setZ(loc.getZ() + 2);
		
		Random random = new Random(TerrainGenUtil.generateValueBasedSeed(seed, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		
		place.getBlock().setType(Material.CHEST, false);
		InventoryHolder chest = (InventoryHolder) place.getBlock().getState();
		loot.fillInventory(chest.getInventory(), random, null);
		
		//Cursed Figure
		place.add(0, 1, 0);
		cursedFigure.getItemEntity(place, new Vector());
		
		return Arrays.asList(new StructureInstance<HauntedGrave>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<HauntedGrave>(this, location, getWidth(), getHeight(),getLength());
	}

}
