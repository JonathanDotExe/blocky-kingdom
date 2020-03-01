package at.jojokobi.blockykingdom.dimensions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.generator.AbstractGenerator;
import at.jojokobi.generator.NoiseValueGenerator;
import at.jojokobi.generator.ValueGenerator;
import at.jojokobi.generator.biome.BiomeSystem;
import at.jojokobi.generator.biome.Desert;
import at.jojokobi.generator.biome.Forest;
import at.jojokobi.generator.biome.HeightBiomeSystem;
import at.jojokobi.generator.biome.Jungle;
import at.jojokobi.generator.biome.Mountains;
import at.jojokobi.generator.biome.Plains;
import at.jojokobi.generator.biome.SnowyPlains;
import at.jojokobi.generator.biome.VolcanoMountains;
import at.jojokobi.generator.populators.BiomePopulator;
import at.jojokobi.generator.populators.ore.OrePopulator;
import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class HeavenGenerator extends AbstractGenerator{

	@Override
	public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid grid) {
		ChunkData data = super.generateChunkData(world, random, x, z, grid);
		for (int y = 30; y < 55; y += 5) {
			NoiseGenerator generator = new SimplexNoiseGenerator(world.getSeed() + y);
			for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
				for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
					if (generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.025, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.025) > 0.7) {
						data.setBlock(i, y, j, Material.WHITE_WOOL);
						data.setBlock(i, y + 1, j, Material.WHITE_WOOL);
						data.setBlock(i, y + 1, j, Material.WHITE_WOOL);
					}
				}
			}
		}
		return data;
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList(new BiomePopulator(this, this), new OrePopulator());
	}
	
	@Override
	public ValueGenerator createValueGenerator(long seed) {
		return new HeavenValueGenerator(seed);
	}

	@Override
	public BiomeSystem createBiomeSystem(long seed) {
		BiomeSystem system = new HeightBiomeSystem(createValueGenerator(seed));
		system.registerBiome(new Plains());
		system.registerBiome(new SnowyPlains());
		system.registerBiome(new Desert());
		system.registerBiome(new Forest());
		system.registerBiome(new Mountains());
		system.registerBiome(new Jungle());
		system.registerBiome(new VolcanoMountains());
		return system;
	}

}

class HeavenValueGenerator extends NoiseValueGenerator {

	private NoiseValueGenerator generator;
	
	public HeavenValueGenerator(long seed) {
		super(seed);
		generator = new NoiseValueGenerator(seed + 96);
		generator.setMinHeight(0);
		generator.setMaxHeight(180);
		setMinHeight(60);
		setMaxHeight(100);
		setSeaLevel(60);
		setMoistureMultiplier(0.01);
		setTemperatureMultiplier(0.01);
		setHeightMultiplier(0.005);
	}	
	
	@Override
	public int getStartHeight(double x, double z) {
		return generator.getHeight(x, z);
	}
	
}
