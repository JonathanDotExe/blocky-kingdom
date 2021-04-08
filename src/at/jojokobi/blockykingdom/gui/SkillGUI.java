package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
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
	
	@Override
	protected void initGUI() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		skills = SkillHandler.getInstance().getItemList();
		for (Skill skill : skills) {
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
			items.add(icon);
		}
		setItems(items);		
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
