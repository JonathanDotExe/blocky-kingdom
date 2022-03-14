package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class HealingWand extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Healing Wand";
	public static final String IDENTIFIER = "healing_wand";
	public static final short META = 8;
	public static final Material ITEM = Material.IRON_SHOVEL;

	public HealingWand(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(256);
		setRepairMaterial(Material.GLISTERING_MELON_SLICE);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("MHM", " S ", " S ");
		recipe.setIngredient('M', Material.GLISTERING_MELON_SLICE);
		recipe.setIngredient('H', SlimerersHeart.ITEM);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}
	
	@EventHandler
	public void onPrepareItemCraft (PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		if (isItem(inv.getResult()) && !ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, SlimerersHeart.IDENTIFIER).isItem(inv.getMatrix()[1])) {
			inv.setResult(null);
		}
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		if (player.getCooldown(item.getType()) <= 0) {
			CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
			double heal = 2.0 + stats.getMagic()/10.0 * 8.0;
			player.setHealth(Math.min(player.getHealth() + heal, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
			player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 5);
			player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
			player.setCooldown(item.getType(), 100);
			
			for (Entity e : player.getNearbyEntities(1 + stats.getMagic() * 0.5, 1 + stats.getMagic() * 0.5, 1 + stats.getMagic() * 0.5)) {
				if (e instanceof LivingEntity && ! (e instanceof Monster)) {
					LivingEntity entity = (LivingEntity) e;
					entity.setHealth(Math.min(entity.getHealth() + heal * 0.7, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
					entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation(), 5);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return false;
	}

}
