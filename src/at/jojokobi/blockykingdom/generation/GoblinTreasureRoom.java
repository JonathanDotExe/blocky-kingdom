package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class GoblinTreasureRoom extends Structure{
	
	private LootInventory loot;

	public GoblinTreasureRoom() {
		super(6, 6, 6, 0, 1);
		loot = new LootInventory();
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 2));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.WHEAT), 1, 5));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.BREAD), 1, 2));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.ROTTEN_FLESH), 1, 2));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_CHESTPLATE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_HELMET), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_BOOTS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_LEGGINGS), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.GOLDEN_SWORD), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.GOLDEN_AXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.GOLDEN_PICKAXE), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.NAME_TAG), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.MUSIC_DISC_11), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.MUSIC_DISC_13), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.MUSIC_DISC_BLOCKS), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.MUSIC_DISC_CAT), 1, 1));
		loot.addItem(new LootItem(0.1, new ItemStack(Material.GOLDEN_APPLE), 1, 1));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 1, 1));
		loot.addItem(new LootItem(0.15, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.15, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.15, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.15, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		loot.addItem(new LootItem(0.15, new ItemStack(Material.ENCHANTED_BOOK), 1, 1).setEnchant(true));
		
		loot.addItem(new LootItem(0.3, new ItemStack(Material.IRON_INGOT), 1, 5));
		loot.addItem(new LootItem(0.7, new ItemStack(Material.GOLD_INGOT), 5, 12));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.GOLD_NUGGET), 1, 5));
		loot.addItem(new LootItem(0.05, new ItemStack(Material.DIAMOND), 1, 3));
		loot.addItem(new LootItem(0.5, new ItemStack(Material.REDSTONE), 1, 16));
		loot.addItem(new LootItem(0.3, new ItemStack(Material.LAPIS_LAZULI), 1, 5));
		
		loot.addItem(new LootItem(0.3, new ItemStack(Material.RAIL), 1, 16));
		loot.addItem(new LootItem(0.2, new ItemStack(Material.RAIL), 1, 16));
		
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(Katana.class), 1, 1));
		loot.addItem(new LootItem(0.2, ItemHandler.getItemStack(Hammer.class), 1, 1));
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		BasicGenUtil.generateCube(loc, Material.AIR, getWidth(), getHeight(), getLength());
		loc.getBlock().setType(Material.CHEST);
		loot.fillInventory(((Chest) loc.getBlock().getState()).getBlockInventory(), new Random(generateValueBeasedSeed(loc, seed)), null);
		return Arrays.asList(getStandardInstance(loc));
	}

	@Override
	public String getIdentifier() {
		return "goblin_treasure_room";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Structure>(this, location, getWidth(), getHeight(), getLength());
	}
	
}
