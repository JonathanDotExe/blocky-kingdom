package at.jojokobi.blockykingdom.players.quests;

@FunctionalInterface
public interface QuestFunction {
	
	public IQuest create (int maxProgress, int reward, long endTimestamp);

}
