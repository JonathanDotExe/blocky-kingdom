package at.jojokobi.blockykingdom.gui.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.gui.ListGUI;

public class ShopGUI extends ListGUI{
	
	private CharacterStats stats;
	private List<Buyable> shopItems = new ArrayList<>();
	private Consumer<Buyable> callback;

	public ShopGUI(Player owner, CharacterStats stats, List<Buyable> shopItems, Consumer<Buyable> callback) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 6));
		this.stats = stats;
		this.shopItems = new ArrayList<>(shopItems);
		this.callback = callback;
		setItemsPerPage(INV_ROW * 5);
		initGUI();
	}
	
	@Override
	protected void initGUI() {
		List<ItemStack> items = new ArrayList<>();
		
		for (Buyable buyable : shopItems) {
			ItemStack item = buyable.getIcon();
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Arrays.asList(" * Price: " + buyable.getPrice() + "$", " * Min Level: " + buyable.getMinLevel()));
			item.setItemMeta(meta);
			items.add(item);
		}
		setItems(items);
		
		fillEmpty(getFiller());
		super.initGUI();
	}

	@Override
	protected void onButtonPress(ItemStack button, ClickType click) {
		int pageIndex = getInventory().first(button);
		if (pageIndex < getItemsPerPage() && pageIndex >= 0) {
			int index = getPage() * getItemsPerPage() + pageIndex;
			if (index < shopItems.size()) {
				Buyable buyable = shopItems.get(index);
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
	}

}
