package at.jojokobi.blockykingdom.gui.shop;

import java.util.Arrays;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.gui.ListGUIEntry;

public interface Buyable extends ConfigurationSerializable, ListGUIEntry {

	public ItemStack getDisplayItem ();
	
	@Override
	public default ItemStack getIcon() {
		ItemStack item = getDisplayItem();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(" * Price: " + getPrice() + "$", " * Min Level: " + getMinLevel()));
		item.setItemMeta(meta);
		return item;
	}
	
	public int getPrice ();
	
	public boolean onBuy (Player player, CharacterStats stats);
	
	public int getMinLevel ();
	
}
