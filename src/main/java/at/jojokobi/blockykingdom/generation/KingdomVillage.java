package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;

import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.generation.population.VillageSpreader;

public class KingdomVillage extends Structure {
	
	private VillageSpreader spreader;
	private DimensionHandler dimHandler;

	public KingdomVillage(DimensionHandler dimHandler, Structure... houses) {
		super(128, 128, 32, 600);
		this.dimHandler = dimHandler;
		spreader = new VillageSpreader(houses);
		spreader.setBlockFunction(b -> {
			switch (b) {
			case GRASS_BLOCK:
				return Material.DIRT_PATH;
			case STONE:
				return Material.COBBLESTONE;
			case SAND:
				return Material.RED_SAND;
			case WATER:
			case ICE:
			case BLUE_ICE:
			case PACKED_ICE:
			case AIR:
				return Material.OAK_PLANKS;
			case LAVA:
				return Material.OBSIDIAN;
			default:
				return Material.GRAVEL;
			}
		});
		setxModifier(-541);
		setzModifier(58484);
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		int innerKingdomX = chunk.getX() * TerrainGenUtil.CHUNK_WIDTH % Kingdom.KINGDOM_WIDTH;
		int innerKingdomZ = chunk.getZ() * TerrainGenUtil.CHUNK_LENGTH % Kingdom.KINGDOM_LENGTH;
		return chunk.getWorld().getEnvironment() == Environment.NORMAL && dimHandler.getDimension(chunk.getWorld()) == null && ((chunk.getX() == 0 && chunk.getZ() == 0) || (innerKingdomX + getWidth() < Kingdom.KINGDOM_WIDTH && innerKingdomZ + getLength() < Kingdom.KINGDOM_LENGTH && super.canGenerate(chunk, seed) && KingdomHandler.getInstance().generateKingdom(new KingdomPoint(chunk.getBlock(0, 0, 0).getLocation())).getState() != KingdomState.UNCLAIMED));
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		List<StructureInstance<?>> structures = new ArrayList<>();
		structures.add(new StructureInstance<Structure>(this, loc, getWidth(), getHeight(), getLength()));
		structures.addAll(spreader.generateVillage(new Random(generateValueBeasedSeed(loc, seed)), seed, loc));
		return structures;
	}

	@Override
	public String getIdentifier() {
		return "kingdom_village";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<KingdomVillage>(this, location, getWidth(), getHeight(),getLength());
	}

}
