package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class ClimbingSkill extends Skill {

	private static final String IDENTIFIER = "climbing";

	public ClimbingSkill() {
		super(8, 20, "Climbing");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && player.isSneaking()
				&& (player.getLocation().add(1, 0, 0).getBlock().getType().isSolid()
						|| player.getLocation().add(-1, 0, 0).getBlock().getType().isSolid()
						|| player.getLocation().add(0, 0, 1).getBlock().getType().isSolid()
						|| player.getLocation().add(0, 0, -1).getBlock().getType().isSolid())) {
			player.setVelocity(player.getVelocity().setY(player.getLocation().getPitch()/-90 * 0.2 * level));
			player.setFallDistance(0);
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character);
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
		return Material.LADDER;
	}

}
