package at.jojokobi.blockykingdom.kingdoms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class KingdomHandler implements Listener{
	
	private static final String KINGDOMS_FOLDER = "kingdoms";
	private static final String KINGDOM_TAG = "kingdom";
	private static KingdomHandler instance;
	
	private HashMap<KingdomPoint, Kingdom> kingdoms = new HashMap<>();
	private List<KingdomLoadListener> loadListeners = new ArrayList<>();

	private KingdomHandler() {
		
	}
	
	public static KingdomHandler getInstance () {
		if (instance == null) {
			instance = new KingdomHandler();
		}
		return instance;
	}
	
	public KingdomPoint getKingdomPoint (Location place) {
		return new KingdomPoint(place);
	}
	
	public Kingdom getKingdom (Location place) {
		return getKingdom(getKingdomPoint(place));
	}
	
	public Kingdom getKingdom (KingdomPoint point) {
		//Get it from the map
		loadKingdom(point);
		Kingdom kingdom = kingdoms.get(point);
		
		return kingdom;
	}
	
	private void loadKingdom (KingdomPoint point) {
		if (kingdoms.get(point) == null) {
			Kingdom kingdom = load(point);
			
			if (kingdom == null) {
				kingdom = generateKingdom(point);
			}
			for (KingdomLoadListener l : loadListeners) {
				l.onKingdomLoad(kingdom, point);
			}
			kingdoms.put(point, kingdom);
		}
	}
	
	private void unloadKingdom (KingdomPoint point) {
		if (kingdoms.get(point) != null) {
			save(kingdoms.get(point), point);
			kingdoms.remove(point);
		}
	}
	
	private Kingdom load (KingdomPoint pos) {
		File saveFolder = new File(Bukkit.getWorldContainer(), pos.getWorld().getName() + File.separator + BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE + File.separator + KINGDOMS_FOLDER);
		saveFolder.mkdirs();
		FileConfiguration config = new YamlConfiguration();
		
		Kingdom kingdom = null;
		
		try {
			config.load(new File(saveFolder, getKingdomName(pos)));
			kingdom = config.getSerializable(KINGDOM_TAG, Kingdom.class);
		}
		catch (FileNotFoundException e) {
			
		}
		catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		return kingdom;
	}
	
	public Kingdom generateKingdom (KingdomPoint point) {
		World world = point.getWorld();
		long seed = TerrainGenUtil.generateValueBasedSeed(world.getSeed(), point.getX() + 6542, 1, point.getZ() - 555, 1);
		if (world.getEnvironment() == Environment.NETHER) {
			seed++;
		}
		else if (world.getEnvironment() == Environment.THE_END) {
			seed--;
		}
		Random random = new Random(seed);
		Kingdom kingdom = new Kingdom(new RandomWordGenerator().generateWord(random, 4, 8));
		kingdom.setCenterX(random.nextInt(Kingdom.KINGDOM_WIDTH));
		kingdom.setCenterZ(random.nextInt(Kingdom.KINGDOM_LENGTH));
		if (Math.abs(point.getX()) > 1 && Math.abs(point.getZ()) > 1) {
			kingdom.setState(KingdomState.values()[random.nextInt(KingdomState.values().length)]);
		}
		return kingdom;
	}
	
	private void save (Kingdom kingdom, KingdomPoint pos) {
		File saveFolder = new File(Bukkit.getWorldContainer(), pos.getWorld().getName() + File.separator + BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE + File.separator + KINGDOMS_FOLDER);
		saveFolder.mkdirs();
		FileConfiguration config = new YamlConfiguration();
		
		config.set(KINGDOM_TAG, kingdom);
		
		//Save
		try {
			config.save(new File(saveFolder, getKingdomName(pos)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onEnable () {
		for (World world : Bukkit.getWorlds()) {
			for (Chunk chunk : world.getLoadedChunks()) {
				loadKingdom(getKingdomPoint(chunk.getBlock(0, 0, 0).getLocation()));
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onChunkLoad (ChunkLoadEvent event) {
		KingdomPoint point = getKingdomPoint(event.getChunk().getBlock(0, 0, 0).getLocation());
		loadKingdom(point);
	}
	
	public void addLoadListener (KingdomLoadListener listener) {
		loadListeners.add(listener);
	}
	
	public List<Kingdom> getKingdoms () {
		List<Kingdom> kingdoms = new ArrayList<>();
		for (Entry<KingdomPoint, Kingdom> entry : this.kingdoms.entrySet()) {
			kingdoms.add(entry.getValue());
		}
		return kingdoms;
	}
	
	public Set<KingdomPoint> getKingdomPoints() {
		return kingdoms.keySet();
	}
	
/*	@EventHandler
	public void onChunkUnload (ChunkUnloadEvent event) {
		KingdomPoint point = getKingdomPoint(event.getChunk().getBlock(0, 0, 0).getLocation());
		Location loc = point.toLocation();
		int chunkX = loc.getChunk().getX();
		int chunkZ = loc.getChunk().getZ();
		
		boolean loaded = false;
		
		System.out.println("Chunk unload");
		//Check if other chunks are loaded
		for (int x = 0; x < Kingdom.KINGDOM_WIDTH/TerrainGenUtil.CHUNK_WIDTH; x++) {
			for (int z = 0; z < Kingdom.KINGDOM_LENGTH/TerrainGenUtil.CHUNK_LENGTH; z++) {
				System.out.println(event.getWorld().isChunkLoaded(chunkX + x, chunkZ + z) && (event.getChunk().getX() != chunkX + x || event.getChunk().getZ() != chunkX + z));
				if (!loaded) {
					loaded = event.getWorld().isChunkLoaded(chunkX + x, chunkZ + z) && (event.getChunk().getX() != chunkX + x || event.getChunk().getZ() != chunkX + z);
				}
			}
		}
		
		if (!loaded) {
			System.out.println("Unloading: " + getKingdom(point));
			unloadKingdom(point);
		}
		System.out.println("End");
	}*/
	
	@EventHandler
	public void onWorldUnload (WorldUnloadEvent event) {
		for (KingdomPoint point : new HashSet<>(kingdoms.keySet())) {
			if (point.getWorld() == event.getWorld()) {
				unloadKingdom(point);
			}
		}
	}
	
	public void onDisable () {
		for (KingdomPoint point : new HashSet<>(kingdoms.keySet())) {
			unloadKingdom(point);
		}
	}
	
	private String getKingdomName (KingdomPoint pos) {
		return "kingdom_" + pos.getX() + "_" + pos.getZ() + ".yml";
	}
	
	@EventHandler
	public void onPlayerMove (PlayerMoveEvent event) {
		if (!getKingdomPoint(event.getTo()).equals(getKingdomPoint(event.getFrom()))) {
			Kingdom kingdom = getKingdom(event.getTo());
			event.getPlayer().sendTitle("Welcome to " + kingdom.getName() + "!", kingdom.getState().getDescrition(), 10, 70, 20);
		}
	}

}
