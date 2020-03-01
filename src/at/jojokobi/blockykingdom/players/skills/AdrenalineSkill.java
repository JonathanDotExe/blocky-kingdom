package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class AdrenalineSkill extends Skill{
	
	public static final String IDENTIFIER = "adrenaline";
	private static final double HEALTH_LIMIT = 3.0;

	public AdrenalineSkill() {
		super(12, 20, "Adrenaline");
	}
	
	@EventHandler (priority=EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		Statable statable = StatHandler.getInstance().getStats(event.getEntity());
		if (statable != null && event.getEntity() instanceof LivingEntity) {
			CharacterStats stats = statable.getCharacterStats();
			int level = stats.getSkillLevel(this);
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (level > 0 && entity.getHealth() >  HEALTH_LIMIT && entity.getHealth() - event.getFinalDamage() <= HEALTH_LIMIT) {
				entity.addPotionEffect(new PotionEffect(getPotionEffectType(stats.getProfession()), 5 * 20 + level * 10, 2));
			}
		}
	}
	
	private static PotionEffectType getPotionEffectType (CharacterProfession profession) {
		PotionEffectType type = null;
		switch (profession) {
		case ADVENTURER:
		case KNIGHT:
		case MAGE:
		case NINJA:
			type = PotionEffectType.INCREASE_DAMAGE;
			break;
		case SMITH:
		case SUMO:
		case VILLAGER:
			type = PotionEffectType.DAMAGE_RESISTANCE;
			break;
		}
		return type;
	}
	
	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getProfession() == CharacterProfession.SUMO || character.getProfession() == CharacterProfession.KNIGHT;
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
		return Material.POTION;
	}

}
