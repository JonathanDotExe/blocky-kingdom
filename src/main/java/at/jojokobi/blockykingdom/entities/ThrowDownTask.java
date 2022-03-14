package at.jojokobi.blockykingdom.entities;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.EntityTask;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class ThrowDownTask implements EntityTask {

	private Locatable goal;
	
	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return !entity.getEntity().getPassengers().isEmpty();
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		entity.setGoal(goal);
		if (entity.reachedGoal()) {
			entity.getEntity().eject();
		}
		return goal;
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		goal = new WrapperLocatable(entity.getEntity().getLocation().add(Math.random() * 40 -20, 40, Math.random() * 40 -20));
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		goal = null;
	}
	
}
