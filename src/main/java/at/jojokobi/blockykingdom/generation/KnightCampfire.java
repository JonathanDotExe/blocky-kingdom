package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Knight;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.Money;
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

public class KnightCampfire extends Structure{

	private EntityHandler entityHandler;
	
	private Building building;
	private LootInventory loot;
	
	public KnightCampfire(EntityHandler entityHandler) {
		super(5, 5, 2, 0);
		this.entityHandler = entityHandler;
		building = Building.loadBuilding(getClass().getResourceAsStream("/buildings/knight_campfire.yml"));
		
		loot = new LootInventory();
		
		loot.addItem(new LootItem(1, new ItemStack(Material.COAL), 1, 8));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_INGOT), 1, 4));
		loot.addItem(new LootItem(0.8, new ItemStack(Material.IRON_INGOT), 1, 8));
		loot.addItem(new LootItem(1, new ItemStack(Material.BREAD), 1, 10));
		loot.addItem(new LootItem(1, new ItemStack(Material.APPLE), 1, 3));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.BOW), 1, 1));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.STONE_SWORD), 1, 1));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_SWORD), 1, 1));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.COOKED_BEEF), 1, 3));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.TRIDENT), 1, 1));
		
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Smasher.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Katana.IDENTIFIER), 1, 1));
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 2));
		
		setxModifier(84984);
		setzModifier(-9292);
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, 0, 0), getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		/*for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				place.setX(loc.getX() + x);
				place.setZ(loc.getZ() + z);
				place.getBlock().setType(Material.COBBLESTONE);
			}
		}
		
		place.add(0, 1, 0);
		
		//Campfire
		place.setX(loc.getX() + getWidth()/2);
		place.setZ(loc.getZ() + getLength()/2);
		place.getBlock().setType(Material.CAMPFIRE);
		
		//Crafting table
		place.setX(loc.getX());
		place.setZ(loc.getZ() + getLength()/2);
		place.getBlock().setType(Material.CRAFTING_TABLE);

		//Chest
		place.setX(loc.getX() + getWidth()/2);
		place.setZ(loc.getZ() + getLength() - 1);
		place.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) place.getBlock().getState();
		loot.fillInventory(chest.getBlockInventory(), random, null);
		
		//Seats
		place.setX(loc.getX() + getWidth() - 1);
		place.setZ(loc.getZ() + getLength()/2);
		place.getBlock().setType(Material.OAK_SLAB);
		
		place.setX(loc.getX() + getWidth()/2);
		place.setZ(loc.getZ());
		place.getBlock().setType(Material.OAK_SLAB);

		//Knight
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 2);
		place.setZ(loc.getZ());
		Knight knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		knight.gainXP(random.nextInt(25));
		new KingdomPoint(loc).addVillager(knight);*/
		
		building.build(loc, (place, mark) -> {
			switch (mark) {
			case "knight_villager":
			{
				place.setX(loc.getX() + getWidth()/2);
				place.setY(loc.getY() + 2);
				place.setZ(loc.getZ());
				Knight knight = new Knight(place, entityHandler, random);
				entityHandler.addSavedEntity(knight);
				knight.gainXP(random.nextInt(25));
				new KingdomPoint(loc).addVillager(knight);
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
		}, random.nextInt(4), false);
		
		return Arrays.asList(new StructureInstance<KnightCampfire>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "knight_campfire";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<KnightCampfire>(this, location, getWidth(), getHeight(),getLength());
	}
}
