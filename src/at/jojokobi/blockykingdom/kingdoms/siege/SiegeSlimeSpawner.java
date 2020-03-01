package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Slime;

import at.jojokobi.mcutil.entity.EntityHandler;

public class SiegeSlimeSpawner implements SiegeMonsterSpawner{

	@Override
	public void spawn(Location place, EntityHandler handler) {
		place.getWorld().spawn(place, Slime.class);
	}

	@Override
	public int minWave() {
		return 3;
	}

	@Override
	public int minKingdomLevel() {
		return 1;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return 1 + villagers/10;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}

}
