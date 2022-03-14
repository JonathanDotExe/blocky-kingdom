package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.ai.TargetAndAttackAI;
@Deprecated
public final class AirheadAI extends TargetAndAttackAI{
	
	private static AirheadAI instance;

	private AirheadAI() {
		
	}

	public static AirheadAI getInstance() {
		if (instance == null) {
			instance = new AirheadAI();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "airhead";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

}
