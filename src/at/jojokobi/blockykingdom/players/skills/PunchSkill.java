package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class PunchSkill extends Skill {

	private static final String IDENTIFIER = "punch";

	public PunchSkill() {
		super(5, 15, "Punch");
	}
	
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		Statable character = StatHandler.getInstance().getStats(event.getDamager());
		if (character != null) {
			CharacterStats stats = character.getCharacterStats();
			int level = stats.getSkillLevel(this);
			if (level > 0 && event.getDamager() instanceof LivingEntity && ((LivingEntity) event.getDamager()).getEquipment().getItemInMainHand() == null) {
				event.setDamage(event.getDamage() * (2 + level/3.0));
			}
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.GIANT;
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
		return Material.PORKCHOP;
	}

}
