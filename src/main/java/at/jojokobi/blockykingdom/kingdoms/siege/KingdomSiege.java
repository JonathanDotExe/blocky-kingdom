package at.jojokobi.blockykingdom.kingdoms.siege;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;

public class KingdomSiege {
	
	public static final int MAX_WAVES = 5;
	
	private KingdomPoint point;
	private int wave = 0;
	
	private List<SiegeMonsterSpawner> spawners = new ArrayList<>();

	public KingdomSiege(KingdomPoint point) {
		super();
		this.point = point;
		
		spawners.add(new SiegeZombieSpawner());
		spawners.add(new SiegeIllusionerSpawner());
		spawners.add(new SiegeSkeletonSpawner());
		spawners.add(new SiegeSpiderSpawner());
		spawners.add(new SiegeWitchSpawner());
		spawners.add(new SiegeZombieTowerSpawner());
		spawners.add(new SiegeZombieBossSpawner());
		spawners.add(new SiegeVindicatorSpawner());
		spawners.add(new SiegeSlimeSpawner());
	}
	
	public void tick (EntityHandler handler) {
		wave++;
		Kingdom kingdom = point.toKingdom();
		//Messages
		for (UUID owner : kingdom.getOwners()) {
			Player player = Bukkit.getPlayer(owner);
			if (player != null) {
				player.sendMessage("Wave " + wave + " is coming to " + kingdom.getName() + "!");
			}
		}
		//Collect goals
		List<Player> players = new ArrayList<>();
		for (UUID uuid : kingdom.getOwners()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				players.add(player);
			}
		}
		List<Entity> all = new ArrayList<>(players.size());
		all.addAll(players);
		for (CustomEntity<?> entity : handler.getEntities()) {
			if (entity instanceof KingdomVillager<?>) {
				if (point.equals(((KingdomVillager<?>) entity).getKingdomPoint())) {
					all.add(entity.getEntity());
				}
			}
		}
		//Spawn monsters
		Random random = new Random();
		for (SiegeMonsterSpawner spawner : spawners) {
			if (wave >= spawner.minWave() && kingdom.getLevel() >= spawner.minKingdomLevel()) {
				for (int i = 0; i < spawner.maxAmountPerWave(point.calcVillagerCount(handler)); i++) {
					Location place = null;
					if (spawner.spawnAtVillagers()) {
						if (!all.isEmpty()) {
							place = all.get(random.nextInt(all.size())).getLocation();
						}
					}
					else if (!players.isEmpty()){
						place = players.get(random.nextInt(players.size())).getLocation();
					}
					
					if (place != null) {
						place.add(random.nextDouble() * 10 - 5, 0, random.nextDouble() * 10 - 5);
						place.setY(place.getWorld().getHighestBlockYAt(place));
						spawner.spawn(place, handler);
					}
				}
			}
		}
	}
	
	public boolean isFinished () {
		return wave >= MAX_WAVES;
	}

	public KingdomPoint getPoint() {
		return point;
	}
	
}
