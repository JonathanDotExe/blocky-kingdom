package at.jojokobi.blockykingdom.gui.shop;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.gui.ListGUI;

public class ShopGUI extends ListGUI<Buyable> {
	
	private CharacterStats stats;
	private Consumer<Buyable> callback;

	public ShopGUI(Player owner, CharacterStats stats, List<Buyable> shopItems, Consumer<Buyable> callback) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 6));
		this.stats = stats;
		setItems(shopItems);
		this.callback = callback;
		setItemsPerPage(INV_ROW * 5);
		initGUI();
	}
	
	@Override
	protected void initGUI() {
		super.initGUI();
		fillEmpty(getFiller());
	}

	@Override
	protected void clickItem(Buyable buyable, int index, ClickType click) {
			if (stats.getMoney() >= buyable.getPrice()) {
				if (stats.getLevel() >= buyable.getMinLevel()) {
					if (buyable.onBuy(getOwner(), stats)) {
						stats.setMoney(stats.getMoney() - buyable.getPrice());
					}
					callback.accept(buyable);
				}
				else {
					getOwner().sendMessage("You are too unexperienced to buy this item!");
				}
			}
			else {
				getOwner().sendMessage("You have too less money to buy this item!");
			}
	}

}
