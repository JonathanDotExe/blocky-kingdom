package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class GoblinCaveCenter extends Structure {
	
	public GoblinCaveCenter() {
		super(16, 16, 256, 0, 1);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		BasicGenUtil.generateCube(loc, Material.AIR, getWidth(), getHeight(), getLength());
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		//Cobweb
		for (int i = 0; i < 128; i++) {
			int x = loc.getBlockX() + (int) Math.round(random.nextDouble()) * 15;
			int y = (int) Math.round(random.nextDouble() * 128);
			int z = loc.getBlockZ() + (int) Math.round(random.nextDouble()) * 15;
			
			for (int j = 0; j < 5; j++) {
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
		}
		return Arrays.asList(new StructureInstance<>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "coblin_cave_center";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<>(this, location, getWidth(), getHeight(), getLength());
	}

}
