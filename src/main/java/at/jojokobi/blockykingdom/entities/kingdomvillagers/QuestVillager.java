package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.quests.IQuest;
import at.jojokobi.blockykingdom.players.quests.KillQuest;
import at.jojokobi.blockykingdom.players.quests.MineQuest;
import at.jojokobi.blockykingdom.players.quests.QuestGenetator;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public class QuestVillager extends KingdomVillager<Villager>{

	private Map<Integer, List<QuestGenetator>> generators;
	private long questSeed = 0;
	private Map<UUID, Long> lastQuestTimes = new HashMap<> ();
	
	public QuestVillager(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, null);
		questSeed = random.nextLong();
		//Peaceful AI
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new ReturnToSpawnTask());
		Map<Integer, List<QuestGenetator>> quests = new HashMap<>();
		quests.put(1, Collections.unmodifiableList(Arrays.asList(
				MineQuest.newMineQuestGenerator(Material.STONE).setMaxAmount(1).setAmountMultiplier(64).setBasicReward(1000).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.OAK_LOG).setMaxAmount(4).setAmountMultiplier(16).setBasicReward(500).makeImutable())));
		quests.put(2, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.ZOMBIE).setMaxAmount(4).setAmountMultiplier(5).setBasicReward(500).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.SKELETON).setMaxAmount(4).setAmountMultiplier(5).setBasicReward(500).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.OAK_LEAVES).setMaxAmount(4).setAmountMultiplier(16).setBasicReward(750).makeImutable())));
		quests.put(3, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.PHANTOM).setMinAmount(1).setMaxAmount(3).setBasicReward(350).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.WHEAT).setMinAmount(1).setMaxAmount(4).setAmountMultiplier(16).setBasicReward(250).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.PUMPKIN).setMinAmount(1).setMaxAmount(4).setBasicReward(200).setAmountMultiplier(2))));
		quests.put(4, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.CREEPER).setMaxAmount(5).setBasicReward(750).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.SPIDER).setMaxAmount(4).setAmountMultiplier(5).setBasicReward(500).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.IRON_ORE).setMinAmount(4).setMaxAmount(8).setAmountMultiplier(4).setBasicReward(64).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.COAL_ORE).setMinAmount(4).setMaxAmount(8).setAmountMultiplier(8).setBasicReward(64).makeImutable())));
		quests.put(5, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.SLIME).setMaxAmount(3).setBasicReward(750).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.GOLD_ORE).setMaxAmount(4).setAmountMultiplier(4).setBasicReward(500).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.DIAMOND_ORE).setMaxAmount(4).setBasicReward(750).makeImutable())));
		quests.put(6, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.BLAZE).setMaxAmount(5).setAmountMultiplier(2).setBasicReward(500).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.PILLAGER).setMaxAmount(5).setAmountMultiplier(1).setBasicReward(300).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.EMERALD_ORE).setMaxAmount(1).setBasicReward(5000).makeImutable())));
		quests.put(7, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.WITHER_SKELETON).setMaxAmount(3).setAmountMultiplier(1).setBasicReward(750).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.ILLUSIONER).setMaxAmount(2).setAmountMultiplier(1).setBasicReward(1000).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.GLOWSTONE).setMaxAmount(4).setAmountMultiplier(8).setBasicReward(300).makeImutable())));
		quests.put(8, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.GHAST).setMaxAmount(2).setAmountMultiplier(1).setBasicReward(1000).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.RAVAGER).setMaxAmount(1).setAmountMultiplier(1).setBasicReward(2000).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.REDSTONE_ORE).setMaxAmount(8).setAmountMultiplier(8).setBasicReward(150).makeImutable())));
		quests.put(9, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.PHANTOM).setMaxAmount(3).setAmountMultiplier(1).setBasicReward(750).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.LAPIS_ORE).setMaxAmount(8).setAmountMultiplier(2).setBasicReward(250).makeImutable())));
		quests.put(10, Collections.unmodifiableList(Arrays.asList(
				KillQuest.newKillQuestGenerator(EntityType.ENDER_DRAGON).setMaxAmount(1).setAmountMultiplier(1).setBasicReward(20000).makeImutable(),
				KillQuest.newKillQuestGenerator(EntityType.WITHER).setMaxAmount(1).setAmountMultiplier(1).setBasicReward(20000).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.BEDROCK).setMaxAmount(1).setBasicReward(Integer.MAX_VALUE).makeImutable(),
				MineQuest.newMineQuestGenerator(Material.DRAGON_EGG).setMaxAmount(1).setBasicReward(10000).makeImutable())));
		generators = Collections.unmodifiableMap(quests);				
	}
	
	public QuestVillager(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getPlayer().isSneaking()) {
			super.onInteract(event);
		}		
		else if (lastQuestTimes.get(player.getUniqueId()) == null || lastQuestTimes.get(player.getUniqueId()) != getDayInt()) {
			CharacterStats stats = StatHandler.getInstance().getStats(event.getPlayer()).getCharacterStats();
			IQuest quest = generateDailyQuest();
			if (stats.addQuest(quest, event.getPlayer())) {
				player.sendMessage("<" + getName() + "> Please complete this quest for me: " + quest.getDescritpion());
				lastQuestTimes.put(player.getUniqueId(), getDayInt());
				gainXP(1);
			}
			else {
				player.sendMessage("<" + getName() + "> I see. You already have too much quests to do.");
			}
		}
		else {
			player.sendMessage("<" + getName() + "> Sorry I have no work to do for you!");
		}
	}
	
	public static QuestVillager deserialize (Map<String, Object> map) {
		QuestVillager entity = new QuestVillager(null, null);
		entity.load(map);
		return entity;
	}
	
	private IQuest generateDailyQuest () {
		ZonedDateTime time = ZonedDateTime.now();
		long day = getDayInt();
		ZonedDateTime end = ZonedDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), 0, 0, 0, 0, time.getZone()).plusDays(1);
		long endTimestamp = System.currentTimeMillis() + Duration.between(time, end).toMillis();
		return generateDailyQuest(day, endTimestamp, questSeed * endTimestamp);
	}
	
	private IQuest generateDailyQuest (long day, long end, long seed) {
		Random random = new Random(seed);
		List<QuestGenetator> generators = getPossibleQuests();
		IQuest quest = generators.get(random.nextInt(generators.size())).generate(random, end);
		return quest;
	}
	
	private List<QuestGenetator> getPossibleQuests () {
		List<QuestGenetator> generators = new ArrayList<>();
		for (int i = 1; i <= getLevel(); i++) {
			generators.addAll(this.generators.get(i));
		}
		return generators;
	}
	
	private long getDayInt() {
		ZonedDateTime time = ZonedDateTime.now();
		long day = ChronoUnit.DAYS.between(time, ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, time.getZone()));
		return day;
	}

	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.NITWIT);
		villager.setVillagerType(Type.SNOW);
		villager.setBreed(false);
		villager.setAdult();
		villager.setCanPickupItems(false);
		villager.setRemoveWhenFarAway(false);
		
		villager.setRecipes(Arrays.asList());
		
		NMSEntityUtil.clearGoals(villager);
		
		return villager;
	}
	
	@Override
	public VillagerCategory getVillagerCategory() {
		return VillagerCategory.TRADER;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	public long getQuestSeed() {
		return questSeed;
	}

	public void setQuestSeed(long questSeed) {
		this.questSeed = questSeed;
	}
	
	@Override
	public Function<Integer, Integer> getLevelXPFunction() {
		return LINEAR_LEVEL_FUNCTION;
	}

	@Override
	protected double getWalkSpeed() {
		return 0.4;
	}
	
	
}
