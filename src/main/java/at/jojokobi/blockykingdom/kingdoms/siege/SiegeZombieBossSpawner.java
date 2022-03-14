package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;

import at.jojokobi.blockykingdom.entities.ZombieBoss;
import at.jojokobi.mcutil.entity.EntityHandler;

public class SiegeZombieBossSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler handler) {
		ZombieBoss boss = new ZombieBoss(place, handler);
		boss.setDespawnTicks(5000);
		handler.addEntity(boss);
	}

	@Override
	public int minWave() {
		return 5;
	}

	@Override
	public int minKingdomLevel() {
		return 8;
	}

	@Override
	public int maxAmountPerWave(int level) {
		return 1;
	}

	@Override
	public boolean spawnAtVillagers() {
		return false;
	}

	
	
}
