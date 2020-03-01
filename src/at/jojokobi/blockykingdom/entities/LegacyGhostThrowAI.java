package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.EntityAI;

@Deprecated
public final class LegacyGhostThrowAI implements EntityAI{
	
	private static LegacyGhostThrowAI instance;

	private LegacyGhostThrowAI() {
		
	}

	public static LegacyGhostThrowAI getInstance() {
		if (instance == null) {
			instance = new LegacyGhostThrowAI();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "ghost_throw";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public void changeAI(CustomEntity<?> entity) {
//		if (entity.getTask() == null) {
//			if (entity.getEntity().getPassengers().isEmpty()) {
//				//Follow Player
//				List<Player> enemies = new ArrayList<>();
//				for (Entity e : entity.getEntity().getNearbyEntities(20, 20, 20)) {
//					if (e instanceof Player && e.getLocation().add(0, -1, 0).getBlock().getType().isSolid()) {
//						enemies.add((Player) e);
//					}
//				}
//				
//				if (!enemies.isEmpty()) {
//					entity.setTask(new LegacyAttackTask(enemies.get(new Random().nextInt(enemies.size()))));
//				}
//			}
//			else {
//				//Rise up
//				entity.setTask(new ThrowDownTask(entity.getEntity().getLocation().add(Math.random() * 40 -20, 40, Math.random() * 40 -20)));
//			}
//		}
	}

}
