package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class DiamondKatana extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Diamond Katana";
	public static final String IDENTIFIER = "diamond_katana";
	public static final short META = 5;
	public static final Material ITEM = Material.IRON_SWORD;

	public DiamondKatana(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(5);
		setSpeed(1.8);
		setMaxDurability(1562);
		setRepairMaterial(Material.DIAMOND);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("  D", "OD ", "SO ");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('S', Material.STICK);
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
