package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.ai.TargetAndAttackAI;

@Deprecated
public final class ZombieBossAI extends TargetAndAttackAI{
	
	private static ZombieBossAI instance;

	private ZombieBossAI() {
		
	}

	public static ZombieBossAI getInstance() {
		if (instance == null) {
			instance = new ZombieBossAI();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "slime_trap";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

}
