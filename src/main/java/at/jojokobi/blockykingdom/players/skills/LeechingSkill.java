package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class LeechingSkill extends Skill {

	private static final String IDENTIFIER = "leeching";

	public LeechingSkill() {
		super(8, 20, "Leeching");
	}
	
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Damageable && event.getDamager() instanceof LivingEntity && Math.round(((Damageable) event.getEntity()).getHealth() - event.getFinalDamage()) <= 0) {
			Statable character = StatHandler.getInstance().getStats(event.getDamager());
			if (character != null) {
				CharacterStats stats = character.getCharacterStats();
				LivingEntity damager = (LivingEntity) event.getDamager();
				int level = stats.getSkillLevel(this);
				if (level > 0 && Math.random() < 0.1 * level) {
					damager.setHealth(Math.min(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), damager.getHealth() + 2.0));
					event.getEntity().getWorld().spawnParticle(Particle.HEART, event.getEntity().getLocation().add(0, 1, 0), 3);
				}
			}
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.VAMPIRE;
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
		return Material.REDSTONE;
	}
	
	@Override
	public String getDescription() {
		return "You have a chance to heal yourself when you damage another entity!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be a vampire to learn this skill!";
	}

}
