package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.Archer;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class ArcherTower extends Structure {

	private EntityHandler entityHandler;

	public ArcherTower(EntityHandler entityHandler) {
		super(7, 7, 20, 0);
		this.entityHandler = entityHandler;

		setxModifier(-6417);
		setzModifier(-894);
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.STONE_BRICKS));
		return super.generateNaturally(place, seed);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();

		Random random = new Random(generateValueBeasedSeed(loc, seed));

		// Walls
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					Material block = Material.AIR;
					int wallHeight = getHeight() - 3;
					if (x == getWidth() - 3 && z == getLength() - 3 && y > 0 && y <= wallHeight) {
						block = Material.LADDER;
					} else if ((y < wallHeight && (x == 1 || x == getWidth() - 2 || z == 1 || z == getLength() - 2))
							|| (y == 0) || (y == wallHeight) || (y == 10)
							|| (y == wallHeight + 1 && (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1))
							|| (y == wallHeight + 2 && (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1)
									&& ((x + z % 2) % 2 == 0))) {
						block = Material.STONE_BRICKS;
					}

					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					if (block != Material.AIR) {
						place.getBlock().setType(block);
					}
				}
			}
		}
		
		//Torch
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength()/2);
		place.getBlock().setType(Material.TORCH, false);
		
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 11);
		place.setZ(loc.getZ() + getLength()/2);
		place.getBlock().setType(Material.TORCH, false);

		// Archer
		place.setX(loc.getX() + getWidth() / 2);
		place.setY(loc.getY() + getHeight() - 2);
		place.setZ(loc.getZ() + getLength() / 2);
		Archer archer = new Archer(place, entityHandler, random);
		entityHandler.addSavedEntity(archer);
		archer.gainXP(random.nextInt(25));
		new KingdomPoint(place).addVillager(archer);

		// Doors
		place.setY(loc.getY() + 1);
		placeDoors(loc, place);
		place.setY(loc.getY() + 11);
		placeDoors(loc, place);

		return Arrays.asList(new StructureInstance<ArcherTower>(this, loc, getWidth(), getHeight(), getLength()));
	}

	private void placeDoors(Location loc, Location place) {
		place.setX(loc.getX() + getWidth() / 2);
		place.setZ(loc.getZ() + 1);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.SOUTH, false, false);

		place.setX(loc.getX() + getWidth() / 2);
		place.setZ(loc.getZ() + getLength() - 2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.NORTH, false, false);

		place.setX(loc.getX() + 1);
		place.setZ(loc.getZ() + getLength() / 2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.EAST, false, false);

		place.setX(loc.getX() + getWidth() - 2);
		place.setZ(loc.getZ() + getLength() / 2);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.WEST, false, false);
	}

	@Override
	public String getIdentifier() {
		return "archer_tower";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<ArcherTower>(this, location, getWidth(), getHeight(),
				getLength());
	}

}
