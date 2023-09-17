package at.jojokobi.blockykingdom.players.quests;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.entity.EntityUtil;

public class KillQuest extends Quest {
	
	public static final String ENTITY_KEY = "entity";
	
	private EntityType entity;
	
	public KillQuest(int maxProgress, int reward, int experience, int skillPoints, EntityType entity) {
		super(maxProgress, reward, experience, skillPoints);
		this.entity = entity;
	}

	@Override
	public String getName() {
		return "Kill Entities";
	}

	@Override
	public String getDescritpion() {
		return "Kill " + getMaxProgress() + " " + EntityUtil.getEntityName(entity) + "s!";
	}

	@Override
	protected int getStatProgress(Player player) {
		return player.getStatistic(Statistic.KILL_ENTITY, entity);
	}

	@Override
	public Material getMaterial() {
		return Material.IRON_SWORD;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put(ENTITY_KEY, entity.toString());
		return map;
	}
	
	public static KillQuest deserialize (Map<String, Object> map) {
		KillQuest quest = new KillQuest(0, 0, 0, 0, EntityType.ZOMBIE);
		quest.load(map);
		try {
			quest.entity = EntityType.valueOf(map.get(ENTITY_KEY) + "");
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return quest;
	}
	
	public static SimpleQuestGenerator newKillQuestGenerator (EntityType entity) {
		return new SimpleQuestGenerator ((p, r, xp, s, e) -> new KillQuest(p, r, xp, s, entity).setExpirationTimeStamp(e));
	}

}
