package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class HungerSkill extends Skill {
	
	private static final String IDENTIFIER = "hunger";
	
	public HungerSkill() {
		super(3, 20, "Hunger");
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Statable stats = StatHandler.getInstance().getStats(player);
		if (stats != null && stats.getCharacterStats().getSkillLevel(this) > 0) {
			player.removePotionEffect(PotionEffectType.HUNGER);
		}
	}
	
//	@EventHandler
//	public void onEntityPotionEffect (EntityPotionEffectEvent event) {
//		event.getEntity().sendMessage("Potion");
//		if (event.getNewEffect() != null && event.getNewEffect().getType() == PotionEffectType.HUNGER) {
//			Entity player = event.getEntity();
//			player.sendMessage("Hunger");
//			Statable stats = StatHandler.getInstance().getStats(player);
//			if (stats != null && stats.getCharacterStats().getSkillLevel(this) > 0) {
//				event.setCancelled(true);
//			}
//		}
//	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.DWARF;
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
		return Material.ROTTEN_FLESH;
	}
	
	@Override
	public String getDescription() {
		return "You can eat rotten flesh without becoming hungry again!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be a dwarf to learn this skill!";
	}

}
