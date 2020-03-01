package at.jojokobi.blockykingdom.players.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class StompAttackSkill extends Skill {

	private static final String IDENTIFIER = "stomp_skill";
	
	private List<UUID> stomping = new ArrayList<>();

	public StompAttackSkill() {
		super(15, 10, "Stomp Skill");
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && event.isSneaking() && player.getFallDistance() > 5 && player.getFallDistance() < 10 && !stomping.contains(player.getUniqueId())) {
			stomping.add(player.getUniqueId());
			player.setVelocity(new Vector(0, -10 * level, 0));
			player.sendMessage("Wahooo!");
		}
	}
	
	
	@EventHandler
	public void onPlayerDamage (EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && stomping.contains(event.getEntity().getUniqueId())) {
			stomping.remove(event.getEntity().getUniqueId());
			event.setCancelled(true);
			Statable statable = StatHandler.getInstance().getStats(event.getEntity());
			if (statable != null) {
				int level = statable.getCharacterStats().getSkillLevel(this);
				for (Entity entity : event.getEntity().getNearbyEntities(2, 2, 2)) {
					if (entity instanceof Damageable) {
						((Damageable) entity).damage(1 + 4 * level/10.0);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		stomping.remove(event.getPlayer().getUniqueId());
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
		return Material.ANVIL;
	}

}
