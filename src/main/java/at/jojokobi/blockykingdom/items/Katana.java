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

public class Katana extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Katana";
	public static final String IDENTIFIER = "katana";
	public static final short META = 1;
	public static final Material ITEM = Material.IRON_SWORD;

	public Katana(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(3);
		setSpeed(1.4);
		setMaxDurability(251);
		setRepairMaterial(Material.IRON_INGOT);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("  I", "NI ", "SN ");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('N', Material.IRON_NUGGET);
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
