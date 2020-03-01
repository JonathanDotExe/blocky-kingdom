package at.jojokobi.blockykingdom.summoning;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class VillagerHousePattern implements SummoningPattern {
	
	private EntityHandler handler;
	private GenerationHandler genHandler;
	private Structure structure;
	private HouseVerifier verifier;
	private BiFunction<Location, EntityHandler, KingdomVillager<?>> villagerFunction;
	private Function<String, String> messageFunction;

	public VillagerHousePattern(EntityHandler handler, GenerationHandler genHandler, Structure structure,
			HouseVerifier verifier, BiFunction<Location, EntityHandler, KingdomVillager<?>> villagerFunction,
			Function<String, String> messageFunction) {
		super();
		this.handler = handler;
		this.genHandler = genHandler;
		this.structure = structure;
		this.verifier = verifier;
		this.villagerFunction = villagerFunction;
		this.messageFunction = messageFunction;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = KingdomHandler.getInstance().getKingdom(event.getBlock().getLocation()) != null;
		double width = verifier.getSize().getX();
		double height = verifier.getSize().getY();
		double length = verifier.getSize().getZ();
		//Start Block
		if (event.getBlock().getType() == verifier.getStartBlock()) {
			Location loc = event.getBlock().getLocation().add(verifier.getOffset());
			for (int x = 0; x < width && matches; x++) {
				for (int y = 0; y < height && matches; y++) {
					for (int z = 0; z < length && matches; z++) {
						Block block = loc.clone().add(x, y, z).getBlock();
						matches = verifier.verifyBlock(block, x, y, z);
					}
				}
			}
		}
		else {
			matches = false;
		}
		return matches;
	}

	@Override
	public void summon(BlockPlaceEvent event) {
		KingdomPoint point = new KingdomPoint(event.getBlock().getLocation());
		KingdomVillager<?> villager = villagerFunction.apply(event.getBlock().getLocation().add(1, 1, 1), handler);
		if (point.toKingdom() != null && point.canAddVillager(villager.getVillagerCategory(), handler)) {
			handler.addSavedEntity(villager);
			point.addVillager(villager);
			event.getPlayer().sendMessage(messageFunction.apply(villager.getName()));
			//Spawn house
			Location start = event.getBlock().getLocation().add(-1, -1, -1);
			StructureInstance<?> inst = structure.getStandardInstance(start);
			genHandler.addStructureInstance(inst);
		}
	}
	
	
	interface HouseVerifier {
		
		public boolean verifyBlock (Block block, int x, int y, int z);
		
		public Vector getSize ();
		
		public Vector getOffset ();
		
		public Material getStartBlock ();
		
		public default int getWidth () {
			return getSize().getBlockX();
		}
		
		public default int getHeight () {
			return getSize().getBlockX();
		}
		
		public default int getLength () {
			return getSize().getBlockX();
		}
		
	}

}
