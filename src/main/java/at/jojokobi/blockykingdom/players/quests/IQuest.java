package at.jojokobi.blockykingdom.players.quests;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IQuest extends ConfigurationSerializable{
	
	public String getName ();
	
	public String getDescritpion ();
	
	public int getReward ();
	
	public int getExperience();
	
	public int getSkillPoints();
	
	public boolean isDone (Player player);
	
	public int getMaxProgress ();
	
	public int getProgress (Player player);
	
	public ItemStack toItemStack (Player player);
	
	public void initQuest (Player player);
	
	public long getExpirationTimeStamp ();
	
	public default boolean expired () {
		return System.currentTimeMillis() > getExpirationTimeStamp();
	}

}
