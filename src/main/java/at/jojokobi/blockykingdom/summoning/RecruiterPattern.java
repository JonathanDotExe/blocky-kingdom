package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.Recruiter;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

@Deprecated
public class RecruiterPattern implements SummoningPattern {
	
	private static final int WIDTH = 8;
	private static final int HEIGHT = 5;
	private static final int LENGTH = 8;
	
	private EntityHandler handler;
	private GenerationHandler genHandler;
	private Structure structure;

	public RecruiterPattern(EntityHandler handler, GenerationHandler genHandler, Structure structure) {
		super();
		this.handler = handler;
		this.genHandler = genHandler;
		this.structure = structure;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = KingdomHandler.getInstance().getKingdom(event.getBlock().getLocation()) != null;
		if (event.getBlock().getType() == Material.ANVIL) {
			Location loc = event.getBlock().getLocation().add(-1, -1, -1);
			for (int x = 0; x < WIDTH && matches; x++) {
				for (int y = 0; y < HEIGHT && matches; y++) {
					for (int z = 0; z < LENGTH && matches; z++) {
						Block block = loc.clone().add(x, y, z).getBlock();
						matches = ((x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1 || z == 0 || z == LENGTH - 1) && (block.getType().isSolid())) ||
								(x == 1 && y == 1 && z == 1 && block.getType() == Material.ANVIL) ||
								(x == 2 && y == 1 && z == 1 && block.getType() == Material.CRAFTING_TABLE) ||
								(x == 3 && y == 1 && z == 1 && block.getType() == Material.FURNACE) ||
								(!(x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1 || z == 0 || z == LENGTH - 1) && (!block.getType().isSolid()));
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
		Recruiter recruiter = new Recruiter(event.getBlock().getLocation().add(1, 1, 1), handler);
		if (point.toKingdom() != null && point.canAddVillager(recruiter.getVillagerCategory(), handler)) {
			point.addVillager(recruiter);
			handler.addSavedEntity(recruiter);
			event.getPlayer().sendMessage("The recruiter " + recruiter.getName() + " just moved in!");
			//Spawn house
			Location start = event.getBlock().getLocation().add(-1, -1, -1);
			StructureInstance<?> inst = structure.getStandardInstance(start);
			genHandler.addStructureInstance(inst);
		}
	}

}
