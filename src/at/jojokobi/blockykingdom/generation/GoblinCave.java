package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.generation.population.VillageNode;
import at.jojokobi.mcutil.generation.population.VillageSpreader;

public class GoblinCave extends Structure{
	
	private static final int START_Y = 12;
	private List<VillageSpreader> layers = new ArrayList<>();
	
	@SafeVarargs
	public GoblinCave(Structure[]... layers) {
		super(128, 128, 64, 1200, 1);
		for (Structure[] list : layers) {
			VillageSpreader spreader = new VillageSpreader(list);
			this.layers.add(spreader);
			
		}
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return 0;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		int y = START_Y;
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		loc.setY(calculatePlacementY(getWidth(), getLength(), loc));
		List<StructureInstance<?>> strucs = new ArrayList<StructureInstance<?>>();
		strucs.add(getStandardInstance(loc));
		for (VillageSpreader spreader : layers) {
			VillageNode[][] nodes = spreader.generateVillageMap(random);
			nodes[nodes.length/2][nodes[nodes.length/2].length/2].setHouse(null);
			strucs.addAll(spreader.generateVillage(nodes, seed, loc.clone().add(0, y, 0)));
			y += 12;
		}
		return strucs;
	}

	@Override
	public String getIdentifier() {
		return "goblin_cave";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Structure>(this, location, getWidth(), getHeight(), getLength());
	}

}
