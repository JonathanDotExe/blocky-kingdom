package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.blockykingdom.entities.Airhead;
import at.jojokobi.mcutil.entity.EntityHandler;

public class AirheadPattern implements SummoningPattern{
	
	private Plugin plugin;
	private EntityHandler handler;

	public AirheadPattern(Plugin plugin, EntityHandler handler) {
		super();
		this.plugin = plugin;
		this.handler = handler;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = HeavenDimension.getInstance().isDimension(event.getBlock().getWorld()) && event.getBlock().getRelative(0, 0, 0).getType() == Material.SKELETON_SKULL && event.getBlock().getRelative(0, -1, 0).getType() == Material.WHITE_WOOL && event.getBlock().getRelative(0, 1, 0).getType() == Material.WHITE_WOOL && ((event.getBlock().getRelative(-1, 0, 0).getType() == Material.WHITE_WOOL && event.getBlock().getRelative(1, 0, 0).getType() == Material.WHITE_WOOL) || (event.getBlock().getRelative(0, 0, -1).getType() == Material.WHITE_WOOL && event.getBlock().getRelative(0, 0, 1).getType() == Material.WHITE_WOOL));
		return matches;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		handler.addEntity(new Airhead(event.getBlock().getLocation().add(0, 2, 0), handler));
		Bukkit.getScheduler().runTask(plugin, () -> {
			event.getBlock().setType(Material.AIR);
			event.getBlock().getRelative(0, -1, 0).setType(Material.AIR);
			event.getBlock().getRelative(0, 1, 0).setType(Material.AIR);
			event.getBlock().getRelative(-1, 0, 0).setType(Material.AIR);
			event.getBlock().getRelative(1, 0, 0).setType(Material.AIR);
			event.getBlock().getRelative(0, 0, -1).setType(Material.AIR);
			event.getBlock().getRelative(0, 0, 1).setType(Material.AIR);
		});
	}

}
