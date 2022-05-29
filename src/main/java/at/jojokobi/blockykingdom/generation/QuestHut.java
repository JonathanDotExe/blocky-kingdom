package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.QuestVillager;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class QuestHut extends Structure{

	private EntityHandler entityHandler;
	
	public QuestHut(EntityHandler entityHandler) {
		super(5, 5, 5, 0);
		this.entityHandler = entityHandler;
		
		setxModifier(1201);
		setzModifier(1454);
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place, getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		
		//Walls
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				for (int y = 0; y < getHeight(); y++) {
					Material block = Material.AIR;
					if ((x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1 || y == 0) && y == getHeight() - 1) {
						block = Material.BRICK_SLAB;
					}
					else if (y == getHeight() - 1) {
						block = Material.BRICKS;
					}
					else if (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1 || y == 0) {
						block = Material.WHITE_TERRACOTTA;
					}
					else if (y == 2 && z == getLength() - 1 && x > 0 && x < getWidth() - 1) {
						block = Material.GLASS;
					}
					
					place.setX(loc.getX() + x);
					place.setY(loc.getY() + y);
					place.setZ(loc.getZ() + z);
					place.getBlock().setType(block);
				}
			}
		}
		
		//Trader
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength()/2);
		QuestVillager shopKeeper = new QuestVillager(place, entityHandler, random);
		entityHandler.addSavedEntity(shopKeeper);
		shopKeeper.gainXP(random.nextInt(15));
		new KingdomPoint(place).addVillager(shopKeeper);
		
		//Crafting table
		place.setX(loc.getX() + 1);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + getLength() - 2);
		place.getBlock().setType(Material.CRAFTING_TABLE);
		
		place.add(1, 0, 0);
		place.getBlock().setType(Material.CARTOGRAPHY_TABLE);
		place.add(0, 1, 0);
		place.getBlock().setType(Material.TORCH);
		
		//Door
		place.setX(loc.getX() + getWidth()/2);
		place.setY(loc.getY() + 1);
		place.setZ(loc.getZ() + 0);
		FurnitureGenUtil.generateDoor(place, Material.OAK_DOOR, BlockFace.EAST, false, true);
		return Arrays.asList(new StructureInstance<QuestHut>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "quest_hut";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<QuestHut>(this, location, getWidth(), getHeight(),getLength());
	}
}
