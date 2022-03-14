package at.jojokobi.blockykingdom.entities;

import org.bukkit.Location;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.LegacyGoalTask;
@Deprecated
public class LegacyThrowDownTask extends LegacyGoalTask{

	public LegacyThrowDownTask(Location goal) {
		super(goal);
	}
	
	@Override
	public void tick(CustomEntity<?> entity) {
		super.tick(entity);
		
		if (entity.reachedGoal() || entity.getEntity().getPassengers().isEmpty()) {
			entity.getEntity().eject();
//			entity.setTask(null);
			entity.setGoal(null);
		}
	}	

}
