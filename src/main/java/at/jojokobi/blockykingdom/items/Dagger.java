package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class Dagger extends CustomTool{

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Dagger";
	public static final String IDENTIFIER = "dagger";
	public static final short META = 2;
	public static final Material ITEM = Material.IRON_SWORD;

	public Dagger(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(3);
		setSpeed(-2.8);
		setMaxDurability(182);
		setRepairMaterial(Material.IRON_INGOT);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public void onHit(ItemStack item, EntityDamageByEntityEvent event) {
		super.onHit(item, event);
		boolean moreDamage = true;
		if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent lastDefender = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			moreDamage = !lastDefender.getDamager().equals(event.getDamager());
		}
		if (event.getDamager().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent lastDamager = (EntityDamageByEntityEvent) event.getDamager().getLastDamageCause();
			moreDamage = moreDamage && !lastDamager.getDamager().equals(event.getEntity());
		}
		if (moreDamage) {
			event.setDamage(event.getDamage() + 8.0);
		}
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape(" NN", "NIN", "SN ");
		recipe.setIngredient('N', Material.IRON_NUGGET);
		recipe.setIngredient('I', Material.IRON_INGOT);
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
