package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.blockykingdom.entities.GoblinBoss;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class GoblinBossChamber extends Structure {
	
	private EntityHandler entityHandler;

	public GoblinBossChamber(EntityHandler entityHandler) {
		super(24, 24, 8, 0, 1);
		this.entityHandler = entityHandler;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		loc.add(0, -1, 0);
		Location place = loc.clone();
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		// Walls
		for (int x = 0; x < getWidth(); x++) {
			place.setX(loc.getX() + x);
			for (int y = 0; y < getHeight(); y++) {
				place.setY(loc.getY() + y);
				for (int z = 0; z < getLength(); z++) {
					place.setZ(loc.getZ() + z);
					Material material = Material.AIR;
					//Netherrack
					if (y == 0 && (x == 1 || x == getWidth() - 2) && (z == 1 || z == getLength() - 2)) {
						material = Material.NETHERRACK;
					}
					else if (y == 1 && (x == 1 || x == getWidth() - 2) && (z == 1 || z == getLength() - 2)) {
						material = Material.FIRE;
					}
					//Door
					else if ((y > 0 && y <= 3) && (((x == 0 || x == getWidth() - 1) && (z >= getLength() / 2 - 1 && z <= getLength() / 2 + 2)) || ((z == 0 || z == getLength() - 1) && (x >= getWidth() / 2 - 1 || x <= getWidth() / 2 + 2)))) {
						material = Material.IRON_BARS;
					}
					//Wall
					else if (((x == 0 || x == getWidth() - 1)
							&& (z < getLength() / 2 - 1 || z > getLength() / 2 + 2 || y > 3))
							|| ((z == 0 || z == getLength() - 1)
									&& (x < getWidth() / 2 - 1 || x > getWidth() / 2 + 2 || y > 3))
							|| y == 0 || y == getHeight() - 1) {
						material = Material.BRICKS;
					}
					//Table
					else if (y == 1 && (x == getWidth()/2 - 3 || x == getWidth()/2 + 3) && (z == 10 || z == getLength() - 4)) {
						material = Material.OAK_LOG;
					}
					else if (y == 2 && x >= getWidth()/2 - 3 && x <= getWidth()/2 + 3 && z >= 10 && z <= getLength() - 4) {
						material = Material.OAK_SLAB;
					}
					//Hay
					else if (y == 1 && random.nextInt(32) == 0) {
						material = Material.HAY_BLOCK;
					}
					place.getBlock().setType(material);
				}
			}
		}
		//Boss
		entityHandler.addSavedEntity(new GoblinBoss(loc.clone().add(getWidth()/2, 3, getLength()/2), entityHandler));
		return Arrays.asList(new StructureInstance<>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "goblin_boss_chamber";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Structure>(this, location, getWidth(), getHeight(), getLength());
	}

}
