package at.jojokobi.blockykingdom.players.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class RageSkill extends Skill {
	
	private static final String IDENTIFIER = "rage";
	private static final long RAGE_DURATION = 3000;
	
	private Map<UUID, Long> timestamps = new HashMap<>();
	
	public RageSkill() {
		super(5, 15, "Rage");
	}
	
	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		Statable stats = StatHandler.getInstance().getStats(event.getEntity());
		if (stats != null) {
			int level = stats.getCharacterStats().getSkillLevel(this);
			
			if (level > 0) {
				timestamps.put(event.getEntity().getUniqueId(), System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		Long timestamp = timestamps.get(event.getDamager().getUniqueId());
		Statable stats = StatHandler.getInstance().getStats(event.getDamager());
		if (timestamp != null && stats != null && timestamp + RAGE_DURATION >= System.currentTimeMillis()) {
			int level = stats.getCharacterStats().getSkillLevel(this);
			
			event.setDamage(event.getFinalDamage() + 1.0 + level * 0.75);
			event.getEntity().getWorld().spawnParticle(Particle.VILLAGER_ANGRY, event.getEntity().getLocation().add(Math.random() - 0.5, 1, Math.random() - 0.5), 1);
			event.getEntity().getWorld().spawnParticle(Particle.VILLAGER_ANGRY, event.getEntity().getLocation().add(Math.random() - 0.5, 1, Math.random() - 0.5), 1);
			event.getEntity().getWorld().spawnParticle(Particle.VILLAGER_ANGRY, event.getEntity().getLocation().add(Math.random() - 0.5, 1, Math.random() - 0.5), 1);
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.BARBARIAN;
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		timestamps.remove(event.getPlayer().getUniqueId());
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
		return Material.FIRE_CHARGE;
	}

}
