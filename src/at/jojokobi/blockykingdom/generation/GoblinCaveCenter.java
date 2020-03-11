package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.mcutil.VectorUtil;
import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class GoblinCaveCenter extends Structure {
	
	public GoblinCaveCenter() {
		super(16, 16, 256, 0, 1);
	}

	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return 0;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		loc.setY(calculatePlacementY(getWidth(), getLength(), loc));
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		BasicGenUtil.generateCube(loc, Material.AIR, b -> {
			if (VectorUtil.interpolate(0.1, 0, b.getY()/64.0) > random.nextDouble()) {
				b.setType(Material.COBWEB);
			}
		}, getWidth(), getHeight(), getLength());
		//Cobweb
		/*for (int i = 0; i < 256; i++) {
			int x = (int) Math.round(random.nextDouble()) * 15;
			int y = (int) Math.round(random.nextDouble() * 128);
			int z = (int) Math.round(random.nextDouble()) * 15;
			
			for (int j = 0; j < 20; j++) {
				if (loc.getBlock().getRelative(x, y, z).getType().isAir()) {
					loc.getBlock().getRelative(x, y, z).setType(Material.COBWEB);
					switch(random.nextInt(6)) {
					case 0:
						x--;
						break;
					case 1:
						x++;
						break;
					case 2:
						y--;
						break;
					case 3:
						y++;
						break;
					case 4:
						z--;
						break;
					case 5:
						z--;
						break;
					}
				}
			}
		}*/
		return Arrays.asList(new StructureInstance<>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "goblin_cave_center";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<>(this, location, getWidth(), getHeight(), getLength());
	}

}
