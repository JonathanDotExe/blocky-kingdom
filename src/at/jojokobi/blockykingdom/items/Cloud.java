package at.jojokobi.blockykingdom.items;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class Cloud extends CustomItem {
	
	public static final String NAME = ChatColor.AQUA + "Cloud";
	public static final String IDENTIFIER = "cloud";
	public static final short META = 3;
	public static final Material ITEM = Material.GHAST_TEAR;
	
	private Plugin plugin;

	public Cloud(Plugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}
	
	@EventHandler
	public void onPrepareItemCraft (PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem item = ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, CloudParticle.IDENTIFIER);
		if (isItem(inv.getResult()) && !Arrays.stream(inv.getMatrix()).allMatch(item::isItem)) {
			inv.setResult(null);
		}
	}

	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("CCC","CCC","CCC");
		recipe.setIngredient('C', CloudParticle.ITEM);
		return recipe;
	}

}
