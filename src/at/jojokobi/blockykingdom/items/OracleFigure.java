package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.item.PlaceableItem;
import at.jojokobi.mcutil.item.Rotation;

public class OracleFigure extends PlaceableItem{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Oracle Figure";
	public static final String IDENTIFIER = "oracle_figure";
	public static final short META = 5;
	public static final Material ITEM = Material.IRON_SHOVEL;

	public OracleFigure(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setRotation(Rotation.CARDINAL);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("WBL", "SBS", "SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('W', Material.WATER);
		recipe.setIngredient('L', Material.LAVA);
		recipe.setIngredient('B', Material.SNOW_BLOCK);
		
		
		return recipe;
	}
	
	@Override
	public void spawnDrops(Location place) {
		place.getWorld().dropItemNaturally(place, createItem());
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity (PlayerInteractAtEntityEvent event) {
//		KingdomPoint point = new KingdomPoint(event.getRightClicked().getLocation());
//		if (isItemEntity(event.getRightClicked()) && event.getRightClicked().getWorld().getTime() > 12000 && !plugin.getSiegeHandler().isSiegeAtPoint(point) && KingdomHandler.getInstance().getKingdom(point).isOwner(event.getPlayer().getUniqueId())) {
//			event.getPlayer().sendMessage("[Oracle Figure] The darkness is rising over " + KingdomHandler.getInstance().getKingdom(point).getName() + "!");
//			plugin.getSiegeHandler().startSiege(point);
//		}
		if (isItemEntity(event.getRightClicked())) {
			event.getPlayer().getWorld().setStorm(!event.getPlayer().getWorld().hasStorm());
			event.getPlayer().sendMessage("I feel that the weather will change very soon!");
		}
	}

	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

}
