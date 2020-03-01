package at.jojokobi.blockykingdom.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
			addButton(species[i].getIcon(), i);
		}
		fillEmpty(getFiller());
	}

	@Override
	protected void onButtonPress(ItemStack button, ClickType click) {
		int index = getInventory().first(button);
		if (index < CharacterSpecies.values().length) {
			statable.getCharacterStats().setSpecies(CharacterSpecies.values()[index]);
			setNext(new SelectProfessionGUI(getOwner(), statable));
			close();
		}
	}

}
