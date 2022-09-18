package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.skills.Skill;
import at.jojokobi.mcutil.gui.ListGUIEntry;

public class SkillEntry implements ListGUIEntry {

	private Skill skill;
	private CharacterStats stats;
	
	private static void splitLines(String str, List<String> lines, int maxChars) {
		String[] words = str.split(" ");
		StringBuilder currLine = new StringBuilder();
		for (String w : words) {
			if (currLine.length() <= 0) {
				currLine.append(w);
			}
			else if (currLine.length() + w.length() + 1 < maxChars){
				currLine.append(" ");
				currLine.append(w);
			}
			else {
				lines.add(currLine.toString());
				currLine = new StringBuilder(w);
			}
		}
		
		if (currLine.length() > 0) {
			lines.add(currLine.toString());
		}
	}
	
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public CharacterStats getStats() {
		return stats;
	}
	public void setStats(CharacterStats stats) {
		this.stats = stats;
	}
	@Override
	public ItemStack getIcon() {
		ItemStack icon = skill.getIcon();
		ItemMeta meta = icon.getItemMeta();
		int level = stats.getSkillLevel(skill);
		
		List<String> lore = new ArrayList<String>();
		lore.add("Level " + level);
		lore.add("Cost: " + (level > 0 ? stats.getNeededSkillPoints(level) : skill.getSkillCost()) + " Skill Points");
		lore.add("Needed Level: " + skill.getMinLevel());
		splitLines(skill.getDescription(), lore, 30);
		splitLines(skill.getRequirementsDescription(), lore, 30);
		meta.setLore(lore);
		icon.setItemMeta(meta);
		return icon;
	}

	public SkillEntry(Skill skill, CharacterStats stats) {
		super();
		this.skill = skill;
		this.stats = stats;
	}
	
}
