package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class HeavinessSkill extends Skill {

	private static final String IDENTIFIER = "heaviness";

	public HeavinessSkill() {
		super(5, 10, "Heaviness");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0) {
			event.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(level/10.0);
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
		return Material.IRON_BLOCK;
	}

}
