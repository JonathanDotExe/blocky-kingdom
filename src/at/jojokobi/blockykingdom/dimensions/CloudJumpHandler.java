package at.jojokobi.blockykingdom.dimensions;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.JojokobiUtilPlugin;

public class CloudJumpHandler implements Listener {
	
	private static CloudJumpHandler instance;

	private CloudJumpHandler() {
		
	}
	
	public static CloudJumpHandler getInstance() {
		if (instance == null) {
			instance = new CloudJumpHandler();
		}
		return instance;
	}

	private Set<UUID> jumping = new HashSet<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		// Jump Particles
		if (jumping.contains(player.getUniqueId())) {
			player.getWorld().spawnParticle(Particle.SPELL_MOB, player.getEyeLocation(), 10);
//			
//			Vector velocity = player.getVelocity();
//			double y = velocity.getY();
//			velocity.setY(0);
//			if (velocity.lengthSquared() != 0.0) {
//				velocity.normalize();
//				velocity.multiply(2);
//			}
//			velocity.setY(y);
//			player.setVelocity(velocity);
			// Remove
			if (event.getPlayer().isOnGround()) {
				player.setFallDistance(0);
				jumping.remove(player.getUniqueId());
			}
			//Go to heaven
			if (player.getLocation().getY() > 255) {
				jumping.remove(player.getUniqueId());
				JojokobiUtilPlugin util = JavaPlugin.getPlugin(JojokobiUtilPlugin.class);
				World dimWorld = util.getDimensionHandler().getDimensionWorld(event.getPlayer().getWorld(), HeavenDimension.getInstance());
				if (dimWorld != null) {
					System.out.println("Teleporting to " + dimWorld.getName());
					Location place = new Location(dimWorld, player.getLocation().getX(), 150, player.getLocation().getZ());
					place.setY(place.getWorld().getHighestBlockYAt(place));
					if (place.getBlockY() == 0) {
						place.setY(70);
						place.getBlock().setType(Material.GLASS);
						place.add(0, 1, 0);
					}
					player.teleport(place);
					//doSuperJump(player, 10);
				}
			}
		} else if (HeavenDimension.getInstance().isDimension(player.getWorld())) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (to != null) {
				// Push up
				if (to.clone().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& !from.clone().add(0, -0.1, 0).getBlock().getType().isSolid()
						&& event.getPlayer().getFallDistance() > 0.5f) {
					player.setVelocity(player.getVelocity().setY(Math.abs(player.getVelocity().getY() / 3 * 2)));
				}
				// Super jump
				else if (player.isSprinting() && from.clone().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& !to.clone().add(0, -0.1, 0).getBlock().getType().isSolid()) {
					doSuperJump(player, 3);
				}
			}
		}
	}
	
	public void doSuperJump (Player player, double y) {
		Vector velocity = player.getVelocity();
		velocity.setY(0);
		if (velocity.lengthSquared() != 0.0) {
			velocity.normalize();
			velocity.multiply(2);
		}
		velocity.setY(y);
		player.setVelocity(velocity);
		jumping.add(player.getUniqueId());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && (jumping.contains(event.getEntity().getUniqueId())
				|| (event.getEntity().getLocation().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& HeavenDimension.getInstance().isDimension(event.getEntity().getWorld())))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		jumping.remove(event.getPlayer().getUniqueId());
	}

}
