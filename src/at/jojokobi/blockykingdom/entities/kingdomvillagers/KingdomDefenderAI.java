package at.jojokobi.blockykingdom.entities.kingdomvillagers;


import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.EntityAI;

@Deprecated
public class KingdomDefenderAI implements EntityAI{
	
	private static KingdomDefenderAI instance;
	
	public static KingdomDefenderAI getInstance() {
		if (instance == null) {
			instance = new KingdomDefenderAI();
		}
		return instance;
	}
	
	private KingdomDefenderAI() {
		
	}

	@Override
	public String getIdentifier() {
		return "kingdom_defender";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public void changeAI(CustomEntity<?> entity) {
//		if (entity instanceof Targeter && (entity.getTask() == null || entity.getTask() instanceof LegacyGoalTask)) {
//			Targeter targeter = (Targeter) entity;
//			
//			//Look for enemy
//			List<Entity> enemies = new ArrayList<>();
//			for (Entity e : entity.getEntity().getNearbyEntities(20, 20, 20)) {
//				if (targeter.isTarget(e)) {
//					enemies.add(e);
//				}
//			}
//			
//			if (!enemies.isEmpty()) {
//				entity.setTask(new LegacyAttackTask(enemies.get(new Random().nextInt(enemies.size()))));
//			}
//		}
//		if (entity.getTask() == null) {
//			entity.setTask(new LegacyGoalTask(entity.getSpawnPoint()));
//		}
	}

}
