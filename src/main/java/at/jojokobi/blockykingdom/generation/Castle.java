package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.Knight;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class Castle extends Structure{
	
	private int wallHeight = 10;
	private ArcherTower archerTower;
	private TraderHut traderHut;
	private EntityHandler entityHandler;
	private DimensionHandler dimHandler;
	
	public Castle(EntityHandler entityHandler, DimensionHandler dimHandler, ArcherTower archerTower, TraderHut traderHut) {
		super(32, 32, 32, 0, 1);
		this.entityHandler = entityHandler;
		this.dimHandler = dimHandler;
		this.archerTower = archerTower;
		this.traderHut = traderHut;
		setxModifier(-651);
		setzModifier(81132);
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		List<StructureInstance<? extends Structure>> structures = new ArrayList<>();
		structures.add(new StructureInstance<Castle>(this, loc.clone(), getWidth(), getHeight(), getLength()));
		
		Location place = loc.clone();
		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		//Walls
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					Material block = Material.AIR;
					if ((y < wallHeight && (x == 1 || x == getWidth() - 2 ||z == 1 || z == getLength() - 2)) ||
						(y == 0) ||
						(y == wallHeight && (x <= 3 || x >= getWidth() - 4 ||z <= 3 || z >= getLength() - 4)) || 
						(y == wallHeight + 1 && (x == 0 || x == getWidth() - 1 ||z == 0 || z == getLength() - 1)) ||
						(y == wallHeight + 2 && (x == 0 || x == getWidth() - 1 ||z == 0 || z == getLength() - 1) && ((x + z % 2) % 2 == 0))) {
						block = Material.STONE_BRICKS;
					}
					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					place.getBlock().setType(block);
				}
			}
		}
		
		//Towers
		place.setX(loc.getX());
		place.setY(loc.getY());
		place.setZ(loc.getZ());
		structures.addAll(archerTower.generate(place, seed));
		
		place.setX(loc.getX() + getWidth() - archerTower.getWidth());
		place.setY(loc.getY());
		place.setZ(loc.getZ());
		structures.addAll(archerTower.generate(place, seed));
		
		place.setX(loc.getX() + getWidth() - archerTower.getWidth());
		place.setY(loc.getY());
		place.setZ(loc.getZ() + getLength() - archerTower.getLength());
		structures.addAll(archerTower.generate(place, seed));
		
		place.setX(loc.getX());
		place.setY(loc.getY());
		place.setZ(loc.getZ() + getLength() - archerTower.getLength());
		structures.addAll(archerTower.generate(place, seed));
		
		//Trader Hut
		place.setX(loc.getX() + 2);
		place.setY(loc.getY());
		place.setZ(loc.getZ() + archerTower.getLength() - 1);
		structures.addAll(traderHut.generate(place, seed));
		
		//Door
		place.setX(loc.getX() + getWidth()/2 - 1);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + 1);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.SOUTH, false, false);
		place.add(-1, 0, -1);
		
		KingdomPoint point = new KingdomPoint(loc);
		Knight knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
		
		place.add(2, 0, 1);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.SOUTH, false, true);
		place.add(1, 0, -1);
		knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
		
		//Guards
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + wallHeight + 1);
		place.setZ(loc.getZ() + 1);
		knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
		
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + wallHeight + 1);
		place.setZ(loc.getZ() + getLength() - 2);
		knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
		
		place.setX(loc.getX() + 1);
		place.setY(loc.getY() + wallHeight + 1);
		place.setZ(loc.getZ() + getLength()/2);
		knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
		
		place.setX(loc.getX() + getWidth() - 2);
		place.setY(loc.getY() + wallHeight + 1);
		place.setZ(loc.getZ() + getLength()/2);
		knight = new Knight(place, entityHandler, random);
		entityHandler.addSavedEntity(knight);
		point.addVillager(knight);
				
		return structures;
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		KingdomPoint point = new KingdomPoint(chunk.getBlock(0, 0, 0).getLocation());
		Kingdom kingdom = KingdomHandler.getInstance().generateKingdom(point);
		Location centerLoc = point.toLocation().add(kingdom.getCenterX(), 0, kingdom.getCenterZ());
		return chunk.getWorld().getEnvironment() == Environment.NORMAL && dimHandler.getDimension(chunk.getWorld()) == null && kingdom.getState() != KingdomState.UNCLAIMED && centerLoc.getBlockX()/TerrainGenUtil.CHUNK_WIDTH == chunk.getX() && centerLoc.getBlockZ()/TerrainGenUtil.CHUNK_LENGTH == chunk.getZ();
	}

	@Override
	public String getIdentifier() {
		return "castle";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<Castle>(this, location, getWidth(), getHeight(), getLength());
	}


}
