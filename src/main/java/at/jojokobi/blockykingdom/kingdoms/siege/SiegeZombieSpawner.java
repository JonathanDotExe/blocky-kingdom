package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;

public class SiegeZombieSpawner implements SiegeMonsterSpawner {

	@Override
	public void spawn(Location place, EntityHandler handler) {
		Zombie entity = place.getWorld().spawn(place, Zombie.class);
		if (Math.random() > 0.5) {
			ItemStack hand = ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER);
			entity.getEquipment().setItemInOffHand(hand);
			entity.getEquipment().setItemInOffHandDropChance(1.0f);
		}
	}

	@Override
	public int minWave() {
		return 0;
	}

	@Override
	public int minKingdomLevel() {
		return 0;
	}

	@Override
	public int maxAmountPerWave(int level) {
		return level * 3;
	}

	@Override
	public boolean spawnAtVillagers() {
		return true;
	}

	
	
}
