package at.jojokobi.blockykingdom.dimensions;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import at.jojokobi.generator.biome.CustomBiome;
import at.jojokobi.generator.ValueGenerator;

public class CloudBiome implements CustomBiome{

	public CloudBiome() {
		
	}

	@Override
	public void generate(ChunkData data, int x, int z, int startHeight, int height, double heightNoise,
			Random random) {
		
		for (int y = startHeight; y < height; y++) {
			data.setBlock(x, y, z, Material.WHITE_WOOL);
		}
		
	}

	@Override
	public void populate(Chunk chunk, ValueGenerator generator, Random random) {
		
	}

	@Override
	public Biome getBiome(int x, int y, int z, int height, double heightNoise) {
		return Biome.THE_VOID;
	}

}
