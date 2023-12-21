package at.jojokobi.blockykingdom.items;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class GoblinSkin extends CustomItem{
	
	public static final String NAME = ChatColor.GREEN + "Goblin Skin";
	public static final String IDENTIFIER = "goblin_skin";
	public static final short META = 7;
	public static final Material ITEM = Material.GHAST_TEAR;
	
	private final NamespacedKey leatherRecipeKey;

	public GoblinSkin(Plugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
		
		leatherRecipeKey = new NamespacedKey(plugin, "leather");
		
		//Leather Recipe
		ShapelessRecipe recipe = new ShapelessRecipe(leatherRecipeKey, new ItemStack(Material.LEATHER));
		recipe.addIngredient(ITEM);
		recipe.addIngredient(ITEM);
		recipe.addIngredient(ITEM);
		recipe.addIngredient(ITEM);
		Bukkit.getScheduler().runTask(plugin, () -> Bukkit.addRecipe(recipe));
	}
	
	@EventHandler
	public void onPrepareItemCraft (PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		if (!(event.getRecipe() instanceof Keyed && ((Keyed) event.getRecipe()).getKey().equals(leatherRecipeKey) && Arrays.stream(inv.getContents()).allMatch(i -> i.getType() != ITEM || isItem(i)))) {
			inv.setResult(null);
		}
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
