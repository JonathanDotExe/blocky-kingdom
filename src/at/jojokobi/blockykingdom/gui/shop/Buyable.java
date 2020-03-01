package at.jojokobi.blockykingdom.gui.shop;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.players.CharacterStats;

public interface Buyable extends ConfigurationSerializable{

	public ItemStack getIcon ();
	
	public int getPrice ();
	
	public void onBuy (Player player, CharacterStats stats);
	
	public int getMinLevel ();
	
}
