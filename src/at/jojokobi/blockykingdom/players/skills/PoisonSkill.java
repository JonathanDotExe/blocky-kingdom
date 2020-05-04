package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class PoisonSkill extends Skill implements Listener {

	public static final String IDENTIFIER = "poison";
	
	public PoisonSkill() {
		super(10, 20, "Poison");
	}
	
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		Statable stats = StatHandler.getInstance().getStats(event.getDamager());
		if (stats != null) {
			int level = stats.getCharacterStats().getSkillLevel(this);
			if (level > 0 && event.getEntity() instanceof LivingEntity && Math.random() < 0.25 + 0.05 * level) {
				((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * level/2, 1));
				event.getEntity().getWorld().spawnParticle(Particle.SLIME, event.getEntity().getLocation().add(0, 1, 0), 10);
			}
		}
	}
	
	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.LIZARDMAN;
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
		return Material.POISONOUS_POTATO;
	}
	
	@Override
	public String getDescription() {
		return "Your enemies get the wither effect when you hit them!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be a lizardman to learn this skill!";
	}

}
