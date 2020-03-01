package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Illusioner;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;

public class SiegeIllusionerSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler entity) {
		Illusioner illusioner = place.getWorld().spawn(place, Illusioner.class);
		ItemStack hand = ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER);
		hand.setAmount((int) Math.random() * 4 + 2);
		illusioner.getEquipment().setItemInOffHand(hand);
		illusioner.getEquipment().setItemInOffHandDropChance(1.0f);
	}

	@Override
	public int minWave() {
		return 4;
	}

	@Override
	public int minKingdomLevel() {
		return 8;
	}

	@Override
	public int maxAmountPerWave(int villagers) {
		return 2;
	}

	@Override
	public boolean spawnAtVillagers() {
		return false;
	}
	
}
