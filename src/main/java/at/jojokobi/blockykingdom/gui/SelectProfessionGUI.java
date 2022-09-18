package at.jojokobi.blockykingdom.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class SelectProfessionGUI extends InventoryGUI {

	private Statable statable;

	public SelectProfessionGUI(Player owner, Statable statable) {
		super(owner, Bukkit.createInventory(owner, INV_ROW, "Select your profession!"));
		this.statable = statable;
		initGUI();
	}

	@Override
	protected void initGUI() {
		CharacterProfession[] species = CharacterProfession.values();
		for (int i = 0; i < species.length; i++) {
			CharacterProfession profession = species[i];
			addButton(species[i].getIcon(), i, (button, index, click) -> {
				statable.getCharacterStats().setProfession(profession);
				close();
			});
		}
		fillEmpty(getFiller());
	}

}
