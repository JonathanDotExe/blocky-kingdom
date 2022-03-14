package at.jojokobi.blockykingdom.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import at.jojokobi.blockykingdom.gui.PlayerGUI;
import at.jojokobi.blockykingdom.gui.SelectSpeciesGUI;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;

public class PlayerActionHandler implements Listener{

	private Plugin plugin;
	private EntityHandler entityHandler;
	private InventoryGUIHandler guiHandler;
	private ChatInputHandler inputHandler;
	
	public PlayerActionHandler(Plugin plugin, EntityHandler entityHandler, InventoryGUIHandler guiHandler,
			ChatInputHandler inputHandler) {
		super();
		this.plugin = plugin;
		this.entityHandler = entityHandler;
		this.guiHandler = guiHandler;
		this.inputHandler = inputHandler;
	}


	@EventHandler (priority= EventPriority.HIGHEST)
	public void onPlayerJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (StatHandler.getInstance().getStats(player).isNew()) {
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					SelectSpeciesGUI gui = new SelectSpeciesGUI(player, StatHandler.getInstance().getStats(event.getPlayer()));
					guiHandler.addGUI(gui);
					gui.show();
				}
			}, 1L);
		}
		
	}
	
	
	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && event.isShiftClick() && event.getSlot() == 40) {
			event.setCancelled(true);
			PlayerGUI gui = new PlayerGUI((Player) event.getWhoClicked(), StatHandler.getInstance().getStats(event.getWhoClicked()), entityHandler, new KingdomPoint(event.getWhoClicked().getLocation()), inputHandler);
			guiHandler.addGUI(gui);
			gui.show();
			
		}
	}

}
