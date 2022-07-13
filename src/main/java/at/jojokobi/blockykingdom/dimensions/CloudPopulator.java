package at.jojokobi.blockykingdom.dimensions;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class CloudPopulator extends BlockPopulator {

	@Override
	public void populate(WorldInfo world, Random random, int x, int z, LimitedRegion region) {
		super.populate(world, random, x, z, region);
		for (int y = 40; y <= 70; y += 10) {
			NoiseGenerator generator = new SimplexNoiseGenerator(world.getSeed() + y);
			for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
				for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
					if (generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.025, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.025) > 0.7) {
						region.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);
						region.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y + 1, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);
						region.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y + 2, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);
					}
				}
			}
		}
	}
	
}
