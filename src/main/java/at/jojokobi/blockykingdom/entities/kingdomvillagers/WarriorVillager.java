package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
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
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
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
		//Check if player owns kingdom
		Statable s = StatHandler.getInstance().getStats(event.getPlayer());
		if (getReloadTime() > 0 && getKingdomPoint() == null && KingdomHandler.getInstance().getKingdom(getEntity().getLocation()).getOwners().contains(event.getPlayer().getUniqueId())) {
			//TODO make some movement logic to bring villager to other kingdom
			if (event.getPlayer().isSneaking()) {
				if (s != null && s.getCharacterStats().getMoney() >= getPrice()) {
					//Buy it
					s.getCharacterStats().setMoney(s.getCharacterStats().getMoney() - getPrice());
					setKingdomPoint(new KingdomPoint(getEntity().getLocation()));
					setSave(true);
					setReloadTime(0);
				}
				else {
					event.getPlayer().sendMessage("You don't have enough money!");
				}
			}
			else {
				event.getPlayer().sendMessage("You can buy me for " + getPrice() + "$ if you sneak-click me!");
			}
			if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BREAD) {
				event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
				getEntity().setHealth(Math.min(getEntity().getHealth() + 4.0, getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
				addHappiness(0.3);
				event.getPlayer().sendMessage("[" + getName() + "] Thank you the bread was delicous!");
				getEntity().getEyeLocation().getWorld().spawnParticle(Particle.HEART, getEntity().getEyeLocation(), 5);
			}
			event.getPlayer().sendMessage("[" + getName() + "] my happiness value is currently " + getHappiness() + "!");
		}
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
