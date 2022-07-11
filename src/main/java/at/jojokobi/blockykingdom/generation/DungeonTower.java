package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
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
	
	private LootInventory loot;
	
	private EntityHandler handler;
	
	public DungeonTower(EntityHandler handler) {
		super(16, 16, STAGE_HEIGHT * FLOOR_COUNT, 700);
		this.handler = handler;
		loot = new LootInventory();
		
		loot.addItem(new LootItem(0.5, new ItemStack(Material.COAL), 1, 16));
		loot.addItem(new LootItem(1, new ItemStack(Material.ROTTEN_FLESH), 1, 5));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.GOLD_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENDER_PEARL), 1, 16));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BOW), 1, 1));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.BONE), 1, 3));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.ARROW), 1, 10));	
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
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place, HeightMap.OCEAN_FLOOR) - 1;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		Material fillMaterial;
		if (Arrays.asList(Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.BADLANDS, Biome.DESERT, Biome.SAVANNA, Biome.BASALT_DELTAS).contains(place.getBlock().getBiome())) {
			fillMaterial = Material.SAND;
		}
		//Ocean
		else if (Arrays.asList(Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.OCEAN, Biome.COLD_OCEAN).contains(place.getBlock().getBiome())) {
			fillMaterial = Material.SAND;
		}
		//Snow
		else if (Arrays.asList(Biome.SNOWY_TAIGA, Biome.SNOWY_BEACH, Biome.SNOWY_PLAINS, Biome.SNOWY_SLOPES, Biome.ICE_SPIKES).contains(place.getBlock().getBiome())) {
			fillMaterial = Material.SNOW_BLOCK;
		}
		//Forest
		else if (Arrays.asList(Biome.JUNGLE, Biome.BAMBOO_JUNGLE, Biome.SPARSE_JUNGLE, Biome.DARK_FOREST).contains(place.getBlock().getBiome())) {
			fillMaterial = Material.DIRT;
		}
		else {
			fillMaterial = Material.STONE_BRICKS;
		}
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(fillMaterial));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		Random random = new Random(TerrainGenUtil.generateValueBasedSeed(seed, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		Material[] floorMaterials = {Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.AIR};
		Material[] wallMaterials = {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.INFESTED_STONE_BRICKS};
		EntityType[] spawnerTypes = {EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER, EntityType.SKELETON, EntityType.CAVE_SPIDER};
		LootInventory specialLoot = new LootInventory();
		//Custom blocks
		//Desert
		if (Arrays.asList(Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.BADLANDS, Biome.DESERT, Biome.SAVANNA, Biome.BASALT_DELTAS).contains(place.getBlock().getBiome())) {
			floorMaterials = new Material[]{Material.SANDSTONE, Material.SANDSTONE, Material.SMOOTH_SANDSTONE, Material.SAND, Material.SAND, Material.SAND, Material.AIR};
			wallMaterials = new Material[]{Material.SANDSTONE, Material.SANDSTONE, Material.SAND, Material.RED_SANDSTONE};
			spawnerTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.HUSK, EntityType.HUSK, EntityType.SPIDER, EntityType.SKELETON, EntityType.CAVE_SPIDER, EntityType.CREEPER};
			specialLoot.addItem(new LootItem(0.25, new ItemStack(Material.BONE_BLOCK), 1, 4));
			specialLoot.addItem(new LootItem(0.2, new ItemStack(Material.LAPIS_LAZULI), 1, 8));
			specialLoot.addItem(new LootItem(0.4, new ItemStack(Material.COBWEB), 1, 8));
		}
		//Ocean
		else if (Arrays.asList(Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.OCEAN, Biome.COLD_OCEAN).contains(place.getBlock().getBiome())) {
			floorMaterials = new Material[]{Material.DARK_PRISMARINE};
			wallMaterials = new Material[]{Material.PRISMARINE_BRICKS};
			spawnerTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.DROWNED, EntityType.SPIDER, EntityType.SKELETON, EntityType.GUARDIAN, EntityType.GUARDIAN};
			specialLoot.addItem(new LootItem(0.5, new ItemStack(Material.PRISMARINE_SHARD), 4, 16));
			specialLoot.addItem(new LootItem(0.5, new ItemStack(Material.PRISMARINE_CRYSTALS), 1, 8));
			specialLoot.addItem(new LootItem(0.2, new ItemStack(Material.SEA_LANTERN), 1, 1));
		}
		//Snow
		else if (Arrays.asList(Biome.SNOWY_TAIGA, Biome.SNOWY_BEACH, Biome.SNOWY_PLAINS, Biome.SNOWY_SLOPES, Biome.ICE_SPIKES).contains(place.getBlock().getBiome())) {
			floorMaterials = new Material[]{Material.PACKED_ICE, Material.PACKED_ICE, Material.PACKED_ICE, Material.AIR};
			wallMaterials = new Material[]{Material.SNOW_BLOCK, Material.SNOW_BLOCK, Material.SNOW_BLOCK, Material.PACKED_ICE};
			spawnerTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.DROWNED, EntityType.SPIDER, EntityType.SKELETON, EntityType.GUARDIAN, EntityType.GUARDIAN};
			specialLoot.addItem(new LootItem(0.5, new ItemStack(Material.BLUE_ICE), 4, 16));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.CHAINMAIL_BOOTS), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.CHAINMAIL_CHESTPLATE), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.CHAINMAIL_HELMET), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.CHAINMAIL_LEGGINGS), 1, 1).setEnchant(true));
		}
		//Forest
		else if (Arrays.asList(Biome.JUNGLE, Biome.BAMBOO_JUNGLE, Biome.SPARSE_JUNGLE, Biome.DARK_FOREST).contains(place.getBlock().getBiome())) {
			floorMaterials = new Material[]{Material.DARK_OAK_WOOD};
			wallMaterials = new Material[]{Material.DARK_OAK_WOOD};
			spawnerTypes = new EntityType[]{EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER, EntityType.SKELETON, EntityType.ENDERMAN, EntityType.PILLAGER};
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_BOOTS), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_CHESTPLATE), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_HELMET), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_LEGGINGS), 1, 1).setEnchant(true));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_AXE), 1, 1).setEnchant(true));
		}
		else {
			specialLoot.addItem(new LootItem(0.2, new ItemStack(Material.SLIME_BALL), 1, 2));
			specialLoot.addItem(new LootItem(0.05, new ItemStack(Material.EMERALD), 1, 3));
			specialLoot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		}
		
		//Floors
		int floorCount = FLOOR_COUNT - 2 + random.nextInt(5);
		for (int floor = 0; floor < floorCount; floor++) {
			for (int y = 0; y < STAGE_HEIGHT; y++) {
				for (int x = 0; x < getWidth(); x++) {
					for (int z = 0; z < getLength(); z++) {
						place.setX(loc.getX() + x);
						place.setY(loc.getY()+ STAGE_HEIGHT * floor + y);
						place.setZ(loc.getZ() + z);
						//Wall
						Material block = Material.AIR;

						if (x == 0 || z == 0 || x == getWidth() - 1 || z == getLength() - 1) {
							block = wallMaterials [random.nextInt(wallMaterials.length)];
						}
						else if (y == 0) {
							if (floor < floorCount - 1) {
								block = floorMaterials [random.nextInt(floorMaterials.length)];
							}
							else {
								block = Material.INFESTED_STONE_BRICKS;
							}
						}

						place.getBlock().setType(block, false);
					}
				}
			}
			
			//Chest
			place.setX(loc.getX() + 1);
			place.setY(loc.getY()+ STAGE_HEIGHT * floor + 1);
			place.setZ(loc.getZ() + 1);
			
			place.getBlock().setType(Material.CHEST, false);
			Chest chest = (Chest) place.getBlock().getState();
			loot.fillInventory(chest.getBlockInventory(), random, null);
			if (floor >= 2) {
				loot.fillInventory(chest.getBlockInventory(), random, null);
			}
			if (floor >= floorCount - 3) {
				specialLoot.fillInventory(chest.getBlockInventory(), random, null);
			}
			
			place.setX(loc.getX() + getWidth()/2);
			place.setY(loc.getY()+ STAGE_HEIGHT * floor + 1);
			place.setZ(loc.getZ() + getLength()/2);
			
			//Spawner
			if (floor != floorCount - 1) {
				place.getBlock().setType(Material.SPAWNER);
				CreatureSpawner spawner = (CreatureSpawner) place.getBlock().getState();
				spawner.setSpawnedType(spawnerTypes[random.nextInt(spawnerTypes.length)]);
				spawner.update();
			}
			else {
				place.add(0, 3, 0);
				ZombieBoss boss = new ZombieBoss(place, handler);
				boss.setSave(true);
				handler.addEntity(boss);
			}
			
		}
		
		return Arrays.asList(new StructureInstance<DungeonTower>(this, loc, getWidth(), floorCount * STAGE_HEIGHT, getLength()));
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
