package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.skills.Skill;
import at.jojokobi.blockykingdom.players.skills.SkillHandler;
import at.jojokobi.mcutil.gui.ListGUI;

public class SkillGUI extends ListGUI<SkillEntry>{
	
	private CharacterStats stats;

	public SkillGUI(Player owner, CharacterStats stats) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 6, "Your Skills"));
		this.stats = stats;
		setItemsPerPage(INV_ROW * 5);
		List<SkillEntry> entries = new ArrayList<>();
		for (Skill skill : SkillHandler.getInstance().getItemList()) {
			entries.add(new SkillEntry(skill, stats));
		}
		setItems(entries);
		initGUI();
	}
	
	@Override
	protected void initGUI() {
		super.initGUI();
		fillEmpty(getFiller());
	}


	@Override
	protected void clickItem(SkillEntry item, int index, ClickType click) {
		Skill skill = item.getSkill();
		int level = stats.getSkillLevel(skill);
		//Buy
		if (level <= 0 && skill.canLearn(stats) && skill.getSkillCost() <= stats.getSkillPoints()) {
			stats.setSkill(skill, 1);
			stats.setSkillPoints(stats.getSkillPoints() - skill.getSkillCost());
		}
		//Power up
		else if (skill.canLearn(stats) && stats.getNeededSkillPoints(level) <= stats.getSkillPoints() && level < CharacterStats.MAX_SKILL_LEVEL) {
			stats.setSkillPoints(stats.getSkillPoints() - stats.getNeededSkillPoints(level));
			stats.setSkill(skill, level + 1);
		}
		initGUI();
	}

}
