package at.jojokobi.blockykingdom.players.skills;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class TeleportSkill extends Skill {
	
	private static final String IDENTIFIER = "teleport";
	private static final int COOLDOWN = 5000;
	private static final int TELEPORTATION_PERIOD = 2000;
	
	private Map<UUID, TeleportEntry> map = new HashMap<> ();
	
	public TeleportSkill() {
		super(10, 20, "Teleport");
	}
	
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && player.isSprinting() && player.getInventory().getItemInMainHand().getType() == Material.AIR && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			TeleportEntry entry = map.get(player.getUniqueId());
			if (entry == null || entry.getTimestamp() + COOLDOWN < System.currentTimeMillis()) {
				entry = new TeleportEntry(System.currentTimeMillis());
			}
			if (entry.getTimestamp() + TELEPORTATION_PERIOD >= System.currentTimeMillis() && entry.getCount() < level) {
				Vector vector = player.getLocation().getDirection();
				vector.normalize();
				vector.multiply(4);
				player.teleport(player.getLocation().add(vector));
				player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 10);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
				entry.addCount();
			}
			map.put(event.getPlayer().getUniqueId(), entry);
		}
	}
	
	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getProfession() == CharacterProfession.MAGE;
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
		return Material.ENDER_PEARL;
	}
	
	@Override
	public String getDescription() {
		return "You can teleport over little distances when you punch in the air with your bare hands while sprinting!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be a wizard to learn this skill!";
	}

}

class TeleportEntry {
	
	private int count = 0;
	private long timestamp;
	
	public TeleportEntry(long timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void addCount () {
		count++;
	}
	
}
