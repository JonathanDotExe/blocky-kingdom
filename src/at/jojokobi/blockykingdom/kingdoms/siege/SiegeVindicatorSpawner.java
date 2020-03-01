package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Vindicator;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;

public class SiegeVindicatorSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler entity) {
		Vindicator evoker = place.getWorld().spawn(place, Vindicator.class);
		ItemStack hand = ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER);
		hand.setAmount((int) Math.random() * 3 + 1);
		evoker.getEquipment().setItemInOffHand(hand);
		evoker.getEquipment().setItemInOffHandDropChance(1.0f);
	}

	@Override
	public int minWave() {
		return 3;
	}

	@Override
	public int minKingdomLevel() {
		return 5;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return  1 + villagers/10;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}
	
}
