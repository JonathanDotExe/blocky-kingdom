package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.generation.population.TunnelPathGenerator;
import at.jojokobi.mcutil.generation.population.VillageNode;
import at.jojokobi.mcutil.generation.population.VillageSpreader;

public class GoblinCave extends Structure{
	
	private static final int START_Y = 12;
	private List<VillageSpreader> layers = new ArrayList<>();
	private Structure center;
	private Structure kingRoom;
	
	@SafeVarargs
	public GoblinCave(Structure center, Structure kingRoom, Structure[]... layers) {
		super(128, 128, 64, 1200, 1);
		this.center = center;
		this.kingRoom = kingRoom;
		for (Structure[] list : layers) {
			VillageSpreader spreader = new VillageSpreader(list);
			spreader.setBlockFunction(m -> Material.AIR);
			spreader.setPathGenerator(new TunnelPathGenerator());
			spreader.setForceHeight(true);
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
			int centerY = nodes.length/2;
			int centerX = nodes[centerY].length/2;
			nodes[centerY][centerX].setHouse(null);
			//King room
			if (y == START_Y) {
				List<Point2D> points = new ArrayList<Point2D>();
				for (int i = 0; i < nodes.length; i++) {
					for (int j = 0; j < nodes[i].length; j++) {
						if (nodes[i][j] != null && (i < centerY - 1 || i > centerY + 1) && (j < centerX - 1 && j > centerX + 1)) {
							points.add(new Point2D(j, i));
						}
					}
				}
				if (points.isEmpty()) {
					if (nodes[centerY][centerX - 1] == null) {
						nodes[centerY][centerX - 1] = new VillageNode();
					}
					if (nodes[centerY][centerX - 2] == null) {
						nodes[centerY][centerX - 2] = new VillageNode();
					}
					nodes[centerY][centerX - 2].setHouse(kingRoom);
					nodes[centerY][centerX - 2].setRight(true);
					nodes[centerY][centerX - 1].setLeft(true);
					nodes[centerY][centerX - 1].setRight(true);
					nodes[centerY][centerX].setLeft(true);
				}
				else {
					Point2D point = points.get(random.nextInt(points.size()));
					nodes[point.y][point.x].setHouse(kingRoom);
				}
				//Cave entry
				nodes[centerY][centerX].setHouse(center);
			}
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

class Point2D {
	
	public int x;
	public int y;
	public Point2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Point2D() {
		
	}
	
}
