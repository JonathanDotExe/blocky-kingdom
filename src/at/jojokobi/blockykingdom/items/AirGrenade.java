package at.jojokobi.blockykingdom.items;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class AirGrenade extends CustomItem{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = ChatColor.AQUA + "Air Grenade";
	public static final String IDENTIFIER = "air_grenade";
	public static final short META = 5;
	public static final Material ITEM = Material.GHAST_TEAR;
	
	public static final String IS_AIR_GRENADE = "air_grenade";

	public AirGrenade(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxStackSize(0);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ItemStack stack = createItem();
		stack.setAmount(8);
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), stack);
		recipe.shape(" P ", "PCP", " P ");
		recipe.setIngredient('P', CloudParticle.ITEM);
		recipe.setIngredient('C', Cloud.ITEM);
		return recipe;
	}

	
	
	@EventHandler
	public void onProjectileHit (ProjectileHitEvent event) {
		if (event.getEntity().hasMetadata(IS_AIR_GRENADE)) {
			for (Entity entity : event.getEntity().getNearbyEntities(10, 10, 10)) {
				if (entity instanceof Player) {
					((Player) entity).playSound(event.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
				}
				if (event.getEntity().getVehicle() != entity && entity.getLocation().distanceSquared(event.getEntity().getLocation()) < 100) {
					Vector dir = entity.getLocation().subtract(event.getEntity().getLocation()).toVector();
					if (dir.lengthSquared() != 0) {
						dir.normalize();
						dir.multiply(5);
						if (dir.getY() == 0) {
							dir.setY(0.3);
						}
						dir.setY(dir.getY() * 2);
					}
					entity.setVelocity(dir);
				}
			}
		}
	}
	
	public Snowball shootAirGrenade (ProjectileSource source) {
		Snowball ball = source.launchProjectile(Snowball.class);
		double velocity = 3;
		ball.setVelocity(ball.getVelocity().normalize().multiply(velocity));
		ball.setMetadata(IS_AIR_GRENADE, new FixedMetadataValue(plugin, true));
		return ball;
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem cloud = ItemHandler.getCustomItem(Cloud.class);
		CustomItem cloudParticle = ItemHandler.getCustomItem(CloudParticle.class);
		if (isItem(inv.getResult()) && !(cloud.isItem(inv.getMatrix()[4]) && cloudParticle.isItem(inv.getMatrix()[1]) && cloudParticle.isItem(inv.getMatrix()[3]) && cloudParticle.isItem(inv.getMatrix()[5]) && cloudParticle.isItem(inv.getMatrix()[7]))) {
			inv.setResult(null);
		}
	}

	@Override
	public boolean onUse(ItemStack item, Player player) {
		item.setAmount(item.getAmount() - 1);
		shootAirGrenade(player);
		return true;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

}
