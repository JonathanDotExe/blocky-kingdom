package at.jojokobi.blockykingdom.dimensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;


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

