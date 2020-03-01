package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.generation.population.VillageSpreader;

public class KingdomVillage extends Structure {
	
	private VillageSpreader spreader;

	public KingdomVillage(Structure... houses) {
		super(128, 128, 32, 600, 1);
		spreader = new VillageSpreader(houses);
		spreader.setBlockFunction(b -> {
			switch (b) {
			case GRASS_BLOCK:
				return Material.GRASS_PATH;
			case STONE:
				return Material.COBBLESTONE;
			case SAND:
				return Material.RED_SAND;
			case WATER:
			case ICE:
			case BLUE_ICE:
			case PACKED_ICE:
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
		return (chunk.getX() == 0 && chunk.getZ() == 0) || super.canGenerate(chunk, seed) && KingdomHandler.getInstance().generateKingdom(new KingdomPoint(chunk.getBlock(0, 0, 0).getLocation())).getState() != KingdomState.UNCLAIMED /* && new KingdomPoint(chunk.getBlock(0, 0, 0).getLocation()).toKingdom().getState() != KingdomState.UNCLAIMED*/;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		List<StructureInstance<?>> structures = new ArrayList<>();
		structures.add(new StructureInstance<Structure>(this, loc, getWidth(), getHeight(), getLength()));
		structures.addAll(spreader.generateVillage(new Random(generateValueBeasedSeed(loc, seed)), seed, loc));
		System.out.println("Generation village at " + loc);
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
