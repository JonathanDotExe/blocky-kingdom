package at.jojokobi.blockykingdom.kingdoms;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import at.jojokobi.mcutil.loot.LootItem;

public class MonsterItemUpgrade implements KingdomMonsterUpgrade {
	
	private LootItem item;
	private int minLevel;
	private List<Class<? extends Entity>> types;


	public MonsterItemUpgrade(LootItem item, int minLevel, List<Class<? extends Entity>> types) {
		super();
		this.item = item;
		this.minLevel = minLevel;
		this.types = types;
	}

	@Override
	public void apply(Entity entity) {
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).getEquipment().setItemInMainHand(item.generateItemStack(new Random()));
		}
	}

	@Override
	public boolean canApply(Entity entity) {
		return types.stream().anyMatch(c -> c.isInstance(entity));
	}

	@Override
	public int minLevel() {
		return minLevel;
	}

}
