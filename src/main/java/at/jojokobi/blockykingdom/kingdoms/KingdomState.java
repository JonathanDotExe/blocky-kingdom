package at.jojokobi.blockykingdom.kingdoms;

import org.bukkit.ChatColor;

public enum KingdomState {
	GOOD, EVIL, UNCLAIMED;
	
	public String getDescrition () {
		switch (this) {
		case GOOD:
			return ChatColor.GREEN + "A friendly land!";
		case EVIL:
			return ChatColor.RED + "I think they wont like you here!";
		case UNCLAIMED:
			return "A wild uncivilized land!";
		}
		return "A land";
	}
}
