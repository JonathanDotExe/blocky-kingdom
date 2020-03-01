package at.jojokobi.blockykingdom.gui.shop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.players.CharacterStats;

public class BuyableItemStack implements Buyable{
	
	private static final String ITEM_STACK_KEY = "itemStack";
	private static final String PRICE_KEY = "price";
	private static final String MIN_LEVEL_KEY = "level";

	private ItemStack itemStack;
	private int price;
	private int minLevel;


	public BuyableItemStack(ItemStack itemStack, int price, int minLevel) {
		super();
		this.itemStack = itemStack;
		this.price = price;
		this.minLevel = minLevel;
	}


	@Override
	public ItemStack getIcon() {
		return itemStack.clone();
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public void onBuy(Player player, CharacterStats stats) {
		player.getInventory().addItem(itemStack.clone());
	}

	@Override
	public int getMinLevel() {
		return minLevel;
	}

	public static BuyableItemStack deserialize (Map<String, Object> map) {
		ItemStack itemStack = new ItemStack(Material.AIR);
		if (map.get(ITEM_STACK_KEY) instanceof ItemStack) {
			itemStack = (ItemStack) map.get(ITEM_STACK_KEY);
		}
		int minLevel = 10;
		try {
			minLevel = Integer.parseInt(map.get(MIN_LEVEL_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int price = 1000;
		try {
			price = Integer.parseInt(map.get(PRICE_KEY) + "");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return new BuyableItemStack(itemStack, price, minLevel);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put(ITEM_STACK_KEY, itemStack);
		map.put(PRICE_KEY, price);
		map.put(MIN_LEVEL_KEY, minLevel);
		return map;
	}
	
}
