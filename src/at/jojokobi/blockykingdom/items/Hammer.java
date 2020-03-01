package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class Hammer extends CustomTool{

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Hammer";
	public static final String IDENTIFIER = "hammer";
	public static final short META = 1;
	public static final Material ITEM = Material.IRON_AXE;

	public Hammer(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(7);
		setSpeed(-3.1);
		setMaxDurability(372);
		setRepairMaterial(Material.IRON_INGOT);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public void onHit(ItemStack item, EntityDamageByEntityEvent event) {
		super.onHit(item, event);
		
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("III", "IBI", " S ");
		recipe.setIngredient('B', Material.IRON_BLOCK);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		return false;
	}
	
	@Override
	public boolean hit(ItemStack item, EntityDamageByEntityEvent event) {
		double damage = event.getDamage() * 0.7;
		for (Entity entity : event.getEntity().getNearbyEntities(3, 3, 3)) {
			if (!entity.equals(event.getEntity()) && !entity.equals(event.getDamager())) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).damage(damage);
				}
			}
		}
		return super.hit(item, event);
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return true;
	}

}
