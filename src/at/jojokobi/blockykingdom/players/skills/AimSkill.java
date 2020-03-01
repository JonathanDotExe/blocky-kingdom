package at.jojokobi.blockykingdom.players.skills;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class AimSkill extends Skill {
	
	private static final String IDENTIFIER = "aim";
	
	private List<UUID> shotArrows = new ArrayList <>();
	
	public AimSkill(Plugin plugin) {
		super(15, 20, "Aim");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (Iterator<UUID> iterator = shotArrows.iterator(); iterator.hasNext();) {
				UUID uuid = iterator.next();
				Entity entity = Bukkit.getEntity(uuid);
				if (entity instanceof Arrow && !((Arrow) entity).isInBlock()) {
					Arrow arrow = (Arrow) entity;
					Statable stats = StatHandler.getInstance().getStats(arrow.getShooter());
					if (stats != null) {
						int level = stats.getCharacterStats().getSkillLevel(this);
						if (level > 0) {
							double range = level/10.0 * 6;
							boolean found = false;
							for (Iterator<Entity> iterator2 = arrow.getNearbyEntities(range, range, range).iterator(); iterator2.hasNext() && !found;) {
								Entity e = iterator2.next();
								if (e instanceof Monster) {
									//Target found
									found = true;
									Vector dir = e.getLocation().subtract(arrow.getLocation()).toVector();
									double length = dir.length();
									if (length != 0) {
										dir.normalize();
										dir.multiply(length);
									}
									arrow.setVelocity(dir);
								}
							}
						}
					}
				}
				else {
					iterator.remove();
				}
			}
		}, 5, 5);
	}
	
	@EventHandler
	public void onProjectileLaunch (ProjectileLaunchEvent event) {
		Statable stats = StatHandler.getInstance().getStats(event.getEntity().getShooter());
		if (event.getEntity() instanceof Arrow && stats != null && stats.getCharacterStats().getSkillLevel(this) > 0) {
			shotArrows.add(event.getEntity().getUniqueId());
		}
	}
	
//	@EventHandler
//	public void onProjectileHit (ProjectileHitEvent event) {
//		if (event.getHitEntity() == null && event.getEntity() instanceof Arrow) {
//			Statable stats = StatHandler.getInstance().getStats(event.getEntity().getShooter());
//			if (stats != null) {
//				int level = stats.getCharacterStats().getSkillLevel(this);
//				
//				if (level > 0) {
//					if (event.getEntity().getShooter() instanceof CommandSender) {
//						((CommandSender) event.getEntity().getShooter()).sendMessage("Aim Skill Here");
//					}
//					double range = level/10.0 * 6;
//					boolean found = false;
//					for (Iterator<Entity> iterator = event.getEntity().getNearbyEntities(range, range, range).iterator(); iterator.hasNext() && !found;) {
//						Entity entity = iterator.next();
//						if (entity instanceof Monster) {
//							found = true;
//							Arrow
//							Vector dir = entity.getLocation().subtract(event.getEntity().getLocation()).toVector();
//							if (dir.lengthSquared() != 0) {
//								dir.normalize();
//								dir.multiply(1 + range);
//							}
//							event.getEntity().setVelocity(dir);
//							if (event.getEntity().getShooter() instanceof CommandSender) {
//								((CommandSender) event.getEntity().getShooter()).sendMessage("Auto Aiming: " + dir);
//							}
//						}
//					}
//				}
//			}
//		}
//	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.ELF;
	}
	
	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.ARROW;
	}

}
