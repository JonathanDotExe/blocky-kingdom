package at.jojokobi.blockykingdom.players.skills;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class CrawlingSkill extends Skill {

	private static final int SNEAK_DELAY = 1000;
	
	private static final String IDENTIFIER = "crawling";
	
	private Map<UUID, Long> timestamps = new HashMap<>();
	private Set<UUID> crawling = new HashSet<>();

	public CrawlingSkill() {
		super(6, 10, "Crawling");
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && event.isSneaking() && event.getPlayer().getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid()) {
			Long timestamp = timestamps.get(event.getPlayer().getUniqueId());
			if (timestamp != null && timestamp + SNEAK_DELAY >= System.currentTimeMillis()) {
				crawling.add(event.getPlayer().getUniqueId());
				event.getPlayer().setGliding(true);
				event.setCancelled(true);
			}
			timestamps.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
		}
		else {
			crawling.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		if (!event.getPlayer().getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid()) {
			crawling.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onPlayerToggleGlide (EntityToggleGlideEvent event) {
		if (!event.isGliding() && crawling.contains(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		timestamps.remove(event.getPlayer().getUniqueId());
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
		return Material.GRASS;
	}
	
	@Override
	public String getDescription() {
		return "Sneak twice to lay down on the ground!";
	}

}
