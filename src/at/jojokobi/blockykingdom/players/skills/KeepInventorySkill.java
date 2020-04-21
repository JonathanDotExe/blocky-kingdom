package at.jojokobi.blockykingdom.players.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class KeepInventorySkill extends Skill{

	private static final String IDENTIFIER = "keep_inventory";
	
	private Map<UUID, List<ItemStack>> inventories = new HashMap<> ();

	public KeepInventorySkill() {
		super(10, 20, "Keep Inventory");
	}
	
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event) {
		Player player = event.getEntity();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		List<ItemStack> drops = event.getDrops();
		List<ItemStack> saved = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < level && !drops.isEmpty(); i++) {
			int index = random.nextInt(drops.size());
			ItemStack drop = drops.remove(index);
			saved.add(drop);
		}
		inventories.put(player.getUniqueId(), saved);
	}
	
	@EventHandler
	public void onPlayerRespawn (PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		List<ItemStack> items = inventories.get(player.getUniqueId());
		if (items != null) {
			inventories.remove(player.getUniqueId());
			player.getInventory().addItem(items.toArray(new ItemStack[0]));
		}
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		Player player = event.getPlayer();
		List<ItemStack> items = inventories.get(player.getUniqueId());
		if (items  != null) {
			for (ItemStack stack : items) {
				player.getWorld().dropItemNaturally(player.getLocation(), stack);
			}
			inventories.remove(player.getUniqueId());
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getProfession() == CharacterProfession.ADVENTURER;
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.CHEST;
	}

}
