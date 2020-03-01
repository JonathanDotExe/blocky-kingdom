package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.ai.EntityTask;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public class VillagerFollowTask implements EntityTask {
	
	private Player follow;

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return follow != null;
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		return new EntityLocatable(follow);
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		entity.setSpawnPoint(entity.getEntity().getLocation());
	}
	
	@Override
	public void onInteract(CustomEntity<?> entity, PlayerInteractEntityEvent event) {
		EntityTask.super.onInteract(entity, event);
		if (follow == null) {
			follow = event.getPlayer();
			follow.sendMessage(entity.getEntity().getName() + " is now following you!");
		}
		else {
			follow.sendMessage(entity.getEntity().getName() + " is no longer following you!");
			follow = null;
		}
	}
	
}
