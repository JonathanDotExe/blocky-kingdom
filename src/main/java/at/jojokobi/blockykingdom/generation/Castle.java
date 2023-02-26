package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Chest;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Archer;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Farmer;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Knight;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class Castle extends Structure{
	
	private Building castleBuilding;
	private List<Building> leftBuildings;
	private List<Building> rightBuildings;
	private EntityHandler entityHandler;
	private DimensionHandler dimHandler;
	
	private LootInventory loot;
	private LootInventory barnLoot;
	private LootInventory shopLoot;
	private LootInventory evilLoot;
	private LootInventory archerLoot;
	private LootInventory weaponLoot;
	
	public Castle(EntityHandler entityHandler, DimensionHandler dimHandler) {
		super(32, 32, 32, 0);
		this.entityHandler = entityHandler;
		this.dimHandler = dimHandler;
		this.castleBuilding = Building.loadBuilding(getClass().getResourceAsStream("/buildings/castle.yml"));
		this.leftBuildings = Arrays.asList(Building.loadBuilding(getClass().getResourceAsStream("/buildings/castle_food_shop.yml")));
		this.rightBuildings = Arrays.asList(Building.loadBuilding(getClass().getResourceAsStream("/buildings/castle_weapon_chamber.yml")), Building.loadBuilding(getClass().getResourceAsStream("/buildings/castle_horse_barn.yml")));

		setxModifier(-651);
		setzModifier(81132);
		
		loot = new LootInventory();
		loot.addItem(new LootItem(0.2, new ItemStack(Material.FEATHER), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.FLINT), 1, 4));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.OAK_PLANKS), 1, 16));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.APPLE), 1, 5));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.STRING), 1, 7));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.COBBLESTONE), 1, 20));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.GOLD_INGOT), 1, 6));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_SWORD), 1, 1));
		loot.addItem(new LootItem(0.4, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 1));
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		barnLoot = new LootInventory();
		barnLoot.addItem(new LootItem(0.2, new ItemStack(Material.COAL), 1, 8));
		barnLoot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		barnLoot.addItem(new LootItem(0.1, new ItemStack(Material.GOLD_INGOT), 1, 3));
		barnLoot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_INGOT), 1, 5));
		barnLoot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 7));
		barnLoot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		barnLoot.addItem(new LootItem(0.2, new ItemStack(Material.STONE_SWORD), 1, 1));
		barnLoot.addItem(new LootItem(0.4, new ItemStack(Material.STONE_HOE), 1, 1));
		barnLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_HOE), 1, 1));
		barnLoot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		barnLoot.addItem(new LootItem(1, new ItemStack(Material.WHEAT_SEEDS), 1, 16));
		barnLoot.addItem(new LootItem(0.4, new ItemStack(Material.CARROT), 1, 4));
		barnLoot.addItem(new LootItem(0.4, new ItemStack(Material.HAY_BLOCK), 1, 4));	
		barnLoot.addItem(new LootItem(0.1, new ItemStack(Material.PUMPKIN), 1, 2));
		barnLoot.addItem(new LootItem(0.05, new ItemStack(Material.MELON), 1, 2));
		barnLoot.addItem(new LootItem(0.1, new ItemStack(Material.POTATO), 1, 2));
		barnLoot.addItem(new LootItem(0.05, new ItemStack(Material.BEETROOT_SEEDS), 1, 4));	
		barnLoot.addItem(new LootItem(0.5, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		shopLoot = new LootInventory();
		shopLoot.addItem(new LootItem(1, new ItemStack(Material.COAL), 1, 16));
		shopLoot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		shopLoot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 4));
		shopLoot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 8));
		shopLoot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		shopLoot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		shopLoot.addItem(new LootItem(0.1, new ItemStack(Material.BOW), 1, 1));
		shopLoot.addItem(new LootItem(0.5, new ItemStack(Material.STONE_SWORD), 1, 1));
		shopLoot.addItem(new LootItem(0.7, new ItemStack(Material.ARROW), 1, 10));	
		shopLoot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		shopLoot.addItem(new LootItem(1, new ItemStack(Material.WHEAT_SEEDS), 1, 16));
		shopLoot.addItem(new LootItem(0.2, new ItemStack(Material.CARROT), 1, 2));
		shopLoot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1, 1));
		shopLoot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1));
		shopLoot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1));
		shopLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1));
		shopLoot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		weaponLoot = new LootInventory();
		weaponLoot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 2));
		weaponLoot.addItem(new LootItem(0.05, new ItemStack(Material.EMERALD), 1, 2));
		weaponLoot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 6));
		weaponLoot.addItem(new LootItem(0.05, new ItemStack(Material.IRON_BLOCK), 1, 3));
		weaponLoot.addItem(new LootItem(0.4, new ItemStack(Material.IRON_BARS), 1, 16));
		weaponLoot.addItem(new LootItem(0.05, new ItemStack(Material.DAMAGED_ANVIL), 1, 1));
		weaponLoot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 10));
		weaponLoot.addItem(new LootItem(0.05, new ItemStack(Material.BOW), 1, 1).setEnchant(true));
		weaponLoot.addItem(new LootItem(0.5, new ItemStack(Material.STONE_SWORD), 1, 1).setEnchant(true));
		weaponLoot.addItem(new LootItem(0.1, new ItemStack(Material.IRON_SWORD), 1, 1).setEnchant(true));
		weaponLoot.addItem(new LootItem(0.7, new ItemStack(Material.ARROW), 1, 10));
		weaponLoot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Katana.IDENTIFIER), 1, 1));
		weaponLoot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1, 1));
		weaponLoot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1));
		weaponLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1));
		weaponLoot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1));
		weaponLoot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		evilLoot = new LootInventory();
		evilLoot.addItem(new LootItem(0.2, new ItemStack(Material.FEATHER), 1, 4));
		evilLoot.addItem(new LootItem(0.1, new ItemStack(Material.FLINT), 1, 4));
		evilLoot.addItem(new LootItem(0.5, new ItemStack(Material.OAK_PLANKS), 1, 16));
		evilLoot.addItem(new LootItem(0.4, new ItemStack(Material.BREAD), 1, 5));
		evilLoot.addItem(new LootItem(0.3, new ItemStack(Material.APPLE), 1, 5));
		evilLoot.addItem(new LootItem(0.2, new ItemStack(Material.STRING), 1, 7));
		evilLoot.addItem(new LootItem(0.7, new ItemStack(Material.COBBLESTONE), 1, 20));
		evilLoot.addItem(new LootItem(0.6, new ItemStack(Material.IRON_INGOT), 1, 12));
		evilLoot.addItem(new LootItem(0.4, new ItemStack(Material.GOLD_INGOT), 1, 10));
		evilLoot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_SWORD), 1, 1));
		evilLoot.addItem(new LootItem(0.4, new ItemStack(Material.STONE_SWORD), 1, 1));
		evilLoot.addItem(new LootItem(0.1, new ItemStack(Material.DIAMOND), 1, 2));
		evilLoot.addItem(new LootItem(0.1, new ItemStack(Material.OBSIDIAN), 1, 3));
		evilLoot.addItem(new LootItem(0.1, new ItemStack(Material.DIAMOND), 1, 2));
		evilLoot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_SWORD), 1, 1).setDamage(true).setEnchant(true));
		evilLoot.addItem(new LootItem(0.2, new ItemStack(Material.IRON_SWORD), 1, 1).setDamage(true).setEnchant(true));
		evilLoot.addItem(new LootItem(0.05, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		evilLoot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Katana.IDENTIFIER), 1, 1).setEnchant(true));		
		evilLoot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		archerLoot = new LootInventory();
		archerLoot.addItem(new LootItem(1, new ItemStack(Material.ARROW), 1, 16));
		archerLoot.addItem(new LootItem(0.6, new ItemStack(Material.FEATHER), 1, 4));
		archerLoot.addItem(new LootItem(0.2, new ItemStack(Material.FLINT), 1, 4));
		archerLoot.addItem(new LootItem(0.5, new ItemStack(Material.OAK_PLANKS), 1, 16));
		archerLoot.addItem(new LootItem(0.4, new ItemStack(Material.BREAD), 1, 3));
		archerLoot.addItem(new LootItem(0.6, new ItemStack(Material.APPLE), 1, 5));
		archerLoot.addItem(new LootItem(0.6, new ItemStack(Material.STRING), 1, 7));
		archerLoot.addItem(new LootItem(0.1, new ItemStack(Material.BOW), 1, 1));
		archerLoot.addItem(new LootItem(0.1, new ItemStack(Material.COBWEB), 1, 7));
		archerLoot.addItem(new LootItem(0.05, new ItemStack(Material.EMERALD), 1, 2));
		archerLoot.addItem(new LootItem(0.3, new ItemStack(Material.GOLD_INGOT), 1, 7));
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.STONE_BRICKS));
		return super.generateNaturally(place, seed);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		List<StructureInstance<? extends Structure>> structures = new ArrayList<>();		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		BiConsumer<Location, String> markInterpreter = (place, mark) -> {
			switch (mark) {
			case "bridge_chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				if (KingdomHandler.getInstance().generateKingdom(new KingdomPoint(loc)).getState() == KingdomState.EVIL) {
					evilLoot.fillInventory(chest.getBlockInventory(), random, null);
				}
				else {
					loot.fillInventory(chest.getBlockInventory(), random, null);
				}
			}
				break;
			case "archer_chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				archerLoot.fillInventory(chest.getBlockInventory(), random, null);
			}
				break;
			case "knight_villager":
			{
				Knight knight = new Knight(place, entityHandler, random);
				entityHandler.addSavedEntity(knight);
				knight.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(knight);
			}
				break;
			case "archer_villager":
			{
				Archer knight = new Archer(place, entityHandler, random);
				entityHandler.addSavedEntity(knight);
				knight.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(knight);
			}
				break;
			case "farmer_villager":
			{
				Farmer knight = new Farmer(place, entityHandler, random);
				entityHandler.addSavedEntity(knight);
				knight.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(knight);
			}
				break;
			case "barn_chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				barnLoot.fillInventory(chest.getBlockInventory(), random, null);
			}
				break;
			case "horse":
			{
				Horse horse = place.getWorld().spawn(place, Horse.class);
				horse.setAdult();
				horse.setTamed(true);
				horse.setStyle(Horse.Style.values()[random.nextInt(Horse.Style.values().length)]);
				horse.setColor(Horse.Color.values()[random.nextInt(Horse.Color.values().length)]);
			}
				break;
			case "shop_chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				shopLoot.fillInventory(chest.getBlockInventory(), random, null);
			}
				break;
			case "trader_villager":
			{
				Trader knight = new Trader(place, entityHandler, random);
				entityHandler.addSavedEntity(knight);
				knight.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(knight);
			}
				break;
			case "weapon_chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				weaponLoot.fillInventory(chest.getBlockInventory(), random, null);
			}
				break;
			}
		};
		castleBuilding.build(loc, markInterpreter, 0, false);
		
		//Add side structures
		rightBuildings.get(random.nextInt(rightBuildings.size())).build(loc.clone().add(1, 0, 9), markInterpreter, 0, false);
		leftBuildings.get(random.nextInt(leftBuildings.size())).build(loc.clone().add(24, 0, 9), markInterpreter, 0, false);
		
		structures.add(new StructureInstance<Castle>(this, loc.clone(), getWidth(), getHeight(), getLength()));
				
		return structures;
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		KingdomPoint point = new KingdomPoint(chunk.getBlock(0, 0, 0).getLocation());
		Kingdom kingdom = KingdomHandler.getInstance().generateKingdom(point);
		Location centerLoc = point.toLocation().add(kingdom.getCenterX(), 0, kingdom.getCenterZ());
		return chunk.getWorld().getEnvironment() == Environment.NORMAL && dimHandler.getDimension(chunk.getWorld()) == null && kingdom.getState() != KingdomState.UNCLAIMED && centerLoc.getBlockX()/TerrainGenUtil.CHUNK_WIDTH == chunk.getX() && centerLoc.getBlockZ()/TerrainGenUtil.CHUNK_LENGTH == chunk.getZ();
	}

	@Override
	public String getIdentifier() {
		return "castle";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Castle>(this, location, getWidth(), getHeight(), getLength());
	}


}
