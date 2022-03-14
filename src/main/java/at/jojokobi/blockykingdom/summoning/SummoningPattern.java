package at.jojokobi.blockykingdom.summoning;

import org.bukkit.event.block.BlockPlaceEvent;

public interface SummoningPattern {

	public boolean matches (BlockPlaceEvent event);
	
	public void summon (BlockPlaceEvent event);
	
}
