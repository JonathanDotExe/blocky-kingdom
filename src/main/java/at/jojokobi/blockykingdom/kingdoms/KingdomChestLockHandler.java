package at.jojokobi.blockykingdom.kingdoms;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;

public class KingdomChestLockHandler implements Listener {
	
	private NamespacedKey chestKey;
	
	public KingdomChestLockHandler(BlockyKingdomPlugin plugin) {
		chestKey = new NamespacedKey(plugin, "kingom_locked");
	}

	public void lockKingdomChest(Chest chest) {
		chest.getPersistentDataContainer().set(chestKey, PersistentDataType.STRING, "");
		chest.update();
	}
	
	public void unlockKingdomChest(Chest chest) {
		chest.getPersistentDataContainer().remove(chestKey);
		chest.update();
	}
	
	public boolean isKingdomChestLocked(Chest chest) {
		return chest.getPersistentDataContainer().has(chestKey, PersistentDataType.STRING);
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		//Check if chest
		if (event.hasBlock() && event.getClickedBlock().getState() instanceof Chest) {
			Chest chest = ((Chest) event.getClickedBlock().getState());
			//Unlock chest for all players if it's a kingdom owner
			Kingdom kingdom = KingdomHandler.getInstance().getKingdom(event.getClickedBlock().getLocation());
			if (kingdom.isOwner(event.getPlayer().getUniqueId())) {
				unlockKingdomChest(chest);
			}
			//Cancel event if locked
			if (isKingdomChestLocked(chest)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(BlockBreakEvent event) {
		//Check if chest
		if (event.getBlock().getState() instanceof Chest) {
			Chest chest = ((Chest) event.getBlock().getState());
			//Unlock chest for all players if it's a kingdom owner
			Kingdom kingdom = KingdomHandler.getInstance().getKingdom(event.getBlock().getLocation());
			if (kingdom.isOwner(event.getPlayer().getUniqueId())) {
				unlockKingdomChest(chest);
			}
			//Cancel event if locked
			if (isKingdomChestLocked(chest)) {
				event.setCancelled(true);
			}
			//TODO cancel when block is broken naturally (with explosions, etc.)
		}
	}
}
