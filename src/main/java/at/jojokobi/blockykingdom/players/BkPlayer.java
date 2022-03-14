package at.jojokobi.blockykingdom.players;

public class BkPlayer implements Statable{
	
	private CharacterStats stats;
	private boolean isNew;

	public BkPlayer(CharacterStats stats, boolean isNew) {
		this.stats = stats;
		this.isNew = isNew;
	}
	
	public BkPlayer(CharacterProfession profession, CharacterSpecies species, boolean isNew) {
		this(new CharacterStats(profession, species), isNew);
	}

	@Override
	public CharacterStats getCharacterStats() {
		return stats;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
	
}
