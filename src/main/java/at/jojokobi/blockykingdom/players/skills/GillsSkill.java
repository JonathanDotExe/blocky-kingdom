package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class GillsSkill extends Skill {
	
	private static final String IDENTIFIER = "gills";
	
	public GillsSkill() {
		super(10, 20, "Gills");
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && player.getLocation().getBlock().getType() == Material.WATER) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, level));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, level));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, level));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 60, level));
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.OCEAN_MAN;
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
		return Material.SALMON;
	}
	
	@Override
	public String getDescription() {
		return "You can breathe and move faster under water!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be an ocean man to learn this skill!";
	}

}
