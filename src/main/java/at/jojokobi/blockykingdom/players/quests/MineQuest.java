package at.jojokobi.blockykingdom.players.quests;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class MineQuest extends Quest {
	
	public static final String BLOCK_KEY = "block";
	
	private Material block;

	public MineQuest(int maxProgress, int reward, int experience, int skillPoints, Material entity) {
		super(maxProgress, reward, experience, skillPoints);
		this.block = entity;
	}

	@Override
	public String getName() {
		return "Mine Blocks";
	}

	@Override
	public String getDescritpion() {
		return "Mine " + getMaxProgress() + " " + getMaterialName(block) + "!";
	}

	@Override
	protected int getStatProgress(Player player) {
		return player.getStatistic(Statistic.MINE_BLOCK, block);
	}

	@Override
	public Material getMaterial() {
		return Material.IRON_PICKAXE;
	}
	
	private String getMaterialName (Material type) {
		StringBuilder builder = new StringBuilder();
		char[] chars = type.toString().toLowerCase().replace('_', ' ').toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] == ' ') {
				builder.append(Character.toUpperCase(chars[i]));
			}
			else {
				builder.append(chars[i]);
			}
		}
		return builder.toString();
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put(BLOCK_KEY, block.toString());
		return map;
	}
	
	public static MineQuest deserialize (Map<String, Object> map) {
		MineQuest quest = new MineQuest(0, 0, 0, 0, Material.STONE);
		quest.load(map);
		try {
			quest.block = Material.valueOf(map.get(BLOCK_KEY) + "");
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return quest;
	}
	
	public static SimpleQuestGenerator newMineQuestGenerator (Material material) {
		return new SimpleQuestGenerator ((p, r, xp, s, e) -> new MineQuest(p, r, xp, s, material).setExpirationTimeStamp(e));
	}

}
