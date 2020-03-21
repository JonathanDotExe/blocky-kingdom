package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class GoblinCharm extends CustomItem {

	public static final String NAME = "Goblin Charm";
	public static final String IDENTIFIER = "goblin_charm";
	public static final short META = 8;
	public static final Material ITEM = Material.GHAST_TEAR;

	private Plugin plugin;

	public GoblinCharm(Plugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(1);
		setHelmet(true);

		// Leather Recipe
		this.plugin = plugin;
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (isItem(player.getInventory().getHelmet())) {
			double radius = 4 + StatHandler.getInstance().getStats(player).getCharacterStats().getMagic() / 2.0;
			for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
				if (entity instanceof Item) {
					Vector velocity = player.getLocation().subtract(entity.getLocation()).toVector();
					if (velocity.length() != 0) {
						entity.setVelocity(velocity.normalize().multiply(0.5));
					}
				}
			}
		}
	}

	@Override
	public void onUse(ItemStack item, Player player) {

	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {

	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem skin = ItemHandler.getCustomItem(GoblinSkin.class);
		CustomItem fang = ItemHandler.getCustomItem(GoblinFang.class);
		if (isItem(inv.getResult()) && !(skin.isItem(inv.getMatrix()[0]) && skin.isItem(inv.getMatrix()[1])
				&& skin.isItem(inv.getMatrix()[2]) && fang.isItem(inv.getMatrix()[6]) && fang.isItem(inv.getMatrix()[7])
				&& fang.isItem(inv.getMatrix()[8]))) {
			inv.setResult(null);
		}
	}

	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("LLL", "S S", "FFF");
		recipe.setIngredient('L', GoblinSkin.ITEM);
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('F', GoblinFang.ITEM);
		return recipe;
	}

}
