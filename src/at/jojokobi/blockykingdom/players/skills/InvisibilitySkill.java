package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class InvisibilitySkill extends Skill {

	private static final String IDENTIFIER = "invisibility";

	public InvisibilitySkill() {
		super(8, 20, "Invisibility");
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && event.isSneaking()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * level, 1, true, false, false));
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getProfession() == CharacterProfession.NINJA;
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
		return Material.PRISMARINE_CRYSTALS;
	}

}
