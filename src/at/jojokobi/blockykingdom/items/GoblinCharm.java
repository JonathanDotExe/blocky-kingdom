package at.jojokobi.blockykingdom.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class GoblinCharm extends CustomItem{
	
	public static final String NAME = "Goblin Charm";
	public static final String IDENTIFIER = "goblin_charm";
	public static final short META = 8;
	public static final Material ITEM = Material.GHAST_TEAR;
	
	private Plugin plugin;

	public GoblinCharm(Plugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
		setHelmet(true);
		
		//Leather Recipe
		this.plugin = plugin;
//		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		
	}
	
	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("LLL", "S S", "FFF");
		recipe.setIngredient('L', GoblinSkin.ITEM);
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('F', GoblinFang.ITEM);
		return recipe;
	}

}
