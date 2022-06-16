package at.jojokobi.blockykingdom.dimensions;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
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
		//Remove bottom
		NoiseGenerator generator = new MultiNoiseGenerator(world.getSeed(), 5);
		for (int xPos = 0; xPos < TerrainGenUtil.CHUNK_WIDTH; xPos++) {
			for (int zPos = 0; zPos < TerrainGenUtil.CHUNK_LENGTH; zPos++) {
				int height = (int) generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + xPos) * 0.005, (z * TerrainGenUtil.CHUNK_LENGTH + zPos) * 0.005) * 40 + 60;
				for (int yPos = 0; yPos < height; yPos++) {
					data.setBlock(xPos, yPos, zPos, Material.AIR);
				}
				data.set
			}
		}
		//Clouds
		for (int y = 30; y < 55; y += 5) {
			NoiseGenerator g = new SimplexNoiseGenerator(world.getSeed() + y);
			for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
				for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
					if (g.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.025, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.025) > 0.7) {
						data.setBlock(i, y, j, Material.WHITE_WOOL);
						data.setBlock(i, y + 1, j, Material.WHITE_WOOL);
						data.setBlock(i, y + 1, j, Material.WHITE_WOOL);
					}
				}
			}
		}
		System.out.println("Surface " + x + "/" + z);
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

