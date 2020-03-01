package at.jojokobi.blockykingdom.entities;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.EntityAI;

@Deprecated
public final class FlyingSheepAI implements EntityAI{
	
	private static FlyingSheepAI instance;

	private FlyingSheepAI() {
		
	}

	public static FlyingSheepAI getInstance() {
		if (instance == null) {
			instance = new FlyingSheepAI();
		}
		return instance;
	}

	@Override
	public String getIdentifier() {
		return "flying_sheep_ai";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public void changeAI(CustomEntity<?> entity) {
		if (!entity.getEntity().getPassengers().isEmpty()) {
//			entity.setTask(new LegacyRidingTask());
		}
	}

}
