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
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.FireWand;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.ProtectingFigure;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class TraderHut extends Structure{

	private EntityHandler entityHandler;
	
	private LootInventory loot;
	private LootInventory evilLoot;
	private DimensionHandler dimHandler;
	
	public TraderHut(EntityHandler entityHandler, DimensionHandler dimHandler) {
		super(8, 8, 5, 0);
		this.entityHandler = entityHandler;
		this.dimHandler = dimHandler;
		
		loot = new LootInventory();
		evilLoot = new LootInventory();
		
		loot.addItem(new LootItem(1, new ItemStack(Material.COAL), 1, 16));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 4));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.BOW), 1, 1));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.ARROW), 1, 10));	
		loot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(1, new ItemStack(Material.WHEAT_SEEDS), 1, 16));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.CARROT), 1, 2));
		
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1));
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		evilLoot.addItem(new LootItem(0.1, new ItemStack(Material.DIAMOND), 1, 3));
		evilLoot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 8));
		evilLoot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 8));
		evilLoot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		evilLoot.addItem(new LootItem(0.5, new ItemStack(Material.BOW), 1, 1));
		evilLoot.addItem(new LootItem(0.5, new ItemStack(Material.IRON_SWORD), 1, 1));
		evilLoot.addItem(new LootItem(0.8, new ItemStack(Material.ARROW), 6, 16));	
		evilLoot.addItem(new LootItem(0.3, new ItemStack(Material.NAME_TAG), 1, 1));
		evilLoot.addItem(new LootItem(0.3, new ItemStack(Material.CARROT), 2, 4));
		
		evilLoot.addItem(new LootItem(0.3, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1).setEnchant(true));
		evilLoot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1).setEnchant(true));
		evilLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1).setEnchant(true));
		evilLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, FireWand.IDENTIFIER), 1, 1).setEnchant(true));
		evilLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, ProtectingFigure.IDENTIFIER), 1, 1));
		evilLoot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, GrapplingHook.IDENTIFIER), 1, 1));
		
		evilLoot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 2, 10));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		
		setxModifier(-95498);
		setzModifier(54);
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		int spawnChunkX = chunk.getWorld().getSpawnLocation().getBlockX()/TerrainGenUtil.CHUNK_WIDTH;
		int spawnChunkZ = chunk.getWorld().getSpawnLocation().getBlockZ()/TerrainGenUtil.CHUNK_WIDTH;
		return (super.canGenerate(chunk, seed) || (chunk.getX() == spawnChunkX && chunk.getZ() == spawnChunkZ)) && dimHandler.getDimension(chunk.getWorld()) == null && chunk.getWorld().getEnvironment() == Environment.NORMAL;
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
					Material block = Material.AIR;
					if ((x == 0 || x == getWidth() - 1) && (z == 0 || z == getLength() - 1)) {
						block = Material.OAK_LOG;
					}
					else if (y >= 2 && y < getHeight() - 1 && x == getWidth() - 1 && z >= 4 &&  z < getLength() - 2 ) {
						block = Material.GLASS;
					}
					else if (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1 || y == 0 || y == getHeight() - 1) {
						block = Material.OAK_PLANKS;
					}
					
					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					place.getBlock().setType(block);
				}
			}
		}
		
		//Trader
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength()/2);
		Trader shopKeeper = new Trader(place, entityHandler, random);
		entityHandler.addSavedEntity(shopKeeper);
		shopKeeper.gainXP(random.nextInt(15));
		new KingdomPoint(loc).addVillager(shopKeeper);
		
		//Crafting table
		place.setX(loc.getX() + 1);
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
		Kingdom kingdom = KingdomHandler.getInstance().generateKingdom(new KingdomPoint(loc));
		if (kingdom != null && kingdom.getState() == KingdomState.EVIL) {
			evilLoot.fillInventory(chest.getBlockInventory(), random, null);
		}
		else {
			loot.fillInventory(chest.getBlockInventory(), random, null);
		}
		
		//Door
		place.setX(loc.getX() + getWidth() - 1);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + 2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.EAST, false, true);
		return Arrays.asList(new StructureInstance<TraderHut>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "trader_hut";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<TraderHut>(this, location, getWidth(), getHeight(),getLength());
	}
}
