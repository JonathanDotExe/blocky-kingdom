package at.jojokobi.blockykingdom.dimensions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.generator.AbstractGenerator;
import at.jojokobi.generator.NoiseValueGenerator;
import at.jojokobi.generator.ValueGenerator;
import at.jojokobi.generator.biome.BiomeEntry;
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
	public void generateNoise(WorldInfo world, Random random, int x, int z, ChunkData data) {
		super.generateNoise(world, random, x, z, data);
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
		HeightBiomeSystem system = new HeightBiomeSystem(createValueGenerator(seed));
		system.registerBiome(new BiomeEntry(new Plains(), 0.1, 0.3, 0.4, 0.7, 0.3, 0.6));
		system.registerBiome(new BiomeEntry(new Desert(), 0, 0.3, 0.5, 1.5, 0.0, 0.5));
		system.registerBiome(new BiomeEntry(new Mountains(), 0.4, 0.8, 0.2, 0.5, 0.3, 0.5));
		system.registerBiome(new BiomeEntry(new VolcanoMountains(), 0.5, 1, 0.7, 1.0, 0.2, 0.5));
		system.registerBiome(new BiomeEntry(new SnowyPlains(), 0, 0.2, 0.0, 0.4, 0.2, 0.6));
		system.registerBiome(new BiomeEntry(new Forest(), 0.1, 0.3, 0.4, 0.7, 0.4, 0.7));
		system.registerBiome(new BiomeEntry(new Jungle(), 0.1, 0.3, 0.7, 1.0, 0.7, 1.1));
		system.registerBiome(new BiomeEntry(new CloudBiome(), 0.5, 0.8, 0.3, 0.7, 0.7, 1.0));
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
