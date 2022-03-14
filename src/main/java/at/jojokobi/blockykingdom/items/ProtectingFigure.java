package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.item.PlaceableItem;
import at.jojokobi.mcutil.item.Rotation;

public class ProtectingFigure extends PlaceableItem{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Protected Figure";
	public static final String IDENTIFIER = "protecting_figure";
	public static final short META = 10;
	public static final Material ITEM = Material.IRON_SHOVEL;

	public ProtectingFigure(BlockyKingdomPlugin plugin) {
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
						//Knockback
						for (Entity entity : e.getNearbyEntities(10, 10, 10)) {
							if (e.getVehicle() != entity && entity.getLocation().distanceSquared(e.getLocation()) < 100) {
								Vector dir = entity.getLocation().subtract(e.getLocation()).toVector();
								if (dir.lengthSquared() != 0) {
									dir.normalize();
								}
								entity.setVelocity(dir);
							}
						}
					}
				}
			}
		}, 1L, 1L); 
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape(" P ", "SCS", "SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('C', Cloud.ITEM);
		recipe.setIngredient('P', CloudParticle.ITEM);
		
		return recipe;
	}
	
	@Override
	public void spawnDrops(Location place) {
//		place.getWorld().dropItemNaturally(place, getItem());
	}

	@EventHandler
	public void onPrepareItemCraft (PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		CustomItem particle = ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, CloudParticle.IDENTIFIER);
		CustomItem cloud = ItemHandler.getCustomItem(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Cloud.IDENTIFIER);
		if (isItem(inv.getResult()) && !(particle.isItem(inv.getMatrix()[1]) && cloud.isItem(inv.getMatrix()[4]))) {
			inv.setResult(null);
		}
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

}
