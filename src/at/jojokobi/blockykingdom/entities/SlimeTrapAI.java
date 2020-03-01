package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.ai.TargetAndAttackAI;

@Deprecated
public final class SlimeTrapAI extends TargetAndAttackAI{
	
	private static SlimeTrapAI instance;

	private SlimeTrapAI() {
		
	}

	public static SlimeTrapAI getInstance() {
		if (instance == null) {
			instance = new SlimeTrapAI();
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
