package at.jojokobi.blockykingdom.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.players.quests.IQuest;
import at.jojokobi.blockykingdom.players.skills.Skill;
import at.jojokobi.blockykingdom.players.skills.SkillHandler;
import at.jojokobi.blockykingdom.players.skills.SkillInstance;
import at.jojokobi.mcutil.VectorUtil;

public class CharacterStats implements ConfigurationSerializable{
	
	public static final int MAX_SKILL_LEVEL = 10;
	
	public static final String PROFESSION_KEY = "profession";
	public static final String SPECIES_KEY = "species";
	
	public static final String ATTACK_KEY = "attack";
	public static final String DEFENSE_KEY = "defense";
	public static final String MAGIC_KEY = "magic";
	public static final String HEALTH_KEY = "health";
	public static final String SPEED_KEY = "speed";
	
	public static final String LEVEL_KEY = "level";
	public static final String XP_KEY = "xp";
	public static final String SKILL_POINTS_KEY = "skillPoints";
	public static final String MONEY_KEY = "money";
	
	public static final String SKILLS_KEY = "skills";
	public static final String QUESTS_KEY = "quests";
	
	private CharacterProfession profession;
	private CharacterSpecies species;
	
	private int attack;
	private int defense;
	private int magic;
	private int health;
	private int speed;
	
	private int level;
	private int xp;
	private int skillPoints;
	private int money;
	
	private Map<Skill, Integer> skills = new HashMap<>();
	private List<IQuest> quests = new ArrayList<>();
	
	public CharacterStats(CharacterProfession profession, CharacterSpecies species) {
		super();
		this.profession = profession;
		this.species = species;
		
		profession.addValues(this);
	}
	
	public CharacterStats() {
		this(CharacterProfession.VILLAGER, CharacterSpecies.HUMAN);
	}
	
	@Override
	public String toString() {
		return "CharacterStats [profession=" + profession + ", species=" + species + ", attack=" + attack + ", defense="
				+ defense + ", magic=" + magic + ", health=" + health + ", speed=" + speed + "]";
	}

	public double getAttackDamageMultiplier () {
//		return Math.pow(1.08, getTotalAttack());
		return VectorUtil.interpolate(1, 2,getTotalAttack()/10.0);
	}
	
	public double getDefenseDamageMultiplier () {
//		return Math.pow(0.93, getTotalDefense());
		return VectorUtil.interpolate(1, 0.5,getTotalAttack()/10.0);
	}
	
	public double getSpeedMultiplier () {
		return Math.pow(1.05, getTotalSpeed());
	}
	
	public int getXpToNextLevel () {
		return 100 * (getLevel() + 1);
	}
	
	public int getNeededSkillPoints (int skillLevel) {
		return (skillLevel + 1) * (skillLevel + 1);
	}
	
	public int getTotalAttack () {
		return getAttack() + species.getAttackOffset();
	}
	
	public int getTotalDefense () {
		return getDefense() + species.getDefenseOffset();
	}
	
	public int getTotalSpeed () {
		return getSpeed() + species.getSpeedOffset();
	}
	
	public int getTotalMagic () {
		return getMagic() + species.getMagicOffset();
	}
	
	public int getTotalHealth () {
		return getHealth() + species.getHealthOffset();
	}
	
	public int getAttack() {
		return attack;
	}
	
	public boolean doAttackPowerUp () {
		boolean done = false;
		if (getAttack() < MAX_SKILL_LEVEL && getSkillPoints() >= getNeededSkillPoints(getAttack())) {
			setSkillPoints(getSkillPoints() - getNeededSkillPoints(getAttack()));
			setAttack(getAttack() + 1);
			done = true;
		}
		return done;
	}
	
	public boolean doDefensePowerUp () {
		boolean done = false;
		if (getDefense() < MAX_SKILL_LEVEL && getSkillPoints() >= getNeededSkillPoints(getDefense())) {
			setSkillPoints(getSkillPoints() - getNeededSkillPoints(getDefense()));
			setDefense(getDefense() + 1);
			done = true;
		}
		return done;
	}
	
	public boolean doSpeedPowerUp () {
		boolean done = false;
		if (getSpeed() < MAX_SKILL_LEVEL && getSkillPoints() >= getNeededSkillPoints(getSpeed())) {
			setSkillPoints(getSkillPoints() - getNeededSkillPoints(getSpeed()));
			setSpeed(getSpeed() + 1);
			done = true;
		}
		return done;
	}
	
	public boolean doMagicPowerUp () {
		boolean done = false;
		if (getMagic() < MAX_SKILL_LEVEL && getSkillPoints() >= getNeededSkillPoints(getMagic())) {
			setSkillPoints(getSkillPoints() - getNeededSkillPoints(getMagic()));
			setMagic(getMagic() + 1);
			done = true;
		}
		return done;
	}
	
	public boolean doHealthPowerUp () {
		boolean done = false;
		if (getHealth() < MAX_SKILL_LEVEL && getSkillPoints() >= getNeededSkillPoints(getHealth())) {
			setSkillPoints(getSkillPoints() - getNeededSkillPoints(getHealth()));
			setHealth(getHealth() + 1);
			done = true;
		}
		return done;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public CharacterProfession getProfession() {
		return profession;
	}

	public CharacterSpecies getSpecies() {
		return species;
	}

	public void setProfession(CharacterProfession profession) {
		this.profession = profession;
	}

	public void setSpecies(CharacterSpecies species) {
		this.species = species;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}
	
	public void gainXp (int gain) {
		xp += gain;
		while (xp >= getXpToNextLevel()) {
			xp -= getXpToNextLevel();
			level++;
			skillPoints += level;
		}
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public void setSkill (Skill skill, int level) {
		skills.put(skill, level);
	}
	
	public int getSkillLevel (Skill skill) {
		return skills.get(skill) == null ? 0 : skills.get(skill);
	}
	
	public boolean addQuest (IQuest quest, Player player) {
		if (quests.size() < 3) {
			quest.initQuest(player);
			quests.add(quest);
			return true;
		}
		else {
			return false;
		}
	}

	public List<IQuest> getQuests() {
		return new ArrayList<>(quests);
	}
	
	public void removeQuest (IQuest quest) {
		quests.remove(quest);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put(PROFESSION_KEY, getProfession().toString());
		map.put(SPECIES_KEY, getSpecies().toString());
		
		map.put(ATTACK_KEY, getAttack());
		map.put(DEFENSE_KEY, getDefense());
		map.put(MAGIC_KEY, getMagic());
		map.put(SPEED_KEY, getSpeed());
		map.put(HEALTH_KEY, getHealth());
		
		map.put(LEVEL_KEY, getLevel());
		map.put(XP_KEY, getXp());
		map.put(SKILL_POINTS_KEY, getSkillPoints());
		map.put(MONEY_KEY, getMoney());
		
		//Skill List
		ArrayList<SkillInstance> saveSkills = new ArrayList<>();
		for (Skill skill : skills.keySet()) {
			saveSkills.add(new SkillInstance(skill.getIdentifier(), skill.getNamespace(), skills.get(skill)));
		}
		
		map.put(SKILLS_KEY, saveSkills);
		map.put(QUESTS_KEY, quests);
		
		return map;
	}
	
	public static CharacterStats valueOf (Map<String, Object> map) {
		CharacterStats stats = new CharacterStats();
		stats.setProfession(CharacterProfession.valueOf(map.get(PROFESSION_KEY).toString()));
		stats.setSpecies(CharacterSpecies.valueOf(map.get(SPECIES_KEY).toString()));
		
		stats.setAttack((int) map.get(ATTACK_KEY));
		stats.setDefense((int) map.get(DEFENSE_KEY));
		stats.setSpeed((int) map.get(SPEED_KEY));
		stats.setMagic((int) map.get(MAGIC_KEY));
		stats.setHealth((int) map.get(HEALTH_KEY));
		
		stats.setLevel((int) map.get(LEVEL_KEY));
		stats.setXp((int) map.get(XP_KEY));
		stats.setSkillPoints((int) map.get(SKILL_POINTS_KEY));
		stats.setMoney((int) map.get(MONEY_KEY));
		
		//Skills
		Object skillsObj = map.get(SKILLS_KEY);
		if (skillsObj instanceof List) {
			List<?> list = (List<?>) skillsObj;
			for (Object object : list) {
				if (object instanceof SkillInstance) {
					SkillInstance skill = (SkillInstance) object;
					stats.setSkill(SkillHandler.getInstance().getItem(skill.getNamespace(), skill.getIdentifier()), skill.getLevel());
				}
			}
		}
		//Quests
		Object questsObj = map.get(QUESTS_KEY);
		if (questsObj instanceof List) {
			List<?> list = (List<?>) questsObj;
			for (Object object : list) {
				if (object instanceof IQuest) {
					stats.quests.add((IQuest) object);
				}
			}
		}
		
		return stats;
	}

}
