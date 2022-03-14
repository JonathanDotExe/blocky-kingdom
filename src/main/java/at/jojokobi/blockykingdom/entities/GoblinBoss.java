package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.BossBarComponent;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.EntityUtil;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.RealHealthAccessor;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class GoblinBoss extends CustomEntity<Zombie> implements Attacker {

	public static final NamespacedEntry GOBLIN_BOSS_SPAWN_KEY = new NamespacedEntry(
			BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "goblin_boss");
	
	private LootInventory loot = new LootInventory();

	public GoblinBoss(Location place, EntityHandler handler) {
		super(place, handler, null);
		addComponent(new HealthComponent(new RealHealthAccessor()));
		addComponent(new BossBarComponent("Goblin Boss", BarColor.GREEN, BarStyle.SEGMENTED_10));
		loot.addItem(new LootItem(1, new ItemStack(Material.EMERALD), 1, 5));
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(Money.class), 10, 20));
		
		addComponent(new LootComponent(loot, 200));
		addEntityTask(new AttackTask(Player.class));
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	protected Zombie createEntity(Location place) {
		Zombie entity = place.getWorld().spawn(place, Zombie.class);

		NMSEntityUtil.clearGoals(entity);

		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150.0);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		entity.setRemoveWhenFarAway(false);

		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setCustomModelData(6);
		meta.setUnbreakable(true);
		helmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(helmet);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInMainHandDropChance(1);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));

		return entity;
	}
	
	public static GoblinBoss deserialize (Map<String, Object> map) {
		GoblinBoss entity = new GoblinBoss(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	public void loop() {
		super.loop();
		getEntity().setConversionTime(Integer.MAX_VALUE);
	}

	@Override
	protected void loadData(EntityMapData data) {

	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<>());
	}

	@Override
	public void attack(Damageable entity) {
		if (getEntity().getLocation().distance(entity.getLocation()) > 4 || !EntityUtil.canHit(getEntity(), entity)) {
			Goblin goblin = new Goblin(getEntity().getLocation(), getHandler());
			getHandler().addEntity(goblin);
			Vector velocity = entity.getLocation().toVector().subtract(getEntity().getLocation().toVector());
			if (velocity.lengthSquared() != 0) {
				velocity.normalize();
				velocity.multiply(3);
			}
			Snowball ball = getEntity().launchProjectile(Snowball.class, velocity);
			ball.addPassenger(goblin.getEntity());
		} else {
			entity.damage(16);
		}
	}

	@Override
	public double getAttackRange() {
		return 20;
	}

	@Override
	public boolean attackWhenNoLineOfSight() {
		return true;
	}

	@Override
	public int getAttackDelay() {
		return 20;
	}

	@Override
	protected double getSprintSpeed() {
		return 0.4;
	}

}
