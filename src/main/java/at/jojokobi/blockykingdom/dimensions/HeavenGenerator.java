package at.jojokobi.blockykingdom.dimensions;

import java.util.ArrayList;
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
import at.jojokobi.generator.biome.BiomeSystem;
import at.jojokobi.generator.biome.NoiseValueGenerator;
import at.jojokobi.generator.biome.biomes.DarkForest;
import at.jojokobi.generator.biome.biomes.Desert;
import at.jojokobi.generator.biome.biomes.Forest;
import at.jojokobi.generator.biome.biomes.Jungle;
import at.jojokobi.generator.biome.biomes.Mountains;
import at.jojokobi.generator.biome.biomes.Plains;
import at.jojokobi.generator.biome.biomes.Savanna;
import at.jojokobi.generator.biome.biomes.SnowyPlains;
import at.jojokobi.generator.biome.biomes.Taiga;
import at.jojokobi.generator.biome.biomes.VolcanoMountains;
import at.jojokobi.generator.biome.grid.GridBiomeEntry;
import at.jojokobi.generator.biome.grid.GridBiomeSystem;
import at.jojokobi.generator.populators.BiomePopulator;
import at.jojokobi.generator.populators.ore.OrePopulator;
import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class HeavenGenerator extends AbstractGenerator{
	
	@Override
	public void generateNoise(WorldInfo world, Random random, int x, int z, ChunkData data) {
		super.generateNoise(world, random, x, z, data);
		
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList(new BiomePopulator(this), new OrePopulator(), new CloudPopulator());
	}
	
	@Override
	public BiomeSystem createBiomeSystem(WorldInfo info) {
		List<GridBiomeEntry> biomes = new ArrayList<>();
		biomes.add(new GridBiomeEntry(new Plains(), 0.0, 0.6, 0.0, 0.6));
		biomes.add(new GridBiomeEntry(new Desert(), 0.5, 1, 0.0, 0.5));
		biomes.add(new GridBiomeEntry(new Mountains(), 0.0, 0.7, 0.0, 0.8));
		biomes.add(new GridBiomeEntry(new VolcanoMountains(), 0.5, 1.0, 0.0, 0.5));
		biomes.add(new GridBiomeEntry(new SnowyPlains(), 0.0, 0.5, 0.3, 1.0));
		biomes.add(new GridBiomeEntry(new Forest(), 0.4, 0.7, 0.2, 0.7));
		biomes.add(new GridBiomeEntry(new Jungle(), 0.5, 1.0, 0.5, 1.0));
		biomes.add(new GridBiomeEntry(new Taiga(), 0.1, 0.7, 0.0, 0.7));
		biomes.add(new GridBiomeEntry(new DarkForest(), 0.4, 0.85, 0.35, 0.9));
		biomes.add(new GridBiomeEntry(new Savanna(), 0.5, 1.0, 0.0, 0.6));
		
		GridBiomeSystem system = new GridBiomeSystem(info.getSeed(), s -> new HeavenValueGenerator(s));

		for (GridBiomeEntry entry : biomes) {
			system.registerBiome(entry);
			system.registerOceanBiome(entry);
		}
		return system;
	}
	
	@Override
	public boolean shouldGenerateMobs() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateCaves() {
		return true;
	}
	
	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}

	@Override
	public boolean shouldGenerateStructures() {
		return true;
	}
	
}

class HeavenValueGenerator extends NoiseValueGenerator {

	private NoiseValueGenerator generator;
	private NoiseGenerator gen;
	private double heightShiftMul = 0.0001;
	
	public HeavenValueGenerator(long seed) {
		super(seed, 64, 150);
		gen = new SimplexNoiseGenerator(seed - 81426);
		generator = new NoiseValueGenerator(seed + 96, 0, 160);
		generator.setHeightMultiplier(0.005);
		generator.setSeaLevel(70);
		setMinHeight(40);
		setMaxHeight(120);
		setSeaLevel(70);
		setHeightMultiplier(0.005);
	}	
	
	@Override
	public int getHeight(double x, double z, double noise) {
		return super.getHeight(x, z, noise) + (int) (40 * (gen.noise(x * heightShiftMul, z * heightShiftMul) + 1));
	}	
	
	@Override
	public int getStartHeight(double x, double z) {
		return generator.getHeight(x, z, generator.getHeightNoise(x, z)) + (int) (40 * (gen.noise(x * heightShiftMul, z * heightShiftMul) + 1));
	}
	
}
