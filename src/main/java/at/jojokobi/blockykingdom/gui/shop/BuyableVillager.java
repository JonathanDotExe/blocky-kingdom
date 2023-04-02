package at.jojokobi.blockykingdom.gui.shop;

import java.util.Map;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.entity.EntityHandler;

public class BuyableVillager implements Buyable{

	private Function<Location, KingdomVillager<?>> entity;
	private EntityHandler handler;
	private ItemStack icon;
	private int price;
	private int minLevel;
	
	public BuyableVillager(Function<Location, KingdomVillager<?>> entity, EntityHandler handler,
			ItemStack icon, int price, int minLevel) {
		super();
		this.entity = entity;
		this.handler = handler;
		this.icon = icon;
		this.price = price;
		this.minLevel = minLevel;
	}

	@Override
	public ItemStack getDisplayItem() {
		return icon.clone();
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public boolean onBuy(Player player, CharacterStats stats) {
		KingdomPoint point = new KingdomPoint(player.getLocation());
		KingdomVillager<?> villager = entity.apply(player.getLocation());
		if (point.canAddVillager(villager.getVillagerCategory(), handler)) {
			handler.addSavedEntity(villager);
			if (point.toKingdom () != null && point.toKingdom().getState() != KingdomState.UNCLAIMED) {
				villager.setKingdomPoint(point);
				player.sendMessage(villager.getName() + " arrived in " + point.toKingdom().getName() + "!");
			}
			return true;
		}
		else {
			player.sendMessage("The kingdom is already full!");
		}
		return false;
	}

	@Override
	public int getMinLevel() {
		return minLevel;
	}
	
	public static BuyableVillager deserialize (Map<String, Object> map) {
		return null;
	}
	
	@Override
	public Map<String, Object> serialize() {
		return null;
	}

}
