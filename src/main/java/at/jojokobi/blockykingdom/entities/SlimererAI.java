package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.ai.TargetAndAttackAI;

@Deprecated
public final class SlimererAI extends TargetAndAttackAI{
	
	private static SlimererAI instance;

	private SlimererAI() {
		
	}

	public static SlimererAI getInstance() {
		if (instance == null) {
			instance = new SlimererAI();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "slimerer";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

}
