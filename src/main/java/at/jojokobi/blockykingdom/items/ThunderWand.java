package at.jojokobi.blockykingdom.items;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class ThunderWand extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Thunder Wand";
	public static final String IDENTIFIER = "thunder_wand";
	public static final short META = 14;
	public static final Material ITEM = Material.IRON_SHOVEL;
	
	public static final String SPAWN_THUNDER = "spawn_thunder";

	public ThunderWand(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(256);
		setRepairMaterial(Material.DISPENSER);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("LDL", " S ", " S ");
		recipe.setIngredient('D', Material.DISPENSER);
		recipe.setIngredient('L', FrozenLightning.ITEM);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		shootThunderProjectile (player);
		return true;
	}
	
	@EventHandler
	public void onProjectileHit (ProjectileHitEvent event) {
		if (event.getEntity().hasMetadata(SPAWN_THUNDER)) {
			event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
		}
	}
	
	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		if (event.getCause() == DamageCause.LIGHTNING && event.getEntity() instanceof LivingEntity && isItem(((LivingEntity) event.getEntity()).getEquipment().getItemInMainHand())) {
			event.setCancelled(true);
		}
	}
	
	public Snowball shootThunderProjectile (ProjectileSource source) {
		Snowball ball = source.launchProjectile(Snowball.class);
		Statable stats = StatHandler.getInstance().getStats(source);
		double velocity = 3;
		if (stats != null) {
			velocity = stats.getCharacterStats().getMagic();
		}
		ball.setVelocity(ball.getVelocity().normalize().multiply(velocity));
		ball.setMetadata(SPAWN_THUNDER, new FixedMetadataValue(plugin, true));
		return ball;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		defender.getWorld().strikeLightning(defender.getLocation());
		return true;
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem item = ItemHandler.getCustomItem(FrozenLightning.class);
		if (isItem(inv.getResult()) && !(item.isItem(inv.getMatrix()[0]) && item.isItem(inv.getMatrix()[2]))) {
			inv.setResult(null);
		}
	}

}
