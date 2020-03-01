package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.skills.Skill;
import at.jojokobi.blockykingdom.players.skills.SkillHandler;
import at.jojokobi.mcutil.gui.ListGUI;

public class SkillGUI extends ListGUI{
	
	private List<Skill> skills;
	private CharacterStats stats;

	public SkillGUI(Player owner, CharacterStats stats) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 6, "Your Skills"));
		this.stats = stats;
		setItemsPerPage(INV_ROW * 5);
		initGUI();
	}
	
	@Override
	protected void initGUI() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		skills = SkillHandler.getInstance().getItemList();
		getOwner().sendMessage(skills + "/" + skills.size());
		for (Skill skill : skills) {
			ItemStack icon = skill.getIcon();
			ItemMeta meta = icon.getItemMeta();
			int level = stats.getSkillLevel(skill);
			meta.setLore(Arrays.asList("Level " + level, "Cost: " + (level > 0 ? stats.getNeededSkillPoints(level) : skill.getSkillCost()) + " Skill Points"));
			icon.setItemMeta(meta);
			items.add(icon);
		}
		setItems(items);
		getOwner().sendMessage(items + "/" + items.size());
		
		super.initGUI();
		
		fillEmpty(getFiller());
	}

	@Override
	protected void onButtonPress(ItemStack button, ClickType click) {
		int pageIndex = getInventory().first(button);
		if (!checkPageButton(button) && pageIndex < getItemsPerPage() && pageIndex >= 0) {
			int index = getPage() * getItemsPerPage() + pageIndex;
			if (index < skills.size()) {
				Skill skill = skills.get(index);
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
	}

}
