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

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.ZombieBoss;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class DungeonTower extends Structure{
	
	private static final int FLOOR_COUNT = 5;
	private static final int STAGE_HEIGHT = 10;
	
	private static final Material[] FLOOR_MATERIALS = {Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.AIR};
	private static final Material[] WALL_MATERIALS = {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.INFESTED_STONE_BRICKS};
	private static final EntityType[] SPAWNER_TYPES = {EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER, EntityType.SKELETON, EntityType.CAVE_SPIDER};
	
	
	private LootInventory loot;
	
	private EntityHandler handler;
	
	public DungeonTower(EntityHandler handler) {
		super(16, 16, STAGE_HEIGHT * FLOOR_COUNT, 600);
		this.handler = handler;
		loot = new LootInventory();
		
		loot.addItem(new LootItem(0.5, new ItemStack(Material.COAL), 1, 16));
		loot.addItem(new LootItem(1, new ItemStack(Material.ROTTEN_FLESH), 1, 5));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.EMERALD), 1, 3));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.GOLD_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENDER_PEARL), 1, 16));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BLAZE_ROD), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BOW), 1, 1));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.BONE), 1, 3));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.SLIME_BALL), 1, 2));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.ARROW), 1, 10));	
		loot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.GOLDEN_HORSE_ARMOR), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.4, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 3));
		
		setxModifier(2651);
		setzModifier(-64);
	}

	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return super.canGenerate(chunk, seed) && chunk.getWorld().getEnvironment() == Environment.NORMAL && chunk.getWorld().getHighestBlockYAt(chunk.getBlock(0, 0, 0).getLocation()) > 0;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		Random random = new Random(TerrainGenUtil.generateValueBasedSeed(seed, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		
		for (int floor = 0; floor < FLOOR_COUNT; floor++) {
			for (int y = 0; y < STAGE_HEIGHT; y++) {
				for (int x = 0; x < getWidth(); x++) {
					for (int z = 0; z < getLength(); z++) {
						place.setX(loc.getX() + x);
						place.setY(loc.getY()+ STAGE_HEIGHT * floor + y);
						place.setZ(loc.getZ() + z);
						//Wall
						Material block = Material.AIR;
						if (floor < FLOOR_COUNT - 1) {
							if (y == 0) {
								block = FLOOR_MATERIALS [random.nextInt(FLOOR_MATERIALS.length)];
							}
							else if (x == 0 || z == 0 || x == getWidth() - 1 || z == getLength() - 1) {
								block = WALL_MATERIALS [random.nextInt(WALL_MATERIALS.length)];
							}
						}
						else if (y == 0) {
							block = Material.INFESTED_STONE_BRICKS;
						}

						place.getBlock().setType(block);
					}
				}
			}
			
			//Chest
			place.setX(loc.getX() + 1);
			place.setY(loc.getY()+ STAGE_HEIGHT * floor + 1);
			place.setZ(loc.getZ() + 1);
			
			place.getBlock().setType(Material.CHEST);
			Chest chest = (Chest) place.getBlock().getState();
			loot.fillInventory(chest.getBlockInventory(), random, null);
			if (floor >= 2) {
				loot.fillInventory(chest.getBlockInventory(), random, null);
			}
			
			place.setX(loc.getX() + getWidth()/2);
			place.setY(loc.getY()+ STAGE_HEIGHT * floor + 1);
			place.setZ(loc.getZ() + getLength()/2);
			
			//Spawner
			if (floor != FLOOR_COUNT - 1) {
				place.getBlock().setType(Material.SPAWNER);
				CreatureSpawner spawner = (CreatureSpawner) place.getBlock().getState();
				spawner.setSpawnedType(SPAWNER_TYPES[random.nextInt(SPAWNER_TYPES.length)]);
				spawner.update();
			}
			else {
				place.add(0, 3, 0);
				ZombieBoss boss = new ZombieBoss(place, handler);
				boss.setSave(true);
				handler.addEntity(boss);
			}
			
		}
		
		return Arrays.asList(new StructureInstance<DungeonTower>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "dungeon_tower";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<DungeonTower>(this, location, getWidth(), getHeight(),getLength());
	}


}
