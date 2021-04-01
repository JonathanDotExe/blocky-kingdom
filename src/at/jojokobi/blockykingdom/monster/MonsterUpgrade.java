package at.jojokobi.blockykingdom.monster;

import org.bukkit.entity.Entity;

public interface MonsterUpgrade {
	
	public void apply(Entity entity);
	
	public boolean canApply(Entity entity);
	
	public int minLevel();

}
