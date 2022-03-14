package at.jojokobi.blockykingdom.summoning;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;

@Deprecated
public class TraderPattern implements SummoningPattern {
	
	private static final int WIDTH = 8;
	private static final int HEIGHT = 5;
	private static final int LENGTH = 8;
	
	private EntityHandler handler;

	public TraderPattern(EntityHandler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public boolean matches(BlockPlaceEvent event) {
		boolean matches = KingdomHandler.getInstance().getKingdom(event.getBlock().getLocation()) != null;
		if (event.getBlock().getType() == Material.CRAFTING_TABLE) {
			Location loc = event.getBlock().getLocation().add(-1, -1, -1);
			for (int x = 0; x < WIDTH && matches; x++) {
				for (int y = 0; y < HEIGHT && matches; y++) {
					for (int z = 0; z < LENGTH && matches; z++) {
						Block block = loc.clone().add(x, y, z).getBlock();
						matches = ((x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1 || z == 0 || z == LENGTH - 1) && (block.getType().isSolid())) ||
								(x == 1 && y == 1 && z == 1 && block.getType() == Material.CRAFTING_TABLE) ||
								(x == 2 && y == 1 && z == 1 && block.getType() == Material.CHEST) ||
								(x == 3 && y == 1 && z == 1 && block.getType() == Material.OAK_FENCE) ||
								(x == 3 && y == 2 && z == 1 && block.getType() == Material.OAK_PRESSURE_PLATE) ||
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
		Trader trader = new Trader(event.getBlock().getLocation().add(1, 1, 1), handler);
		if (point.toKingdom() != null && point.canAddVillager(trader.getVillagerCategory(), handler)) {
			point.addVillager(trader);
			handler.addSavedEntity(trader);
			event.getPlayer().sendMessage("The trader " + trader.getName() + " just moved in!");
		}
	}

}
