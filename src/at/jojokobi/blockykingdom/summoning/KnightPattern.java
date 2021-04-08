package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.Knight;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KnightPattern implements SummoningPattern{
	
	private Plugin plugin;
	private EntityHandler handler;

	public KnightPattern(Plugin plugin, EntityHandler handler) {
		super();
		this.plugin = plugin;
		this.handler = handler;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = event.getBlock().getType() == Material.SOUL_CAMPFIRE;
		Block rel = event.getBlock().getRelative(-1, 0, 0);
		matches = matches && rel.getType() == Material.CHEST && ((Chest) rel.getState()).getBlockInventory().first(new ItemStack(Material.COOKED_BEEF, 5)) >= 0 && ((Chest) rel.getState()).getBlockInventory().first(new ItemStack(Material.IRON_SWORD, 1)) >= 0;
		return matches;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		handler.addEntity(new Knight(event.getBlock().getLocation().add(1, 0, 0), handler));
		Bukkit.getScheduler().runTask(plugin, () -> {
			event.getBlock().setType(Material.CAMPFIRE);
			Block rel = event.getBlock().getRelative(-1, 0, 0);
			((Chest) rel.getState()).getInventory().clear();
		});
	}

}
