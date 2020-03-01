package at.jojokobi.blockykingdom.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.World;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomHappinessScheduler implements Runnable{
	
	private long lastTime;
	private World world;
	private EntityHandler handler;
	
	public KingdomHappinessScheduler (World world, EntityHandler handler) {
		lastTime = world.getTime();
		this.world = world;
		this.handler = handler;
	}
	
	@Override
	public void run () {
		if (world.getTime() < lastTime /*Day*/ || (lastTime < 12541 && world.getTime() >= 12541) /*Night*/) {
			//Reset Kingdoms
			for (Kingdom kingdom : KingdomHandler.getInstance().getKingdoms()) {
				kingdom.setHappiness(0);
			}
			//Villagers
			for (CustomEntity<?> entity : handler.getEntities()) {
				if (entity instanceof KingdomVillager<?>) {
					KingdomVillager<?> villager = (KingdomVillager<?>) entity;
					if (villager.getKingdomPoint() != null && villager.getKingdomPoint().toKingdom() != null) {
						villager.getKingdomPoint().toKingdom().setHappiness(villager.getKingdomPoint().toKingdom().getHappiness() + villager.getKingdomHappiness());
						if (villager.getKingdomPoint().toKingdom().getOwners().stream().anyMatch((uuid) -> Bukkit.getPlayer(uuid) != null)) {
							if (villager.getHappiness() < -0.3) {
								villager.addHappiness(0.3);
							}
//							else if (villager.getKingdomHappiness() > 0.2) {
//								villager.addHappiness(-0.2);
//							}
//							else {
//								villager.setHappiness(0);
//							}
 						}
					}
				}
			}
		}
		lastTime = world.getTime();
	}

	public World getWorld() {
		return world;
	}

}
