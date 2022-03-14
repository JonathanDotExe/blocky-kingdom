package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class DoubleBow extends CustomTool{

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Double Bow";
	public static final String IDENTIFIER = "double_bow";
	public static final short META = 1;
	public static final Material ITEM = Material.BOW;

	public DoubleBow(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(385);
		setRepairMaterial(Material.BOW);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("NGN", "IBI", "SSS");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('B', Material.BOW);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('N', Material.GOLD_NUGGET);
		recipe.setIngredient('S', Material.STRING);
		return recipe;
	}
	
	@EventHandler
	public void onProjectileLaunch (EntityShootBowEvent event) {
		LivingEntity shooter = event.getEntity();
		ItemStack held = event.getBow();
		if (isItem(held)) {
			setDurability(held, getDurability(held) - 1);
			boolean shoot = true;
			if (shooter instanceof InventoryHolder) {
				Inventory inventory = ((InventoryHolder) shooter).getInventory();
				int index = inventory.first(Material.ARROW);
				if (index < 0) {
					shoot = false;
				}
				else if (held.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 0) {
					ItemStack arrow = inventory.getItem(index);
					arrow.setAmount(arrow.getAmount() - 1);
				}
			}
			if (shoot) {
				Arrow arrow = shooter.launchProjectile(Arrow.class);
				arrow.teleport(event.getProjectile().getLocation().add(0, -0.3, 0));
				arrow.setVelocity(event.getProjectile().getVelocity());
			}
		}
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		return false;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return false;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		
	}
	
}
