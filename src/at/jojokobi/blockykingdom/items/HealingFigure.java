package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.item.PlaceableItem;
import at.jojokobi.mcutil.item.Rotation;

public class HealingFigure extends PlaceableItem{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Healing Figure";
	public static final String IDENTIFIER = "healing_figure";
	public static final short META = 9;
	public static final Material ITEM = Material.IRON_SHOVEL;

	public HealingFigure(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setRotation(Rotation.CARDINAL);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
		
		//Loop
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (World world : Bukkit.getWorlds()) {
				for (Entity e : world.getEntities()) {
					if (isItemEntity(e)) {
						//Heal
						for (Entity entity : e.getNearbyEntities(20, 20, 20)) {
							if (entity instanceof LivingEntity && !(entity instanceof Monster)) {
								LivingEntity living = (LivingEntity) entity;
								living.setHealth(Math.min(living.getHealth() + 6.0, living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
							}
						}
					}
				}
			}
		}, 20 * 15, 20 * 15); 
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape("HGH", "SHS", "SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('H', SlimerersHeart.ITEM);
		recipe.setIngredient('G', Material.GHAST_TEAR);		
		
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
	}

	@EventHandler
	public void onPrepareItemCraft (PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem item = ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, SlimerersHeart.IDENTIFIER);
		if (isItem(inv.getResult()) && !(item.isItem(inv.getMatrix()[0]) && item.isItem(inv.getMatrix()[2]) && item.isItem(inv.getMatrix()[4]))) {
			inv.setResult(null);
		}
	}
	
	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

}
