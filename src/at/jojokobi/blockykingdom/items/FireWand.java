package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class FireWand extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Fire Wand";
	public static final String IDENTIFIER = "fire_wand";
	public static final short META = 1;
	public static final Material ITEM = Material.IRON_SHOVEL;
	
	public static final String NO_EXPLODE = "no_block_damage";

	public FireWand(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(256);
		setRepairMaterial(Material.FLINT);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("FCF", " B ", " S ");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('C', Material.FIRE_CHARGE);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		Fireball ball = (Fireball) player.launchProjectile(Fireball.class);
		ball.setYield(1 + StatHandler.getInstance().getStats(player).getCharacterStats().getMagic() * 0.15f);
		ball.setDirection(ball.getDirection().normalize().multiply(StatHandler.getInstance().getStats(player).getCharacterStats().getMagic()));
		ball.setMetadata(NO_EXPLODE, new FixedMetadataValue(plugin, true));
		return true;
	}
	
	@EventHandler
	public void onEntityExplode (EntityExplodeEvent event) {
		if (event.getEntity().hasMetadata(NO_EXPLODE)) {
			event.blockList().clear();
		}
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return false;
	}

}
