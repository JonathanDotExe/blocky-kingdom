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
	}

	@Override
	public long getSeedOffset() {
		return -87;
	}

}
