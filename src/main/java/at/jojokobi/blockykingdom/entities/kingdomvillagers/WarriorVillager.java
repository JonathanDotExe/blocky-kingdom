package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.Targeter;

public abstract class WarriorVillager<T extends LivingEntity> extends KingdomVillager<T> implements Targeter, Attacker{

	public WarriorVillager(Location place, EntityHandler handler, Random random, CustomEntityType<?> type) {
		super(place, handler, random, type);
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		super.onInteract(event);
	}

	@Override
	public boolean isTarget(Entity entity) {
		CustomEntity<?> custom = getHandler().getCustomEntityForEntity(entity);
		//Attack monsters, villagers of other kingdoms and players if kingdom is evil or no kingdom is set at all
		return ((entity instanceof Monster || entity instanceof Phantom || entity instanceof Ghast) && !(entity instanceof Creeper) && !(entity instanceof PigZombie)
				&& (getKingdomPoint() == null || !(custom instanceof KingdomVillager<?>)
						|| !getKingdomPoint().equals(((KingdomVillager<?>) custom).getKingdomPoint())))
				|| ((getKingdomPoint() == null
						|| KingdomHandler.getInstance().getKingdom(getKingdomPoint()).getState() == KingdomState.EVIL)
						&& entity instanceof Player);
	}

}
