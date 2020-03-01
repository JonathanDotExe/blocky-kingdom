package at.jojokobi.blockykingdom.kingdoms.siege;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;

public class SiegeZombieTowerSpawner implements SiegeMonsterSpawner{

	@Override
	public void spawn(Location place, EntityHandler handler) {
		LivingEntity entity = place.getWorld().spawn(place, Zombie.class);
		for (int i = 0; i < 9; i++) {
			LivingEntity temp = place.getWorld().spawn(place, Zombie.class);
			temp.setHealth(4.0);
			entity.addPassenger(temp);
			entity = temp;
			ItemStack hand = ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER);
			hand.setAmount(1);
			entity.getEquipment().setItemInOffHand(hand);
			entity.getEquipment().setItemInOffHandDropChance(0.33f);
		}
		entity.addPassenger(place.getWorld().spawn(place, Skeleton.class));
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
