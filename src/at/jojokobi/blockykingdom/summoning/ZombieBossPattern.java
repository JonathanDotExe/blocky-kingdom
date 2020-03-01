package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.entities.ZombieBoss;
import at.jojokobi.mcutil.entity.EntityHandler;

public class ZombieBossPattern implements SummoningPattern{
	
	private Plugin plugin;
	private EntityHandler handler;

	public ZombieBossPattern(Plugin plugin, EntityHandler handler) {
		super();
		this.plugin = plugin;
		this.handler = handler;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = event.getBlock().getType() == Material.ZOMBIE_HEAD && event.getBlock().getRelative(0, -1, 0).getType() == Material.IRON_BLOCK;
		return matches;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		handler.addEntity(new ZombieBoss(event.getBlock().getLocation().add(0, 2, 0), handler));
		Bukkit.getScheduler().runTask(plugin, () -> {
			event.getBlock().setType(Material.AIR);
			event.getBlock().getRelative(0, -1, 0).setType(Material.AIR);
		});
	}

}
