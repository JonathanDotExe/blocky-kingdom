package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.TargetAndAttackAI;

@Deprecated
public final class DeathAngelAI extends TargetAndAttackAI{
	
	private static DeathAngelAI instance;

	private DeathAngelAI() {
		
	}

	public static DeathAngelAI getInstance() {
		if (instance == null) {
			instance = new DeathAngelAI();
		}
		return instance;
	}
	
	@Override
	public void changeAI(CustomEntity<?> entity) {
		super.changeAI(entity);
//		if (entity.getTask() == null) {
//			entity.setTask(new LegacyRandomTask());
//		}
	}

	@Override
	public String getIdentifier() {
		return "death_angel";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

}
