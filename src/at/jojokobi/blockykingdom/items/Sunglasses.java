package at.jojokobi.blockykingdom.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class Sunglasses extends CustomItem {
	
	public static final String NAME = "Cool Sunglasses";
	public static final String IDENTIFIER = "sunglasses";
	public static final short META = 15;
	public static final Material ITEM = Material.IRON_SHOVEL;
	
	private Plugin plugin;

	public Sunglasses(Plugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setHelmet(true);
		ItemHandler.addCustomItem(this);
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		if (isItem(event.getPlayer().getInventory().getHelmet())) {
			Bukkit.getScheduler().runTask(plugin, () -> {
				List<Entity> entities = event.getPlayer().getNearbyEntities(4, 4, 4);
				for (Entity entity : entities) {
					if (entity.getLocation().distanceSquared(event.getTo()) <= 4 * 4 && entity.getLocation().distanceSquared(event.getFrom()) > 4 * 4) {
						event.getPlayer().sendMessage("[" + entity.getName() + "] Cool man");
					}
				}
			});
		}
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
