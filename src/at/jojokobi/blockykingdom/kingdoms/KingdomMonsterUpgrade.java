package at.jojokobi.blockykingdom.kingdoms;

import org.bukkit.entity.Entity;

public interface KingdomMonsterUpgrade {
	
	public void apply(Entity entity);
	
	public boolean canApply(Entity entity);
	
	public int minLevel();

}
