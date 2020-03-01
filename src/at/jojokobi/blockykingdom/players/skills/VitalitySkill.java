package at.jojokobi.blockykingdom.players.skills;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;;

public class VitalitySkill extends Skill implements Listener{

	private long time = 0;
	
	public VitalitySkill(Plugin plugin) {
		super(8, 25, "Vitality");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (Entry<Player,Statable> entry : StatHandler.getInstance().getEntrySet()) {
				int level = entry.getValue().getCharacterStats().getSkillLevel(this);
				if (level > 0 && time %  (20 - level) == 0) {
					entry.getKey().setHealth(Math.min(entry.getKey().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), entry.getKey().getHealth() + 1.0));
				}
			}
			time++;
		}, 20L, 20L);
	}
	
	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.HUMAN;
	}
	
	@Override
	public String getIdentifier() {
		return "vitality";
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.POTION;
	}

}
