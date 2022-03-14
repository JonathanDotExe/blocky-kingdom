package at.jojokobi.blockykingdom.monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.items.DiamondKatana;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.MagicTorch;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootItem;

public class MonsterUpgradeHandler implements Listener {
	
	private EntityHandler handler;
	private Random random = new Random();
	private List<MonsterUpgrade> upgrades = new ArrayList<>();
	

	public MonsterUpgradeHandler(EntityHandler handler) {
		super();
		this.handler = handler;
		
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Katana.class), 1, 1), 1, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Katana.class), 1, 1).setEnchant(true), 3, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Smasher.class), 1, 1), 3, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Katana.class), 1, 1).setEnchant(true), 4, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(MagicTorch.class), 1, 1), 4, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Hammer.class), 1, 1), 5, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Hammer.class), 1, 1), 6, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(DiamondKatana.class), 1, 1), 7, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(DiamondKatana.class), 1, 1).setEnchant(true), 8, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));

		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 8), 2, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 8), 4, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 8), 6, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(Money.class), 1, 8), 8, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class)));

		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(DoubleBow.class), 1, 1), 5, Arrays.asList(Skeleton.class, Stray.class)));
		upgrades.add(new MonsterItemUpgrade(new LootItem(1, ItemHandler.getItemStack(DoubleBow.class), 1, 1).setEnchant(true), 7, Arrays.asList(Skeleton.class, Stray.class)));
		
		
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.FIRE_RESISTANCE, 1, 3, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.SPEED, 1, 5, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.DAMAGE_RESISTANCE, 1, 7, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.SPEED, 2, 7, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.INCREASE_DAMAGE, 1, 7, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.DAMAGE_RESISTANCE, 2, 9, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.INCREASE_DAMAGE, 2, 9, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
		upgrades.add(new MonsterPotionUpgrade(PotionEffectType.INCREASE_DAMAGE, 3, 10, Arrays.asList(Zombie.class, Husk.class, ZombieVillager.class, Drowned.class, Skeleton.class, Stray.class, Spider.class, CaveSpider.class)));
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (handler.getCustomEntityForEntity(entity) == null) {
			//Chance
			int level = KingdomHandler.getInstance().getKingdom(entity.getLocation()).getLevel();
			double chance = Math.min(level/20.0, 0.5);
			if (chance > random.nextDouble()) {
				//Get available upgrades
				List<MonsterUpgrade> available = new ArrayList<>();
				for (MonsterUpgrade upgrade : upgrades) {
					if (upgrade.canApply(entity) && level >= upgrade.minLevel()) {
						available.add(upgrade);
					}
				}
				//Upgrade
				if (!available.isEmpty()) {
					MonsterUpgrade upgrade = available.get(random.nextInt(available.size()));
					upgrade.apply(entity);
				}
			}
		}
	}

}
