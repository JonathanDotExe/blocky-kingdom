package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.QuestVillager;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class QuestHut extends Structure{

	private EntityHandler entityHandler;
	private Building building;
	
	public QuestHut(EntityHandler entityHandler) {
		super(5, 5, 5, 0);
		this.entityHandler = entityHandler;
		building = Building.loadBuilding(getClass().getResourceAsStream("/buildings/quest_hut.yml"));
		
		setxModifier(1201);
		setzModifier(1454);
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}

	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		BasicGenUtil.generateCube(loc, Material.AIR, getWidth(), getHeight(), getLength());
		int rotations = random.nextInt(4);
		building.build(loc, (l, mark) -> {
			switch (mark) {
			case "quest_villager":
			{
				QuestVillager shopKeeper = new QuestVillager(l, entityHandler, random);
				entityHandler.addSavedEntity(shopKeeper);
				shopKeeper.gainXP(random.nextInt(15));
				new KingdomPoint(l).addVillager(shopKeeper);
			}
				break;
			}
		}, rotations, false);
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
