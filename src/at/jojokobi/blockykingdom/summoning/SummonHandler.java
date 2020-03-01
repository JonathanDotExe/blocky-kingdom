package at.jojokobi.blockykingdom.summoning;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SummonHandler implements Listener{

	private List<SummoningPattern> patterns = new ArrayList<>();
	
	public void addPattern (SummoningPattern pattern) {
		patterns.add(pattern);
	}
	
	@EventHandler
	public void onBlockPlace (BlockPlaceEvent event) {
		for (SummoningPattern pattern : patterns) {
			if (pattern.matches(event)) {
				pattern.summon(event);
			}
		}
	}
	
}
