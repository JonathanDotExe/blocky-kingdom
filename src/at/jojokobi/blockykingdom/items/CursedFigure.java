package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.Ghost;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.item.PlaceableItem;
import at.jojokobi.mcutil.item.Rotation;

public class CursedFigure extends PlaceableItem{
	
	private static final Vector[] GHOST_SPAWNS = {new Vector(-20, 0, 0), new Vector(20, 0, 0), new Vector(0, 0, -20), new Vector(0, 0, 20)};

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Cursed Figure";
	public static final String IDENTIFIER = "cursed_figure";
	public static final short META = 3;
	public static final Material ITEM = Material.IRON_SHOVEL;
	private EntityHandler entityHandler;

	public CursedFigure(BlockyKingdomPlugin plugin, EntityHandler entityHandler) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		this.entityHandler = entityHandler;
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
		recipe.shape(" E ", "SBS", "SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('B', Material.BONE);
		recipe.setIngredient('E', Material.SPIDER_EYE);
		return recipe;
	}
	
	@Override
	public void spawnDrops(Location place) {
		spawnGhosts(place);
	}
	
	@Override
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		super.onPlayerInteract(event);
		if (event.getClickedBlock() != null) {
			for (Entity entity : event.getClickedBlock().getChunk().getEntities()) {
				if (isItemEntity(entity) && entity.getLocation().distanceSquared(event.getClickedBlock().getLocation()) < 3 * 3) {
					spawnGhosts(entity.getLocation());
				}
			}
		}
	}
	
	private void spawnGhosts (Location place) {
		for (Vector vector : GHOST_SPAWNS) {
			Location loc = place.clone().add(vector);
			//Find Y
			while (loc.getBlock().getType().isSolid()) {
				loc.add(0, 1, 0);
			}
			Ghost ghost = new Ghost(loc, entityHandler);
			entityHandler.addEntity(ghost);
		}
	}

	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

}
