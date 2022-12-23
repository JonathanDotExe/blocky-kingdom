package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Farmer;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.FireWand;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.ProtectingFigure;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class FamilyHouse extends Structure {

	private EntityHandler entityHandler;
	
	private LootInventory loot;
	private Building building;
	
	public FamilyHouse(EntityHandler entityHandler) {
		super(10, 10, 10, 0);
		this.entityHandler = entityHandler;
		building = Building.loadBuilding(getClass().getResourceAsStream("/buildings/family_house1.yml"));
		
		loot = new LootInventory();
		
		loot = new LootInventory();
		
		loot.addItem(new LootItem(1, new ItemStack(Material.WHEAT), 1, 16));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 4));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 12));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 5));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.IRON_SWORD), 1, 1));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.ARROW), 1, 10));	
		loot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(1, new ItemStack(Material.WHEAT_SEEDS), 1, 16));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.DARK_OAK_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.OAK_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ACACIA_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.SPRUCE_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.JUNGLE_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.BIRCH_SAPLING), 1, 4));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.QUARTZ), 1, 32));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.EMERALD), 1, 2));
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 5));
		
		loot.addItem(new LootItem(0.3, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DoubleBow.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, FireWand.IDENTIFIER), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, ProtectingFigure.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, GrapplingHook.IDENTIFIER), 1, 1));
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 2, 10));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		
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
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		//Walls
		building.build(loc, (place, mark) -> {
			System.out.println(mark);
			switch (mark) {
			case "farmer_villager":
			{
				Farmer farmer = new Farmer(place, entityHandler, random);
				entityHandler.addSavedEntity(farmer);
				farmer.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(farmer);
			}
			break;
			case "knight_villager":
			{
				Trader shopKeeper = new Trader(place, entityHandler, random);
				entityHandler.addSavedEntity(shopKeeper);
				shopKeeper.gainXP(random.nextInt(20));
				new KingdomPoint(loc).addVillager(shopKeeper);
			}
			break;
			case "chest":
			{
				Chest chest = (Chest) place.getBlock().getState();
				loot.fillInventory(chest.getBlockInventory(), random, null);
			}
			break;
			case "armor_stand":
			{
				place.getWorld().spawn(place, ArmorStand.class);
				//TODO put armor on
			}
			break;
			case "village_dog":
			{
				//TODO
			}
			break;
			}
		}, random.nextInt(3), false);
		return Arrays.asList(new StructureInstance<FamilyHouse>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "family_house";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<FamilyHouse>(this, location, getWidth(), getHeight(),getLength());
	}
}
