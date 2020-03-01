package at.jojokobi.blockykingdom.dimensions;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import at.jojokobi.generator.ValueGenerator;
import at.jojokobi.generator.biome.CustomBiome;

public class CloudBiome extends CustomBiome{

	public CloudBiome() {
		super(0.5, 0.8, 0.3, 0.7, 0.7, 1.0);
	}

	@Override
	public Biome generate(ChunkData data, int x, int z, int startHeight, int height, double heightNoise,
			Random random) {
		
		for (int y = startHeight; y < height; y++) {
			data.setBlock(x, y, z, Material.WHITE_WOOL);
		}
		
		return Biome.THE_VOID;
	}

	@Override
	public void populate(Chunk chunk, ValueGenerator generator, Random random) {
		
	}

}
