package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class Claws extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Claws";
	public static final String IDENTIFIER = "claws";
	public static final short META = 7;
	public static final Material ITEM = Material.IRON_SWORD;

	public Claws(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(5);
		setSpeed(1.3);
		setMaxDurability(251);
		setRepairMaterial(Material.IRON_BLOCK);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem fang = ItemHandler.getCustomItem(GoblinFang.class);
		if (isItem(inv.getResult()) && !(fang.isItem(inv.getMatrix()[0]) && fang.isItem(inv.getMatrix()[1])
				&& fang.isItem(inv.getMatrix()[3]))) {
			inv.setResult(null);
		}
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("FF ", "FBI", " IO");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('B', Material.IRON_BLOCK);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('F', GoblinFang.ITEM);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		return false;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return true;
	}

}
