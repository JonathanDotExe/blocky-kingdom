package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Spider;

import at.jojokobi.mcutil.entity.EntityHandler;

public class SiegeSpiderSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler entity) {
		place.getWorld().spawn(place, Spider.class);
	}

	@Override
	public int minWave() {
		return 2;
	}

	@Override
	public int minKingdomLevel() {
		return 0;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return villagers/3 * 2;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}
	
}
