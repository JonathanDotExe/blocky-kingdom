package at.jojokobi.blockykingdom.players.quests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Quest implements IQuest {
	
	public static final String MAX_PROGRESS_KEY = "maxProgress";
	public static final String REWARD_KEY = "reward";
	public static final String EXPERIENCE_KEY = "experience";
	public static final String SKILL_POINTS_KEY = "skillPoints";
	public static final String INITIAL_PROGRESS_KEY = "initialProgress";
	public static final String EXPIRATION_TIME_STAMP_KEY = "expirationTimeStamp";
	
	private int maxProgress;
	private int reward;
	private int experience;
	private int skillPoints;
	private int initialProgress;
	private long expirationTimeStamp;
	

	public Quest(int maxProgress, int reward, int experience, int skillPoints) {
		super();
		this.maxProgress = maxProgress;
		this.reward = reward;
		this.experience = experience;
		this.skillPoints = skillPoints;
	}

	@Override
	public int getReward() {
		return reward;
	}
	
	@Override
	public int getMaxProgress() {
		return maxProgress;
	}
	
	
	@Override
	public int getExperience() {
		return experience;
	}

	@Override
	public int getSkillPoints() {
		return skillPoints;
	}

	public int getInitialProgress() {
		return initialProgress;
	}
	
	@Override
	public int getProgress(Player player) {
		return getStatProgress(player) - getInitialProgress();
	}
	
	@Override
	public boolean isDone(Player player) {
		return getProgress(player) >= getMaxProgress();
	}
	
	@Override
	public void initQuest(Player player) {
		initialProgress = getStatProgress(player);
	}
	
	@Override
	public long getExpirationTimeStamp() {
		return expirationTimeStamp;
	}

	public Quest setExpirationTimeStamp(long expirationTimeStamp) {
		this.expirationTimeStamp = expirationTimeStamp;
		return this;
	}

	protected abstract int getStatProgress (Player player);
	
	public abstract Material getMaterial ();
	
	public Map<String, Object> serialize () {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put(EXPIRATION_TIME_STAMP_KEY, expirationTimeStamp);
		map.put(INITIAL_PROGRESS_KEY, initialProgress);
		map.put(MAX_PROGRESS_KEY, maxProgress);
		map.put(REWARD_KEY, reward);
		return map;
	}
	
	protected void load (Map<String, Object> map) {
		try {
			expirationTimeStamp = Long.parseLong(map.get(EXPIRATION_TIME_STAMP_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			reward = Integer.parseInt(map.get(REWARD_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			experience = Integer.parseInt(map.get(EXPERIENCE_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			skillPoints = Integer.parseInt(map.get(SKILL_POINTS_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			initialProgress = Integer.parseInt(map.get(INITIAL_PROGRESS_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			maxProgress = Integer.parseInt(map.get(MAX_PROGRESS_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemStack toItemStack(Player player) {
		ItemStack stack = new ItemStack(getMaterial());
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(getName());
		long timeLeft = getExpirationTimeStamp() - System.currentTimeMillis();
		timeLeft /= 1000;
		meta.setLore(Arrays.asList(getDescritpion(), getProgress(player) + "/" + getMaxProgress(), expired() ? "Expired" : "Time left " + (timeLeft/3600) + "h " + (timeLeft%3600/60) + " min " + (timeLeft%60) + " secs"));
		stack.setItemMeta(meta);
		return stack;
	}
	
}
