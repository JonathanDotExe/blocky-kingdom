package at.jojokobi.blockykingdom;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.blockykingdom.commands.ResetStatsCommand;
import at.jojokobi.blockykingdom.dimensions.CloudJumpHandler;
import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.blockykingdom.entities.Airhead;
import at.jojokobi.blockykingdom.entities.DeathAngel;
import at.jojokobi.blockykingdom.entities.EliteGoblin;
import at.jojokobi.blockykingdom.entities.FlyingSheep;
import at.jojokobi.blockykingdom.entities.FlyingSheepType;
import at.jojokobi.blockykingdom.entities.Ghost;
import at.jojokobi.blockykingdom.entities.GhostType;
import at.jojokobi.blockykingdom.entities.Goblin;
import at.jojokobi.blockykingdom.entities.GoblinBoss;
import at.jojokobi.blockykingdom.entities.SlimeTrap;
import at.jojokobi.blockykingdom.entities.Slimerer;
import at.jojokobi.blockykingdom.entities.StoneBeetle;
import at.jojokobi.blockykingdom.entities.ZombieBoss;
import at.jojokobi.blockykingdom.entities.ZombieBossType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Archer;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.ArcherType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.GolemKnightType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Healer;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.HealerType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Knight;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.KnightType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.QuestVillager;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Recruiter;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.RecruiterType;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.TraderType;
import at.jojokobi.blockykingdom.generation.ArcherTower;
import at.jojokobi.blockykingdom.generation.Castle;
import at.jojokobi.blockykingdom.generation.DungeonTower;
import at.jojokobi.blockykingdom.generation.EliteGoblinSpawnerRoom;
import at.jojokobi.blockykingdom.generation.FlyingSheepFlock;
import at.jojokobi.blockykingdom.generation.GoblinBossChamber;
import at.jojokobi.blockykingdom.generation.GoblinCave;
import at.jojokobi.blockykingdom.generation.GoblinCaveCenter;
import at.jojokobi.blockykingdom.generation.GoblinHut;
import at.jojokobi.blockykingdom.generation.GoblinLibraryRoom;
import at.jojokobi.blockykingdom.generation.GoblinSpawnerRoom;
import at.jojokobi.blockykingdom.generation.GoblinTreasureRoom;
import at.jojokobi.blockykingdom.generation.HauntedGrave;
import at.jojokobi.blockykingdom.generation.KingdomVillage;
import at.jojokobi.blockykingdom.generation.KnightCampfire;
import at.jojokobi.blockykingdom.generation.QuestHut;
import at.jojokobi.blockykingdom.generation.RecruiterHouse;
import at.jojokobi.blockykingdom.generation.StoneBeetleRoom;
import at.jojokobi.blockykingdom.generation.TraderHut;
import at.jojokobi.blockykingdom.gui.shop.BuyableItemStack;
import at.jojokobi.blockykingdom.gui.shop.BuyableSkillPoint;
import at.jojokobi.blockykingdom.items.AirGrenade;
import at.jojokobi.blockykingdom.items.Claws;
import at.jojokobi.blockykingdom.items.Cloud;
import at.jojokobi.blockykingdom.items.CloudParticle;
import at.jojokobi.blockykingdom.items.CursedFigure;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DiamondKatana;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.EconomicFigure;
import at.jojokobi.blockykingdom.items.ExecutionersScythe;
import at.jojokobi.blockykingdom.items.FireWand;
import at.jojokobi.blockykingdom.items.FloatingWand;
import at.jojokobi.blockykingdom.items.FrozenLightning;
import at.jojokobi.blockykingdom.items.GoblinCharm;
import at.jojokobi.blockykingdom.items.GoblinFang;
import at.jojokobi.blockykingdom.items.GoblinKnife;
import at.jojokobi.blockykingdom.items.GoblinSkin;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.HealingFigure;
import at.jojokobi.blockykingdom.items.HealingWand;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.MagicTorch;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.OracleFigure;
import at.jojokobi.blockykingdom.items.ProtectingFigure;
import at.jojokobi.blockykingdom.items.RainbowDye;
import at.jojokobi.blockykingdom.items.SlimerersHeart;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.items.Sunglasses;
import at.jojokobi.blockykingdom.items.ThunderWand;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomHappinessHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.siege.KingdomSiegeHandler;
import at.jojokobi.blockykingdom.monster.MonsterUpgradeHandler;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.PlayerActionHandler;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.quests.KillQuest;
import at.jojokobi.blockykingdom.players.quests.MineQuest;
import at.jojokobi.blockykingdom.players.skills.AdrenalineSkill;
import at.jojokobi.blockykingdom.players.skills.AimSkill;
import at.jojokobi.blockykingdom.players.skills.ClimbingSkill;
import at.jojokobi.blockykingdom.players.skills.CrawlingSkill;
import at.jojokobi.blockykingdom.players.skills.GillsSkill;
import at.jojokobi.blockykingdom.players.skills.HeavinessSkill;
import at.jojokobi.blockykingdom.players.skills.HungerSkill;
import at.jojokobi.blockykingdom.players.skills.InvisibilitySkill;
import at.jojokobi.blockykingdom.players.skills.KeepInventorySkill;
import at.jojokobi.blockykingdom.players.skills.LeechingSkill;
import at.jojokobi.blockykingdom.players.skills.MiningSenseSkill;
import at.jojokobi.blockykingdom.players.skills.PoisonSkill;
import at.jojokobi.blockykingdom.players.skills.PunchSkill;
import at.jojokobi.blockykingdom.players.skills.RageSkill;
import at.jojokobi.blockykingdom.players.skills.Skill;
import at.jojokobi.blockykingdom.players.skills.SkillHandler;
import at.jojokobi.blockykingdom.players.skills.SkillInstance;
import at.jojokobi.blockykingdom.players.skills.StealSkill;
import at.jojokobi.blockykingdom.players.skills.StompAttackSkill;
import at.jojokobi.blockykingdom.players.skills.TeleportSkill;
import at.jojokobi.blockykingdom.players.skills.VitalitySkill;
import at.jojokobi.blockykingdom.players.skills.WallJumpSkill;
import at.jojokobi.blockykingdom.summoning.AirheadPattern;
import at.jojokobi.blockykingdom.summoning.KnightPattern;
import at.jojokobi.blockykingdom.summoning.SlimererPattern;
import at.jojokobi.blockykingdom.summoning.SummonHandler;
import at.jojokobi.blockykingdom.summoning.VillagerHousePatternFactory;
import at.jojokobi.blockykingdom.summoning.ZombieBossPattern;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.JojokobiUtilPlugin;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnData;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawner;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnerHandler;
import at.jojokobi.mcutil.entity.spawns.CustomSpawnsHandler;
import at.jojokobi.mcutil.entity.spawns.FunctionSpawn;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;

public class BlockyKingdomPlugin extends JavaPlugin implements Listener{
	
	public static final String PLUGIN_NAME = "BlockyKingdom";
	public static final String BLOCKY_KINGDOM_NAMESPACE = "blockykingdom";
	
	private JojokobiUtilPlugin util;
	
//	private GenerationHandler genHandler;
	
//	private EntityHandler<CustomEntity<?>> entityHandler;
	private ChatInputHandler inputHandler;
//	private InventoryGUIHandler guiHandler;
//	private DimensionHandler dimensionHandler;
	private KingdomSiegeHandler siegeHandler;

	@Override
	public void onLoad() {
		super.onLoad();
		ConfigurationSerialization.registerClass(CharacterStats.class, "BlockyKingdomCharacterStats");
		ConfigurationSerialization.registerClass(SkillInstance.class, "BlockyKingdomSkillInstance");
		ConfigurationSerialization.registerClass(Kingdom.class, "BlockyKingdomKingdom");
		ConfigurationSerialization.registerClass(KingdomPoint.class, "BlockyKingdomKingdomPoint");
		
		ConfigurationSerialization.registerClass(FlyingSheep.class, "BlockyKingdomFlyingSheep");
		ConfigurationSerialization.registerClass(Ghost.class, "BlockyKingdomGhost");
		ConfigurationSerialization.registerClass(DeathAngel.class, "BlockyKingdomDeathAngel");
		ConfigurationSerialization.registerClass(ZombieBoss.class, "BlockyKingdomZombieBoss");
		ConfigurationSerialization.registerClass(Slimerer.class, "BlockyKingdomSlimerer");
		ConfigurationSerialization.registerClass(SlimeTrap.class, "BlockyKingdomSlimeTrap");
		ConfigurationSerialization.registerClass(Airhead.class, "BlockyKingdomAirhead");
		ConfigurationSerialization.registerClass(Goblin.class, "BlockyKingdomGoblin");
		ConfigurationSerialization.registerClass(GoblinBoss.class, "BlockyKingdomGoblinBoss");
		ConfigurationSerialization.registerClass(EliteGoblin.class, "BlockyKingdomEliteGoblin");
		ConfigurationSerialization.registerClass(StoneBeetle.class, "BlockyKingdomStoneBeetle");
		
		ConfigurationSerialization.registerClass(Archer.class, "BlockyKingdomArcher");
		ConfigurationSerialization.registerClass(Healer.class, "BlockyKingdomHealer");
		ConfigurationSerialization.registerClass(Knight.class, "BlockyKingdomKnight");
		ConfigurationSerialization.registerClass(Recruiter.class, "BlockyKingdomRecruiter");
		ConfigurationSerialization.registerClass(Trader.class, "BlockyKingdomTrader");
		ConfigurationSerialization.registerClass(QuestVillager.class, "BlockyKingdomQuestVillager");
		
		ConfigurationSerialization.registerClass(KillQuest.class, "BlockyKingdomKillQuest");
		ConfigurationSerialization.registerClass(MineQuest.class, "BlockyKingdomMineQuest");
		
		ConfigurationSerialization.registerClass(BuyableSkillPoint.class, "BlockyKingdomBuyableSkillPoint");
		ConfigurationSerialization.registerClass(BuyableItemStack.class, "BlockyKingdomBuyableItemStack");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		util = getPlugin(JojokobiUtilPlugin.class);
		
		CustomEntitySpawnerHandler spawnerHandler = util.getSpawnerHandler();
		CustomEntitySpawner spawner = util.getEntitySpawner();
		
		//Input Handler
		inputHandler = new ChatInputHandler(this);
		Bukkit.getPluginManager().registerEvents(inputHandler, this);
		//GUI Handler
//		guiHandler = new InventoryGUIHandler(this);
//		Bukkit.getPluginManager().registerEvents(guiHandler, this);
		//Kingdoms
//		kingdomHandler = new KingdomHandler();
		Bukkit.getPluginManager().registerEvents(KingdomHandler.getInstance(), this);
		KingdomHandler.getInstance().onEnable();
		//Stats
		Bukkit.getPluginManager().registerEvents(StatHandler.getInstance(), this);
		//Init Skills
		registerSkill(new GillsSkill());
		registerSkill(new RageSkill());
		registerSkill(new ClimbingSkill());
		registerSkill(new HeavinessSkill());
		registerSkill(new PunchSkill());
		registerSkill(new InvisibilitySkill());
		registerSkill(new WallJumpSkill());
		registerSkill(new StompAttackSkill());
		registerSkill(new PoisonSkill());
		registerSkill(new VitalitySkill(this));
		registerSkill(new LeechingSkill());
		registerSkill(new CrawlingSkill());
		registerSkill(new HungerSkill());
		registerSkill(new AimSkill(this));
		registerSkill(new StealSkill());
		registerSkill(new TeleportSkill());
		registerSkill(new KeepInventorySkill());
		registerSkill(new AdrenalineSkill());
		registerSkill(new MiningSenseSkill());
		
//		Bukkit.getPluginManager().registerEvents(climbing, this);
//		Bukkit.getPluginManager().registerEvents(gills, this);
//		Bukkit.getPluginManager().registerEvents(rage, this);
//		Bukkit.getPluginManager().registerEvents(heaviness, this);
//		Bukkit.getPluginManager().registerEvents(punch, this);
//		Bukkit.getPluginManager().registerEvents(invisibility, this);
//		Bukkit.getPluginManager().registerEvents(wallJump, this);
//		Bukkit.getPluginManager().registerEvents(stompAttack, this);
//		Bukkit.getPluginManager().registerEvents(poison, this);
//		Bukkit.getPluginManager().registerEvents(vitality, this);
//		Bukkit.getPluginManager().registerEvents(leech, this);
//		Bukkit.getPluginManager().registerEvents(crawling, this);
//		Bukkit.getPluginManager().registerEvents(hunger, this);
//		Bukkit.getPluginManager().registerEvents(aim, this);
//		Bukkit.getPluginManager().registerEvents(steal, this);
//		Bukkit.getPluginManager().registerEvents(teleport, this);
//		
//		SkillHandler.getInstance().addItem(gills);
//		SkillHandler.getInstance().addItem(rage);
//		SkillHandler.getInstance().addItem(climbing);
//		SkillHandler.getInstance().addItem(heaviness);
//		SkillHandler.getInstance().addItem(punch);
//		SkillHandler.getInstance().addItem(invisibility);
//		SkillHandler.getInstance().addItem(wallJump);
//		SkillHandler.getInstance().addItem(stompAttack);
//		SkillHandler.getInstance().addItem(poison);
//		SkillHandler.getInstance().addItem(vitality);
//		SkillHandler.getInstance().addItem(leech);
//		SkillHandler.getInstance().addItem(crawling);
//		SkillHandler.getInstance().addItem(hunger);
//		SkillHandler.getInstance().addItem(aim);
//		SkillHandler.getInstance().addItem(steal);
//		SkillHandler.getInstance().addItem(teleport);

		EntityHandler entityHandler = util.getEntityHandler();
		entityHandler.addLegacySaveFolder(new EntityHandler.LegacySaveFolder("blockykingdom" + File.separator + "entities") {
			@Override
			public CustomEntity<?> getStandardInstance(World world) {
				return null;
			}
		});
		//Actions
		PlayerActionHandler playerActionHandler = new PlayerActionHandler(this, entityHandler, util.getGuiHandler(), inputHandler);
		Bukkit.getPluginManager().registerEvents(playerActionHandler, this);
		//Items
		Katana katana = new Katana(this);
		Bukkit.getPluginManager().registerEvents(katana, this);
		Dagger dagger = new Dagger(this);
		Bukkit.getPluginManager().registerEvents(dagger, this);
		Smasher smasher = new Smasher(this);
		Bukkit.getPluginManager().registerEvents(smasher, this);
		GrapplingHook hook = new GrapplingHook(this);
		Bukkit.getPluginManager().registerEvents(hook, this);
		Hammer hammer = new Hammer(this);
		Bukkit.getPluginManager().registerEvents(hammer, this);
		DoubleBow doubleBow = new DoubleBow(this);
		Bukkit.getPluginManager().registerEvents(doubleBow, this);
		FireWand fireWand = new FireWand(this);
		Bukkit.getPluginManager().registerEvents(fireWand, this);
		MagicTorch magicTorch = new MagicTorch(this);
		Bukkit.getPluginManager().registerEvents(magicTorch, this);
		CursedFigure cursedStatue = new CursedFigure(this, entityHandler);
		Bukkit.getPluginManager().registerEvents(cursedStatue, this);
		EconomicFigure economicFigure = new EconomicFigure(this);
		Bukkit.getPluginManager().registerEvents(economicFigure, this);
		DiamondKatana diamondKatana = new DiamondKatana(this);
		Bukkit.getPluginManager().registerEvents(diamondKatana, this);
		Money money = new Money();
		Bukkit.getPluginManager().registerEvents(money, this);
		Bukkit.getPluginManager().registerEvents(new OracleFigure(this), this);
		Bukkit.getPluginManager().registerEvents(new ExecutionersScythe(), this);
		Bukkit.getPluginManager().registerEvents(new SlimerersHeart(), this);
		Bukkit.getPluginManager().registerEvents(new RainbowDye(entityHandler), this);
		Bukkit.getPluginManager().registerEvents(new HealingWand(this), this);
		Bukkit.getPluginManager().registerEvents(new HealingFigure(this), this);
		Bukkit.getPluginManager().registerEvents(new ProtectingFigure(this), this);
		Bukkit.getPluginManager().registerEvents(new CloudParticle(), this);
		Bukkit.getPluginManager().registerEvents(new Cloud(this), this);
		Bukkit.getPluginManager().registerEvents(new FloatingWand(this), this);
		Bukkit.getPluginManager().registerEvents(new ThunderWand(this), this);
		Bukkit.getPluginManager().registerEvents(new Sunglasses(this), this);
		Bukkit.getPluginManager().registerEvents(new FrozenLightning(), this);
		Bukkit.getPluginManager().registerEvents(new AirGrenade(this), this);
		Bukkit.getPluginManager().registerEvents(new GoblinKnife(), this);
		Bukkit.getPluginManager().registerEvents(new GoblinFang(), this);
		Bukkit.getPluginManager().registerEvents(new GoblinSkin(this), this);
		Bukkit.getPluginManager().registerEvents(new GoblinCharm(this), this);
		Bukkit.getPluginManager().registerEvents(new Claws(this), this);
		
		//Entity Handler
		
//		entityHandler = new EntityHandler<CustomEntity<?>>(this, util.getGuiHandler(), BLOCKY_KINGDOM_NAMESPACE + File.separator + "entities") {
//			@Override
//			public CustomEntity<?> getStandardInstance(Chunk chunk) {
//				return null;
//			}
//		};
		
		//AIs
//		AIHandler.addAIController(GhostThrowAI.getInstance());
//		AIHandler.addAIController(KingdomDefenderAI.getInstance());
//		AIHandler.addAIController(FlyingSheepAI.getInstance());
//		AIHandler.addAIController(DeathAngelAI.getInstance());
//		AIHandler.addAIController(SlimererAI.getInstance());
//		AIHandler.addAIController(SlimeTrapAI.getInstance());
//		AIHandler.addAIController(ZombieBossAI.getInstance());
//		AIHandler.addAIController(AirheadAI.getInstance());
		
		//Entity types
		entityHandler.getHandler().addItem(ZombieBossType.getInstance());
		entityHandler.getHandler().addItem(GhostType.getInstance());
		entityHandler.getHandler().addItem(KnightType.getInstance());
		entityHandler.getHandler().addItem(ArcherType.getInstance());
		entityHandler.getHandler().addItem(TraderType.getInstance());
		entityHandler.getHandler().addItem(HealerType.getInstance());
		entityHandler.getHandler().addItem(RecruiterType.getInstance());
		entityHandler.getHandler().addItem(FlyingSheepType.getInstance());
		entityHandler.getHandler().addItem(GolemKnightType.getInstance());
		
		Bukkit.getPluginManager().registerEvents(new MonsterUpgradeHandler(entityHandler), this);
		
		CustomSpawnsHandler.getInstance().addItem(new FunctionSpawn (Goblin.GOBLIN_SPAWN_KEY, l -> new Goblin(l, null)));
		CustomSpawnsHandler.getInstance().addItem(new FunctionSpawn (GoblinBoss.GOBLIN_BOSS_SPAWN_KEY, l -> new GoblinBoss(l, null)));
		CustomSpawnsHandler.getInstance().addItem(new FunctionSpawn (EliteGoblin.ELITE_GOBLIN_SPAWN_KEY, l -> new EliteGoblin(l, null)));
		CustomSpawnsHandler.getInstance().addItem(new FunctionSpawn(StoneBeetle.STONE_BEETLE_KEY, l -> new StoneBeetle(l, null)));
		CustomSpawnsHandler.getInstance().addItem(new FunctionSpawn(DeathAngel.DEATH_ANGEL_KEY, l -> new DeathAngel(l, null)));

						
		//Spawns
		spawner.addSpawn(new CustomEntitySpawnData(CustomSpawnsHandler.getInstance().getItem(DeathAngel.DEATH_ANGEL_KEY), 0.4, 3).setSpawnGroupSize(3).setCanSpawn((l, p) -> HeavenDimension.getInstance().isDimension(l.getWorld())).setMaxEntitiesAround(32));
		spawner.addSpawn(new CustomEntitySpawnData(CustomSpawnsHandler.getInstance().getItem(Goblin.GOBLIN_SPAWN_KEY), 0.2, 4).setSpawnGroupSize(4).setCanSpawn((l, p) -> l.getBlock().getLightLevel() <= 7).setMaxEntitiesAround(16));
		spawner.addSpawn(new CustomEntitySpawnData(CustomSpawnsHandler.getInstance().getItem(EliteGoblin.ELITE_GOBLIN_SPAWN_KEY), 0.05, 1).setCanSpawn((l, p) -> l.getBlock().getLightLevel() <= 7).setMaxEntitiesAround(16));

		
		//Happiness Handler
		Bukkit.getPluginManager().registerEvents(new KingdomHappinessHandler(this, entityHandler), this);
		//Sieges
		siegeHandler = new KingdomSiegeHandler(this, entityHandler);
		
		//Dimensions
		util.getDimensionHandler().addDimension(HeavenDimension.getInstance());
		
		Bukkit.getPluginManager().registerEvents(CloudJumpHandler.getInstance(), this);
		
		//Generation
		GenerationHandler genHandler = util.getGenerationHandler();
		genHandler.addStructure(new DungeonTower(entityHandler));
		genHandler.addStructure(new HauntedGrave(cursedStatue));
		ArcherTower tower = new ArcherTower(entityHandler);
		genHandler.addStructure(tower);
		TraderHut traderHut = new TraderHut(entityHandler, getDimensionHandler());
		genHandler.addStructure(traderHut);
		RecruiterHouse recruiterHouse = new RecruiterHouse(entityHandler);
		genHandler.addStructure(recruiterHouse);
		QuestHut questHut = new QuestHut(entityHandler);
		genHandler.addStructure(questHut);
		KnightCampfire campfire = new KnightCampfire(entityHandler);
		genHandler.addStructure(campfire);
		genHandler.addStructure(new Castle(entityHandler, getDimensionHandler(), tower, traderHut));
		genHandler.addStructure(new FlyingSheepFlock(entityHandler));
		genHandler.addStructure(new KingdomVillage(getDimensionHandler(), traderHut, traderHut, traderHut, recruiterHouse, recruiterHouse, questHut, questHut, tower, campfire));
		genHandler.addStructure(new GoblinHut(spawnerHandler));
		GoblinSpawnerRoom goblinSpawner = new GoblinSpawnerRoom(spawnerHandler, getDimensionHandler());
		genHandler.addStructure(goblinSpawner);
		EliteGoblinSpawnerRoom eliteGoblinspawner = new EliteGoblinSpawnerRoom(spawnerHandler, getDimensionHandler());
		genHandler.addStructure(eliteGoblinspawner);
		GoblinCaveCenter caveCenter = new GoblinCaveCenter();
		genHandler.addStructure(caveCenter);
		GoblinBossChamber bossChamber = new GoblinBossChamber(entityHandler);
		genHandler.addStructure(bossChamber);
		GoblinLibraryRoom libraryRoom = new GoblinLibraryRoom(entityHandler);
		genHandler.addStructure(libraryRoom);
		GoblinTreasureRoom treasureRoom = new GoblinTreasureRoom();
		genHandler.addStructure(treasureRoom);
		StoneBeetleRoom beetleRoom = new StoneBeetleRoom(entityHandler);
		genHandler.addStructure(beetleRoom);
		GoblinCave goblinCave = new GoblinCave(getDimensionHandler(), caveCenter, bossChamber, new Structure[]{goblinSpawner, eliteGoblinspawner, treasureRoom, libraryRoom}, new Structure[]{goblinSpawner, goblinSpawner, eliteGoblinspawner, treasureRoom, treasureRoom, libraryRoom, beetleRoom}, new Structure[]{goblinSpawner, goblinSpawner, goblinSpawner, eliteGoblinspawner, treasureRoom, treasureRoom, libraryRoom, libraryRoom, beetleRoom, beetleRoom}, new Structure[]{goblinSpawner, goblinSpawner, goblinSpawner, treasureRoom, libraryRoom, beetleRoom});
		genHandler.addStructure(goblinCave);
		
		genHandler.addLegacySaveFolder(BLOCKY_KINGDOM_NAMESPACE + File.separator + "structures");
		
		//Summoning
		SummonHandler summonHandler = new SummonHandler();
		summonHandler.addPattern(VillagerHousePatternFactory.createRecruiterPattern(entityHandler, genHandler, recruiterHouse));
		summonHandler.addPattern(VillagerHousePatternFactory.createTraderPattern(entityHandler, genHandler, recruiterHouse));
		summonHandler.addPattern(VillagerHousePatternFactory.createQuestHutPattern(entityHandler, genHandler, questHut));
		summonHandler.addPattern(new KnightPattern(this, entityHandler));
		summonHandler.addPattern(new SlimererPattern(this, entityHandler));
		summonHandler.addPattern(new ZombieBossPattern(this, entityHandler));
		summonHandler.addPattern(new AirheadPattern(this, entityHandler));
		Bukkit.getPluginManager().registerEvents(summonHandler, this);
		
		Bukkit.getPluginManager().registerEvents(this, this);
		
		Bukkit.getScheduler().runTask(this, () -> Bukkit.addRecipe(getExperienceRecipe()));
		
		//Commands
		getCommand(ResetStatsCommand.COMMAND_NAME).setExecutor(new ResetStatsCommand(util.getGuiHandler()));
		
		//Config
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		for (String name : config.getStringList("kingdoms.noKingdomWorlds")) {
			KingdomHandler.getInstance().addNoKingdomWorlds(name);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		KingdomHandler.getInstance().onDisable();
		util.getDimensionHandler().unloadWorlds(HeavenDimension.getInstance());
	}

	public GenerationHandler getGenHandler() {
		return util.getGenerationHandler();
	}
//	
//	public EntityHandler<CustomEntity<?>> getEntityHandler() {
//		return entityHandler;
//	}

	public ChatInputHandler getInputHandler() {
		return inputHandler;
	}

	public static String getBlockyKingdomNamespace() {
		return BLOCKY_KINGDOM_NAMESPACE;
	}

	public InventoryGUIHandler getGuiHandler() {
		return util.getGuiHandler();
	}
	
//	@EventHandler
//	public void onPlayerInteract (PlayerInteractEvent event) {
//		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.RED_BED && event.getClickedBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.GLOWSTONE) {
//			event.setCancelled(true);
//			World dimWorld = util.getDimensionHandler().getDimensionWorld(event.getPlayer().getWorld(), HeavenDimension.getInstance());
//			if (dimWorld != null) {
//				System.out.println("Teleporting to " + dimWorld.getName());
//				Player player = event.getPlayer();
//				Location place = new Location(dimWorld, player.getLocation().getX(), 80, player.getLocation().getZ());
//				player.teleport(place);
////				util.getEntityHandler().addEntity(new DeathAngel(place, util.getEntityHandler()));
//			}
//		}
////		else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
////			util.getEntityHandler().addEntity(new DeathAngel(event.getClickedBlock().getLocation().add(0, 1, 0), util.getEntityHandler()));
////		}
//	}
//	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		if (HeavenDimension.getInstance().isDimension(event.getTo().getWorld()) && event.getTo().getY() < 0) {
			Location to = new Location(util.getDimensionHandler().getNormalWorld(event.getTo().getWorld()), event.getTo().getX(), 255, event.getTo().getZ());
			event.setTo(to);
//			util.getEntityHandler().addEntity(new DeathAngel(to, util.getEntityHandler()));
		}
	}

	public DimensionHandler getDimensionHandler() {
		return util.getDimensionHandler();
	}

	public KingdomSiegeHandler getSiegeHandler() {
		return siegeHandler;
	}
	
	private Recipe getExperienceRecipe () {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "experience_bottle"), new ItemStack(Material.EXPERIENCE_BOTTLE, 16));
		recipe.shape("BMB", "SGS", "RRR");
		recipe.setIngredient('B', Material.BLAZE_POWDER);
		recipe.setIngredient('M', Material.GLISTERING_MELON_SLICE);
		recipe.setIngredient('S', Material.GLOWSTONE_DUST);
		recipe.setIngredient('R', Material.REDSTONE_BLOCK);
		recipe.setIngredient('G', Material.GLASS_BOTTLE);
		return recipe;
	}
	
	private void registerSkill (Skill skill) {
		Bukkit.getPluginManager().registerEvents(skill, this);
		SkillHandler.getInstance().addItem(skill);
	}

}
