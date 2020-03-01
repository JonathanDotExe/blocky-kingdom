package at.jojokobi.blockykingdom.items;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class GoblinKnife extends CustomTool{
	
	public static final String NAME = "Goblin Knife";
	public static final String IDENTIFIER = "goblin_knife";
	public static final short META = 6;
	public static final Material ITEM = Material.IRON_SWORD;

	public GoblinKnife() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(7);
		setSpeed(0.4);
		setMaxDurability(251);
		setRepairMaterial(Material.IRON_INGOT);
		ItemHandler.addCustomItem(this);
//		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
//		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
//		recipe.shape("  I", "NI ", "SN ");
//		recipe.setIngredient('I', Material.IRON_INGOT);
//		recipe.setIngredient('N', Material.IRON_NUGGET);
//		recipe.setIngredient('S', Material.STICK);
		return null;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		return false;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		if (defender instanceof LivingEntity) {
			((LivingEntity) defender).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 1));
			((LivingEntity) defender).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
		}
		return true;
	}

}
