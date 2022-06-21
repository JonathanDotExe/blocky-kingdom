package at.jojokobi.blockykingdom.dimensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.mcutil.generation.MultiNoiseGenerator;
import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class HeavenGenerator extends ChunkGenerator {
	
	@Override
	public void generateNoise(WorldInfo world, Random random, int x, int z, ChunkData data) {
		super.generateNoise(world, random, x, z, data);
	}
	
	@Override
	public void generateSurface(WorldInfo world, Random random, int x, int z, ChunkData data) {
		super.generateSurface(world, random, x, z, data);
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> populators = new ArrayList<BlockPopulator>(super.getDefaultPopulators(world));
		populators.add(new BlockPopulator() {
			@Override
			public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
				super.populate(worldInfo, random, x, z, limitedRegion);
				//Remove bottom
				NoiseGenerator generator = new MultiNoiseGenerator(world.getSeed() + 87, 0);
				for (int xPos = 0; xPos < TerrainGenUtil.CHUNK_WIDTH; xPos++) {
					for (int zPos = 0; zPos < TerrainGenUtil.CHUNK_LENGTH; zPos++) {
						int height = (int) (generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + xPos) * 0.005, (z * TerrainGenUtil.CHUNK_LENGTH + zPos) * 0.005) * 60 + 80);
						boolean ended = false;
						for (int yPos = 0; yPos < height || !ended; yPos++) {
							Material type = limitedRegion.getType(x * TerrainGenUtil.CHUNK_WIDTH + xPos, yPos, z * TerrainGenUtil.CHUNK_LENGTH + zPos);
							ended = !type.isSolid() || type.isAir();
							
							if (yPos < height || !ended) {
								BlockState state = limitedRegion.getBlockState(x * TerrainGenUtil.CHUNK_WIDTH + xPos, yPos, z * TerrainGenUtil.CHUNK_LENGTH + zPos);
								state.setType(Material.AIR);
								state.update(true, false);
							}
						}
					}
				}
				//Clouds
				for (int y = 30; y < 55; y += 5) {
					NoiseGenerator g = new SimplexNoiseGenerator(world.getSeed() + y);
					for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
						for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
							if (g.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.025, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.025) > 0.7) {
								limitedRegion.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);
								limitedRegion.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y + 1, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);
								limitedRegion.setType(x * TerrainGenUtil.CHUNK_WIDTH + i, y + 2, z * TerrainGenUtil.CHUNK_LENGTH + j, Material.WHITE_WOOL);							
							}
						}
					}
				}
			}
		});
		return populators;
	}
	
	@Override
	public void generateCaves(WorldInfo world, Random random, int x, int z, ChunkData data) {
		super.generateCaves(world, random, x, z, data);
	}

	@Override
	public boolean shouldGenerateBedrock() {
		return false;
	}
	
	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateNoise() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateStructures() {
		return false;
	}
	
	@Override
	public boolean shouldGenerateCaves() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateMobs() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateSurface() {
		return true;
	}
}

