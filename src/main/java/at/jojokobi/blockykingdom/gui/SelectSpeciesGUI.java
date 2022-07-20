package at.jojokobi.blockykingdom.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class SelectSpeciesGUI extends InventoryGUI {

	private Statable statable;

	public SelectSpeciesGUI(Player owner, Statable statable) {
		super(owner, Bukkit.createInventory(owner, INV_ROW, "Select your species!"));
		this.statable = statable;
		initGUI();
	}

	@Override
	protected void initGUI() {
		CharacterSpecies[] species = CharacterSpecies.values();
		for (int i = 0; i < species.length; i++) {
			CharacterSpecies s = species[i];
			addButton(species[i].getIcon(), i, (button, index, click) -> {
				statable.getCharacterStats().setSpecies(s);
				close();
			});
		}
		fillEmpty(getFiller());
	}

}
