package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.event.Listener;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.Iconable;
import at.jojokobi.mcutil.Identifiable;

public abstract class Skill implements Listener, Identifiable, Iconable{
	
	private int minLevel;
	private int skillCost;
	
	private String name;
//	private StatHandler statHandler;
	
	public Skill(int minLevel, int skillCost, String name) {
		super();
		this.minLevel = minLevel;
		this.skillCost = skillCost;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public void setSkillCost(int skillCost) {
		this.skillCost = skillCost;
	}

	public boolean canLearn (CharacterStats character) {
		return character.getLevel() >= minLevel;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int getSkillCost() {
		return skillCost;
	}
	
}
