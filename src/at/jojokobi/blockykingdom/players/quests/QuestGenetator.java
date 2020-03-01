package at.jojokobi.blockykingdom.players.quests;

import java.util.Random;

public interface QuestGenetator {
	
	public IQuest generate (Random random, long endTimestamp);

}
