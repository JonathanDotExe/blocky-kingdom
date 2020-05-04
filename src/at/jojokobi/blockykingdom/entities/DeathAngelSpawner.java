package at.jojokobi.blockykingdom.entities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class DeathAngelSpawner {
	
	public static final double DEATH_ANGEL_CHANCE = 0.01;

	public DeathAngelSpawner(Plugin plugin, EntityHandler handler) {
		Random random = new Random ();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (World world : Bukkit.getWorlds()) {
					if (HeavenDimension.getInstance().isDimension(world)) {
						for (Chunk chunk : world.getLoadedChunks()) {
							if (Math.random() < DEATH_ANGEL_CHANCE) {
								int amount = random.nextInt(3) + 1;
								for (int i = 0; i < amount; i++) {
									Location place = chunk.getBlock(random.nextInt(TerrainGenUtil.CHUNK_LENGTH), 150, random.nextInt(TerrainGenUtil.CHUNK_LENGTH)).getLocation();
									handler.addEntity(new DeathAngel(place, handler));
								}
							}
						}
					}
				}
			}
		}, 1000L, 1000L);
	}

}
