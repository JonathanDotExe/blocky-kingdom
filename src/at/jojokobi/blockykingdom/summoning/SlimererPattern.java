package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.entities.Slimerer;
import at.jojokobi.mcutil.entity.EntityHandler;

public class SlimererPattern implements SummoningPattern{
	
	private Plugin plugin;
	private EntityHandler handler;

	public SlimererPattern(Plugin plugin, EntityHandler handler) {
		super();
		this.plugin = plugin;
		this.handler = handler;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = event.getBlock().getType() == Material.ZOMBIE_HEAD && event.getBlock().getRelative(0, -1, 0).getType() == Material.SLIME_BLOCK;
		return matches;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		handler.addEntity(new Slimerer(event.getBlock().getLocation().add(0, 2, 0), handler));
		Bukkit.getScheduler().runTask(plugin, () -> {
			event.getBlock().setType(Material.AIR);
			event.getBlock().getRelative(0, -1, 0).setType(Material.AIR);
		});
	}

}
