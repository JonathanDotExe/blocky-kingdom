package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Recruiter;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.kingdoms.KingdomChestLockHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class RecruiterHouse extends Structure{

	private EntityHandler entityHandler;
	
	private LootInventory loot;
	private Building building;
	private KingdomChestLockHandler lockHandler;
	
	public RecruiterHouse(EntityHandler entityHandler) {
		super(10, 10, 6, 0);
		this.entityHandler = entityHandler;
		building = Building.loadBuilding(getClass().getResourceAsStream("/buildings/recruiter_house.yml"));
		
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
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		//Location place = loc.clone();
		BasicGenUtil.generateCube(loc, Material.AIR, getWidth(), getHeight(), getLength());
		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		building.build(loc, (place, mark) -> {
			switch (mark) {
			case "recruiter":
			{
				Recruiter recruiter = new Recruiter(place, entityHandler);
				entityHandler.addSavedEntity(recruiter);
				recruiter.gainXP(random.nextInt(15));
				new KingdomPoint(place).addVillager(recruiter);
			}
			break;
			case "chest":
			{
				place.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) place.getBlock().getState();
				loot.fillInventory(chest.getBlockInventory(), random, null);
			}
			break;
			}
		}, random.nextInt(3), false);
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
