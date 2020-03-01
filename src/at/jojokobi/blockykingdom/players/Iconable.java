package at.jojokobi.blockykingdom.players;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface Iconable {

	public Material getMaterial();
	
	public default String getName () {
		String string = toString();
		return (string.length() > 0 ? string.charAt(0) : "") + (string.length() > 1 ? string.substring(1).toLowerCase(): "");
	}
	
	public default ItemStack getIcon () {
		ItemStack icon = new ItemStack(getMaterial());
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getName());
		icon.setItemMeta(meta);
		return icon;
	}
	
}
