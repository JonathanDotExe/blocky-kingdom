package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;

public class WallJumpSkill extends Skill {

	private static final String IDENTIFIER = "wall_jump";

	public WallJumpSkill() {
		super(5, 10, "Wall Jump");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		/*Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK
				&& event.getClickedBlock().getLocation().getY() >= player.getLocation().getY()
				&& event.getClickedBlock().getLocation().getY() <= player.getLocation().getY() + player.getHeight()
				&& player.getLocation().add(0, -0.1, 0).getBlock().getType().isAir()) {
			CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
			int level = stats.getSkillLevel(this);
			if (level > 0) {
//				Location place = player.getLocation();
//				place.setYaw(place.getYaw() + 180);
//				player.teleport(place);
				Vector velocity = player.getLocation().getDirection().setY(0).normalize();
				velocity.setY(-0.3);
				velocity.multiply(-(1 + level/10.0));
				player.setVelocity(velocity);
			}
		}*/
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
		return Material.LEATHER_BOOTS;
	}
	
	@Override
	public String getDescription() {
		return "Punch a wall while youR'e in the air to jump off!";
	}

}
