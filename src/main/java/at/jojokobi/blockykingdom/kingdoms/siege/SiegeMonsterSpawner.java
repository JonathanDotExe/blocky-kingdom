package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;

import at.jojokobi.mcutil.entity.EntityHandler;

public interface SiegeMonsterSpawner {

	public void spawn (Location place, EntityHandler handler);
	
	public int minWave ();
	
	public int minKingdomLevel ();
	
	public int maxAmountPerWave (int villagers);
	
	public boolean spawnAtVillagers ();
	
}
