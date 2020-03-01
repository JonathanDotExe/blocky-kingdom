package at.jojokobi.blockykingdom.dimensions;

import org.bukkit.generator.ChunkGenerator;

import at.jojokobi.mcutil.dimensions.CustomDimension;

public class HeavenDimension implements CustomDimension{
	
	private static HeavenDimension instance;
	
	public static HeavenDimension getInstance () {
		if (instance == null) {
			instance = new HeavenDimension();
		}
		return instance;
	}
	
	private HeavenDimension() {
		
	}

	@Override
	public String getSaveName() {
		return "bk_heaven";
	}

	@Override
	public String getName() {
		return "Heaven";
	}

	@Override
	public ChunkGenerator createGenerator() {
		return new HeavenGenerator();
/*		return new ChunkGenerator() {
			
			@Override
			public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid grid) {
				ChunkData data = createChunkData(world);

				NoiseGenerator generator = new SimplexNoiseGenerator(world.getSeed());
				NoiseGenerator generator2 = new SimplexNoiseGenerator(world.getSeed() + 96);
				
				for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
					for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
						double noise =  generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.01, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.01);
						double noise2 = generator2.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.02, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.02) * 2;
						if (noise2 < noise) {
							int height = (int) Math.round(80 + noise * 20);
							for (int k = (int) Math.round(80 + noise2 * 20); k < height; k++) {
								if (k  < height - 4) {
									data.setBlock(i, k, j, Material.STONE);
								}
								else if (k < height - 1) {
									data.setBlock(i, k, j, Material.DIRT);
								}
								else {
									data.setBlock(i, k, j, Material.GRASS_BLOCK);
								}
							}
						}
//						if (noise2 > 0.0) {
//							double noise = generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.005, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.005);
//							int noiseInt = (int) Math.round(noise * 10 + 5) * 4;
//							
//							grid.setBiome(i, j, Biome.FOREST);
//							
//							data.setBlock(i, 60 + noiseInt, j, Material.WHITE_WOOL);
//							data.setBlock(i, 61 + noiseInt, j, Material.WHITE_WOOL);
//							if (noise > 0.2) {
//								data.setBlock(i, 62 + noiseInt, j, Material.WHITE_WOOL);
//							}
//						}
					}
				}
				
				return data;
			}
		};*/
	}

}
