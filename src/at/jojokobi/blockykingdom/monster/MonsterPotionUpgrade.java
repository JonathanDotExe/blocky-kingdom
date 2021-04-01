package at.jojokobi.blockykingdom.monster;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class MonsterPotionUpgrade implements MonsterUpgrade {
	
	private PotionEffectType effectType;
	private int strength;
	private int minLevel;
	private List<Class<? extends Entity>> types;

	public MonsterPotionUpgrade(PotionEffectType effectType, int strength, int minLevel,
			List<Class<? extends Entity>> types) {
		super();
		this.effectType = effectType;
		this.strength = strength;
		this.minLevel = minLevel;
		this.types = types;
	}

	@Override
	public void apply(Entity entity) {
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addPotionEffect(new PotionEffect(effectType, 1000000, strength));
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
