package at.jojokobi.blockykingdom.kingdoms.siege;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomSiegeHandler {
	
	private List<KingdomSiege> sieges = new LinkedList<KingdomSiege> ();

	public KingdomSiegeHandler(Plugin plugin, EntityHandler handler) {
		super();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			//Start Sieges
			for (KingdomPoint point : KingdomHandler.getInstance().getKingdomPoints()) {
				if (Math.random() <= 0.1 && point.toKingdom().getOwners().stream().anyMatch((owner) -> {
					Player player = Bukkit.getPlayer(owner);
					return player != null && new KingdomPoint(player.getLocation()).equals(point);
				}) && point.getWorld().getTime() >= 11615 && point.getWorld().getTime() <= 11615 + 3400 && !isSiegeAtPoint(point)) {
					startSiege(point);
				}
			}
			//Update Sieges
			for (Iterator<KingdomSiege> iterator = sieges.iterator(); iterator.hasNext();) {
				KingdomSiege siege = iterator.next();
				siege.tick(handler);
				if (siege.isFinished()) {
					iterator.remove();
				}
			}
		}, 20L * 60L, 20L * 60L);
	}
	
	public void startSiege (KingdomPoint point) {
		sieges.add(new KingdomSiege(point));
	}
	
	public boolean isSiegeAtPoint (KingdomPoint point) {
		return sieges.stream().anyMatch((siege) -> siege.getPoint().equals(point));
	}

}
