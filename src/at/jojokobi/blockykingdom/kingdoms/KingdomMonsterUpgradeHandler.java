package at.jojokobi.blockykingdom.kingdoms;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomMonsterUpgradeHandler implements Listener {
	
	private EntityHandler handler;
	

	public KingdomMonsterUpgradeHandler(EntityHandler handler) {
		super();
		this.handler = handler;
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (handler.getCustomEntityForEntity(entity) == null) {
			int level = KingdomHandler.getInstance().getKingdom(entity.getLocation()).getLevel();
			
		}
	}

}
