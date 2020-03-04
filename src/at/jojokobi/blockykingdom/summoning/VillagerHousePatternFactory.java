package at.jojokobi.blockykingdom.summoning;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.QuestVillager;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Recruiter;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;

public class VillagerHousePatternFactory {
	
	public static SummoningPattern createRecruiterPattern (EntityHandler handler, GenerationHandler genHandler, Structure structure) {
		return new DynamicVillagerHousePattern(handler, genHandler, structure,  (loc, hand) -> new Recruiter (loc, hand), name -> "The recruiter " + name + " just moved in!", new Vector(8, 5, 8), new Vector(12, 5, 12), Arrays.asList(new SingleBlockFuniture(Material.ANVIL), new SingleBlockFuniture(Material.CRAFTING_TABLE), new SingleBlockFuniture(Material.FURNACE)), b -> b.getType().isSolid(), 0.75);
		/*return new StaticVillagerHousePattern(handler, genHandler, structure, new StaticVillagerHousePattern.HouseVerifier() {
			
			@Override
			public boolean verifyBlock(Block block, int x, int y, int z) {
				return ((x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (block.getType().isSolid())) ||
						(x == 1 && y == 1 && z == 1 && block.getType() == Material.ANVIL) ||
						(x == 2 && y == 1 && z == 1 && block.getType() == Material.CRAFTING_TABLE) ||
						(x == 3 && y == 1 && z == 1 && block.getType() == Material.FURNACE) ||
						(!(x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (!block.getType().isSolid()));
			}
			
			@Override
			public Material getStartBlock() {
				return Material.ANVIL;
			}
			
			@Override
			public Vector getSize() {
				return new Vector(8, 5, 8);
			}
			
			@Override
			public Vector getOffset() {
				return new Vector(-1, -1, -1);
			}
		}, (loc, hand) -> new Recruiter (loc, hand), name -> "The recruiter " + name + " just moved in!");*/
	}
	
	public static SummoningPattern createTraderPattern (EntityHandler handler, GenerationHandler genHandler, Structure structure) {
		return new DynamicVillagerHousePattern(handler, genHandler, structure, (loc, hand) -> new Trader (loc, hand), name -> "The trader " + name + " just moved in!", new Vector(8, 5, 8), new Vector(12, 5, 12), Arrays.asList(new SingleBlockFuniture(Material.CRAFTING_TABLE), new SingleBlockFuniture(Material.CHEST), new TableFurniture()), b -> b.getType().isSolid(), 0.75);
		/*return new StaticVillagerHousePattern(handler, genHandler, structure, new StaticVillagerHousePattern.HouseVerifier() {
			
			@Override
			public boolean verifyBlock(Block block, int x, int y, int z) {
				return ((x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (block.getType().isSolid())) ||
						(x == 1 && y == 1 && z == 1 && block.getType() == Material.CRAFTING_TABLE) ||
						(x == 2 && y == 1 && z == 1 && block.getType() == Material.CHEST) ||
						(x == 3 && y == 1 && z == 1 && block.getType() == Material.OAK_FENCE) ||
						(x == 3 && y == 2 && z == 1 && block.getType() == Material.OAK_PRESSURE_PLATE) ||
						(!(x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (!block.getType().isSolid()));
			}
			
			@Override
			public Material getStartBlock() {
				return Material.CRAFTING_TABLE;
			}
			
			@Override
			public Vector getSize() {
				return new Vector(8, 5, 8);
			}
			
			@Override
			public Vector getOffset() {
				return new Vector(-1, -1, -1);
			}
		}, (loc, hand) -> new Trader (loc, hand), name -> "The trader " + name + " just moved in!");*/
	}
	
	public static SummoningPattern createQuestHutPattern (EntityHandler handler, GenerationHandler genHandler, Structure structure) {
		return new DynamicVillagerHousePattern(handler, genHandler, structure, (loc, hand) -> new QuestVillager (loc, hand), name -> "The quest villager " + name + " just moved in!", new Vector(4, 5, 4), new Vector(8, 5, 8), Arrays.asList(new SingleBlockFuniture(Material.CARTOGRAPHY_TABLE), new SingleBlockFuniture(Material.CRAFTING_TABLE)), b -> b.getType().isSolid(), 0.75);
		/*return new StaticVillagerHousePattern(handler, genHandler, structure, new StaticVillagerHousePattern.HouseVerifier() {
			
			@Override
			public boolean verifyBlock(Block block, int x, int y, int z) {
				return ((x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (block.getType().isSolid())) ||
						(x == 1 && y == 1 && z == 1 && block.getType() == Material.CRAFTING_TABLE) ||
						(x == 2 && y == 1 && z == 1 && block.getType() == Material.CHEST) ||
						(x == 3 && y == 1 && z == 1 && block.getType() == Material.OAK_FENCE) ||
						(x == 3 && y == 2 && z == 1 && block.getType() == Material.OAK_PRESSURE_PLATE) ||
						(!(x == 0 || x == getWidth() - 1 || y == 0 || y == getHeight() - 1 || z == 0 || z == getLength() - 1) && (!block.getType().isSolid()));
			}
			
			@Override
			public Material getStartBlock() {
				return Material.CRAFTING_TABLE;
			}
			
			@Override
			public Vector getSize() {
				return new Vector(8, 5, 8);
			}
			
			@Override
			public Vector getOffset() {
				return new Vector(-1, -1, -1);
			}
		}, (loc, hand) -> new Trader (loc, hand), name -> "The trader " + name + " just moved in!");*/
	}

}
