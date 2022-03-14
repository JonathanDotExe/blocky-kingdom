package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Witch;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;

public class SiegeWitchSpawner implements SiegeMonsterSpawner{

	@Override
	public void spawn(Location place, EntityHandler handler) {
		Witch entity = place.getWorld().spawn(place, Witch.class);
		
		ItemStack hand = ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER);
		hand.setAmount(2);
		entity.getEquipment().setItemInOffHand(hand);
		entity.getEquipment().setItemInOffHandDropChance(1.0f);
	}

	@Override
	public int minWave() {
		return 3;
	}

	@Override
	public int minKingdomLevel() {
		return 3;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return 1 + villagers/30;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}

}
