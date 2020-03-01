package at.jojokobi.blockykingdom.kingdoms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomHappinessHandler implements Listener{
	
	private List<KingdomHappinessScheduler> schedulers = new LinkedList<>();
	
	private EntityHandler handler;
	
	public KingdomHappinessHandler (Plugin plugin, EntityHandler handler) {
		this.handler = handler;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> schedulers.forEach ((s) -> s.run()), 10L, 10L);
	}
	
	@EventHandler
	public void onWorldLoad (WorldLoadEvent event) {
		schedulers.add(new KingdomHappinessScheduler(event.getWorld(), handler));
	}
	
	@EventHandler
	public void onWorldUnload (WorldUnloadEvent event) {
		for (Iterator<KingdomHappinessScheduler> iterator = schedulers.iterator(); iterator.hasNext();) {
			KingdomHappinessScheduler kingdomHappinessScheduler = iterator.next();
			if (kingdomHappinessScheduler.getWorld().equals(event.getWorld())) {
				iterator.remove();
			}
		}
	}
	
}
