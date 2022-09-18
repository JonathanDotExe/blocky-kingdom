package at.jojokobi.blockykingdom.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.quests.IQuest;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class QuestGUI extends InventoryGUI{
	
	private static final int START_INDEX = 3;
	
	private CharacterStats stats;

	public QuestGUI(Player owner, CharacterStats stats) {
		super(owner, Bukkit.createInventory(owner, INV_ROW));
		this.stats = stats;
		initGUI();
	}

	@Override
	protected void initGUI() {
		int i = 0;
		for (IQuest quest : stats.getQuests()) {
			addButton(quest.toItemStack(getOwner()), START_INDEX + i, (button, index, click) -> {
				if (quest.expired()) {
					stats.removeQuest(quest);
					close ();
				}
				else if (quest.isDone(getOwner())) {
					stats.removeQuest(quest);
					stats.setMoney(stats.getMoney() + quest.getReward());
					close ();
				}
			});
			i++;
		}
		fillEmpty(getFiller());
	}

}
