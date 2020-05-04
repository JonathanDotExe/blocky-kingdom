package at.jojokobi.blockykingdom.players;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.quests.KillQuest;
import at.jojokobi.blockykingdom.players.quests.MineQuest;

public class StatHandler implements Listener{
	
	private static final String PLAYER_PATH = "players";
	
	private Map<Player, Statable> statables = new HashMap<>();
	
//	private File dataFolder;
	
	private static StatHandler instance;
	
	
	private StatHandler() {
		super();
	}
	
	public static StatHandler getInstance () {
		if (instance == null) {
			instance = new StatHandler();
		}
		return instance;
	}

	public Statable getStats (Object entity) {
		return statables.get(entity);
	}
	
	@EventHandler (priority=EventPriority.NORMAL)
	public void onPlayerJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = null;
		boolean isNew = true;
		
		File dataFolder = new File(Bukkit.getPluginManager().getPlugin(BlockyKingdomPlugin.PLUGIN_NAME).getDataFolder(), PLAYER_PATH);
		dataFolder.mkdirs();
		
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(dataFolder, event.getPlayer().getUniqueId() + ".yml"));
			stats = config.getSerializable("stats", CharacterStats.class);
			isNew = false;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		if (stats == null) {
			stats = new CharacterStats();
		}
		
		if (isNew) {
			stats.addQuest(new KillQuest(15, 1000, EntityType.ZOMBIE).setExpirationTimeStamp(Long.MAX_VALUE), event.getPlayer());
			stats.addQuest(new MineQuest(8, 100, Material.WHEAT).setExpirationTimeStamp(Long.MAX_VALUE), event.getPlayer());
			stats.addQuest(new MineQuest(3, 2000, Material.DIAMOND_ORE).setExpirationTimeStamp(Long.MAX_VALUE), event.getPlayer());
		}
		
//		stats.addQuest(new MineQuest(64, 1000, Material.STONE).setExpirationTimeStamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24), player);
//		stats.addQuest(new MineQuest(5, 5000, Material.DIAMOND).setExpirationTimeStamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24), player);
//		stats.addQuest(new KillQuest(15, 1000, EntityType.ZOMBIE).setExpirationTimeStamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24), player);
//		stats.addQuest(new KillQuest(5, 1500, EntityType.SLIME).setExpirationTimeStamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24), player);
//		if (stats == null) {
//			stats = new CharacterStats();
//			
//			Bukkit.getScheduler().runTask(plugin, new Runnable() {
//				@Override
//				public void run() {
//					SelectSpeciesGUI gui = new SelectSpeciesGUI(player, getStats(event.getPlayer()));
//					guiHandler.addGUI(gui);
//					gui.show();
//				}
//			});
//		}
		statables.put(player, new BkPlayer(stats, isNew));
	}
	
	@EventHandler
	public void onPlayerExpChange (PlayerExpChangeEvent event) {
		CharacterStats stats = statables.get(event.getPlayer()).getCharacterStats();
		stats.gainXp(event.getAmount() * 6);
		if (Math.random() < 0.25) {
			stats.setSkillPoints(stats.getSkillPoints() + 1);
		}
		stats.setMoney(stats.getMoney() + (int) (event.getAmount()*Math.random()));
	}
	
	@EventHandler
	public void onPlayerAdvancementDone (PlayerAdvancementDoneEvent event) {
		CharacterStats stats = statables.get(event.getPlayer()).getCharacterStats();
		stats.gainXp(30);
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		CharacterStats stats = statables.remove(event.getPlayer()).getCharacterStats();
		File dataFolder = new File(Bukkit.getPluginManager().getPlugin(BlockyKingdomPlugin.PLUGIN_NAME).getDataFolder(), PLAYER_PATH);
		dataFolder.mkdirs();
		//Save
		FileConfiguration config = new YamlConfiguration();
		config.set("stats", stats);
		try {
			config.save(new File(dataFolder, event.getPlayer().getUniqueId() + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		if (!event.isCancelled()) {
			//Attack
			{
				Statable statable = statables.get(event.getDamager());
				if (statable != null) {
					CharacterStats stats = statable.getCharacterStats();
					event.setDamage(event.getDamage() * stats.getAttackDamageMultiplier());
				}
				else if (event.getDamager() instanceof Projectile) {
					statable = statables.get(((Projectile) event.getDamager()).getShooter());
					if (statable != null) {
						CharacterStats stats = statable.getCharacterStats();
						event.setDamage(event.getDamage() * stats.getAttackDamageMultiplier());
					}
				}
			}
			//Kill money
			if (event.getEntity() instanceof Damageable && Math.round(((Damageable) event.getEntity()).getHealth() - event.getFinalDamage()) <= 0) {
				Statable statable = statables.get(event.getDamager());
				if (statable != null) {
					CharacterStats stats = statable.getCharacterStats();
					stats.setMoney(stats.getMoney() + 50);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamage (EntityDamageEvent event) {
		//Defense
		if (!event.isCancelled()){
			Statable statable = statables.get(event.getEntity());
			if (statable != null) {
				CharacterStats stats = statable.getCharacterStats();
				event.setDamage(event.getDamage() * stats.getDefenseDamageMultiplier());
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Statable stats = statables.get(player);
		//Health
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0 + stats.getCharacterStats().getTotalHealth() * 2);
	}
	
	@EventHandler
	public void onPlayerToggleSprint (PlayerToggleSprintEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = getStats(player).getCharacterStats();
		//Sprint
		if (event.isSprinting()) {
			player.setWalkSpeed(Math.min(1, (float) (0.3 * stats.getSpeedMultiplier())));
		}
		else {
			player.setWalkSpeed(0.2f);
		}
	}
	
	public Set<Entry<Player, Statable>> getEntrySet () {
		return statables.entrySet();
	}
	
}
