package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Skeleton;

import at.jojokobi.mcutil.entity.EntityHandler;

public class SiegeSkeletonSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler entity) {
		place.getWorld().spawn(place, Skeleton.class);
	}

	@Override
	public int minWave() {
		return 0;
	}

	@Override
	public int minKingdomLevel() {
		return 0;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return villagers/2;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}
	
}
