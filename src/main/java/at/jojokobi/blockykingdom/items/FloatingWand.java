package at.jojokobi.blockykingdom.items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class FloatingWand extends CustomTool {

	private BlockyKingdomPlugin plugin;

	public static final String NAME = "Floating Wand";
	public static final String IDENTIFIER = "floating_wand";
	public static final short META = 13;
	public static final Material ITEM = Material.IRON_SHOVEL;
	
	private Map<UUID, FlyingInformation> players = new HashMap <>();

	public FloatingWand(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(321);
		setRepairMaterial(Material.IRON_BLOCK);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}

	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("CLC", "CSC", "CSC");
		recipe.setIngredient('C', Cloud.ITEM);
		recipe.setIngredient('L', FrozenLightning.ITEM);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem item = ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE,
				Cloud.IDENTIFIER);
		CustomItem lightning = ItemHandler.getCustomItem(FrozenLightning.class);
		if (isItem(inv.getResult()) && !(item.isItem(inv.getMatrix()[0]) && lightning.isItem(inv.getMatrix()[1])
				&& item.isItem(inv.getMatrix()[2]) && item.isItem(inv.getMatrix()[3])
				&& item.isItem(inv.getMatrix()[5]) && item.isItem(inv.getMatrix()[6])
				&& item.isItem(inv.getMatrix()[8]))) {
			inv.setResult(null);
		}
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		if (player.getCooldown(item.getType()) <= 0) {
			CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
//			if (player.isSprinting()) {
//				Vector velocity = player.getLocation().getDirection();
//				if (velocity.lengthSquared() != 0) {
//					velocity.normalize();
//					velocity.multiply(1 + stats.getMagic()/5.0);
//				}
//				player.setVelocity(velocity);
//			}
//			else {
				FlyingInformation state = players.get(player.getUniqueId());
				if (state == null) {
//					players.put(player.getUniqueId(), new FlyingInformation(System.currentTimeMillis() + 3000, true));
//					player.setVelocity(player.getVelocity().setY(stats.getMagic()/5.0));
//				}
//				else if (state.getUp()) {
					players.put(player.getUniqueId(), new FlyingInformation(System.currentTimeMillis() + 500 * stats.getMagic()));
				}
				else {
					players.remove(player.getUniqueId());
				}
//				if (player.hasPotionEffect(PotionEffectType.LEVITATION) && player.getPotionEffect(PotionEffectType.LEVITATION).getAmplifier() == 255) {
//					player.removePotionEffect(PotionEffectType.LEVITATION);
//				}
//				else if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
//					int duration = stats.getMagic() * 20 + 40;
//					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, 255), true);
//				}
//				else {
//					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, Math.max(1,  2 + stats.getMagic())), true);
//				}
//			}
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		FlyingInformation state = players.get(player.getUniqueId());
		if (state != null) {
			if (state.expired()) {
				players.remove(player.getUniqueId());
			}
			else {
				Vector velocity = null;
				if (player.isSprinting()) {
					velocity = player.getLocation().getDirection().multiply (0.8);
				}
				else {
					velocity = player.getVelocity().multiply(0.99);
					velocity.setY(0);
				}
				
//				if (state.getUp()) {
//					velocity.setY(stats.getMagic()/10.0 * 3);
//				}
//				else if (player.isSprinting()) {
//					velocity = player.getLocation().getDirection().setY(0);
//				}
//				else {
//					velocity = new Vector(0, 0, 0);
//				}
				player.setVelocity(velocity);
				if (event.getTo().getY() > 256) {
					World dimWorld = plugin.getDimensionHandler().getDimensionWorld(event.getPlayer().getWorld(), HeavenDimension.getInstance());
					if (dimWorld != null) {
						event.setTo(new Location(dimWorld, event.getTo().getX(), 150, event.getTo().getZ()));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player && isItem(((Player) event.getEntity()).getInventory().getItemInMainHand())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		players.remove(event.getPlayer().getUniqueId());
	}
	
//	@EventHandler
//	public void onPlayerMove (PlayerMoveEvent event) {
//		Player player = event.getPlayer();
//		if (player.hasPotionEffect(PotionEffectType.LEVITATION) && isItem (player.getInventory().getItemInMainHand())) {
//			Vector velocity = player.getVelocity().clone();
//			double y = velocity.getY();
//			velocity.setY(0);
//			if (velocity.lengthSquared() != 0) {
//				velocity.normalize().multiply(2);
//				velocity.setY(y);
//				player.setVelocity(velocity);
//			}
//		}
//	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return false;
	}

}

class FlyingInformation {
	
	private long expirationTimestamp;
	
	public FlyingInformation (long timestamp) {
		this.expirationTimestamp = timestamp;
	}
	
	public long getExpirationTimestamp () {
		return expirationTimestamp;
	}
	
	public boolean expired () {
		return System.currentTimeMillis() >= getExpirationTimestamp();
	}
	
}

