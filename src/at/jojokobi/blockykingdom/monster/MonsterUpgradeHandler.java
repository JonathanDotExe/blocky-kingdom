package at.jojokobi.blockykingdom.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.mcutil.entity.EntityHandler;

public class MonsterUpgradeHandler implements Listener {
	
	private EntityHandler handler;
	private Random random = new Random();
	private List<MonsterUpgrade> upgrades = new ArrayList<>();
	

	public MonsterUpgradeHandler(EntityHandler handler) {
		super();
		this.handler = handler;
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (handler.getCustomEntityForEntity(entity) == null) {
			//Chance
			int level = KingdomHandler.getInstance().getKingdom(entity.getLocation()).getLevel();
			double chance = Math.min(level/20.0, 0.5);
			if (chance > random.nextDouble()) {
				//Get available upgrades
				List<MonsterUpgrade> available = new ArrayList<>();
				for (MonsterUpgrade upgrade : upgrades) {
					if (upgrade.canApply(entity) && level >= upgrade.minLevel()) {
						available.add(upgrade);
					}
				}
				//Upgrade
				if (!available.isEmpty()) {
					MonsterUpgrade upgrade = available.get(random.nextInt(upgrades.size()));
					upgrade.apply(entity);
				}
			}
		}
	}

}
