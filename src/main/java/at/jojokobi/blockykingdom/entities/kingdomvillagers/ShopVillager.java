package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import at.jojokobi.blockykingdom.gui.shop.Buyable;
import at.jojokobi.blockykingdom.gui.shop.ShopGUI;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;

public abstract class ShopVillager<T extends LivingEntity> extends KingdomVillager<T>{

	private List<Buyable> offers = new ArrayList<>();
	
	public ShopVillager(Location place, EntityHandler handler, Random random, CustomEntityType<?> type) {
		super(place, handler, random, type);
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		if (event.getPlayer().isSneaking()) {
			super.onInteract(event);
		}		
		else if (getKingdomPoint() != null && isLoaded()) {
			event.setCancelled(true);
			ShopGUI gui = new ShopGUI(event.getPlayer(), StatHandler.getInstance().getStats(event.getPlayer()).getCharacterStats(), offers, (buyable) -> {addHappiness(getBuyHappiness()); gainXP(1);});
			getHandler().getGuiHandler().addGUI(gui);
			gui.show();
		}
	}
	
	protected List<Buyable> getOffers() {
		return offers;
	}

	protected void setOffers(List<Buyable> offers) {
		this.offers = offers;
	}

	@Override
	public boolean isTarget(Entity entity) {
		return false;
	}
	
	protected abstract double getBuyHappiness ();

}
