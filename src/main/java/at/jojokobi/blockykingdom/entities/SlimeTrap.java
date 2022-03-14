package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;

public class SlimeTrap extends CustomEntity<Slime> implements Targeter, Attacker {
	
	public SlimeTrap(Location place, EntityHandler handler) {
		super(place, handler, null);
//		setAi(SlimeTrapAI.getInstance());
		addEntityTask(new AttackTask(Player.class));
	}

	@Override
	protected Slime createEntity(Location place) {
		Slime slime = place.getWorld().spawn(place, Slime.class);
		slime.setSize(1);
		slime.setCustomName("It's a trap!");
		
		slime.setAI(false);
		slime.setGravity(false);
		return slime;
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<>());
	}

	@Override
	public boolean isTarget(Entity entity) {
		return entity instanceof Player;
	}

	@Override
	public void attack(Damageable entity) {
		//Check if is placed
		if (getEntity().getVehicle() == null) {
			Vector distance = getEntity().getLocation().subtract(entity.getLocation()).toVector();
			if (distance.lengthSquared() > 3 * 3) {
				entity.setVelocity(distance.multiply(0.3));
			}
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).setSwimming(true);
			}
		}
	}

	@Override
	public int getAttackDelay() {
		return 1;
	}
	
	@Override
	public double getAttackRange() {
		return 5;
	}
	
	public static SlimeTrap deserialize (Map<String, Object> map) {
		SlimeTrap entity = new SlimeTrap(null, null);
		entity.load(map);
		return entity;
	}
	
	public static SlimeTrap launchSlimeTrap (ProjectileSource source, Vector velocity, EntityHandler handler) {
		Snowball ball = source.launchProjectile(Snowball.class, velocity);
		SlimeTrap trap = new SlimeTrap(ball.getLocation(), handler);
		handler.addEntity(trap);
		ball.addPassenger(trap.getEntity());
		return trap;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
}
