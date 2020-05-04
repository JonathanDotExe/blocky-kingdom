package at.jojokobi.blockykingdom.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class GrapplingHook extends CustomTool{

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Grappling Hook";
	public static final String IDENTIFIER = "grappling_hook";
	public static final short META = 4;
	public static final Material ITEM = Material.IRON_SWORD;
	
	private List<ShotHook> hooks = new ArrayList<>();

	public GrapplingHook(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(321);
		setRepairMaterial(Material.BOW);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("IPI", "WSW", "WBW");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('W', Material.OAK_PLANKS);
		recipe.setIngredient('P', Material.IRON_PICKAXE);
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.BOW);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		boolean shot = false;
		if (!hooks.stream().anyMatch((hook) -> hook.getSnowball().getShooter().equals(player))) {
			shot = true;
			Snowball ball = player.launchProjectile(Snowball.class);
			ShotHook hook = new ShotHook(ball.getLocation().clone(), ball.getVelocity().clone(), ball);
			hooks.add(hook);
		}
		return shot;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Snowball) {
			ShotHook hook = null;
			for (int i = 0; i < hooks.size() && hook == null; i++) {
				if (event.getEntity() == hooks.get(i).getSnowball()) {
					hook = hooks.get(i);
				}
			}
			if (hook != null) {
				hooks.remove(hook);
				if (hook.getSnowball().getShooter() instanceof Entity) {
					Snowball ball = hook.getSnowball().getShooter().launchProjectile(Snowball.class);
					ball.addPassenger((Entity) hook.getSnowball().getShooter());
					ball.teleport(hook.getStartLocation());
					ball.setVelocity(hook.getVelocity());
				}
			}
		}
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return false;
	}

}
