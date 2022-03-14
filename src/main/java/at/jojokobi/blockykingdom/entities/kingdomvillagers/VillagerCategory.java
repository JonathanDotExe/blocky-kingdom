package at.jojokobi.blockykingdom.entities.kingdomvillagers;

public enum VillagerCategory {
	TRADER, WARRIOR, HEALER;
	
	public int getMaxAmount (int kingdomLevel) {
		int amount = 0;
		switch (this) {
		case HEALER:
			amount = 1 + kingdomLevel;
			break;
		case WARRIOR:
			amount = 2 + 2 * kingdomLevel;
			break;
		case TRADER:
			amount = 2 + 2 * kingdomLevel;
			break;
		}
		return amount;
	}
	
}
