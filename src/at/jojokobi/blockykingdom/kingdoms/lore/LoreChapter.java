package at.jojokobi.blockykingdom.kingdoms.lore;

import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public interface LoreChapter extends ConfigurationSerializable {
	
	public ItemStack getIcon ();
	
	public String getName();
	
	public List<String> getDescription ();
	
	public boolean isCompleted ();
	
	public List<LoreChapter> getNext ();

}
