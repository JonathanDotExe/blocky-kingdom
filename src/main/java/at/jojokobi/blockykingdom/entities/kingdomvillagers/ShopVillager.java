package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import at.jojokobi.blockykingdom.gui.shop.Buyable;
import at.jojokobi.blockykingdom.gui.shop.ShopGUI;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public abstract class ShopVillager<T extends LivingEntity> extends KingdomVillager<T>{

	private List<Buyable> offers = new ArrayList<>();
	
	public ShopVillager(Location place, EntityHandler handler, Random random, CustomEntityType<?> type) {
		super(place, handler, random, type);
		//Peaceful AI
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new ReturnToSpawnTask());
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		if (event.getPlayer().isSneaking()) {
			super.onInteract(event);
		}
		//Only owning people can buy from them
		else if (getKingdomPoint() != null && isLoaded() && KingdomHandler.getInstance().getKingdom(getKingdomPoint()).getOwners().contains(event.getPlayer().getUniqueId())) {
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
	
	protected abstract double getBuyHappiness ();

}
