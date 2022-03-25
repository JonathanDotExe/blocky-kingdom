package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Cloud;
import at.jojokobi.blockykingdom.items.CloudParticle;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.RealHealthAccessor;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.SwitchLocatable;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class DeathAngel extends CustomEntity<Stray> implements Attacker, Targeter{
	
	public static final NamespacedEntry DEATH_ANGEL_KEY = new NamespacedEntry(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "death_angel");
	private final LootInventory loot = new LootInventory();

	public DeathAngel(Location place, EntityHandler handler) {
		super(place, handler, null);
		setDespawnTicks(2500);
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, CloudParticle.IDENTIFIER), 1, 3));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Cloud.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(1, new ItemStack(Material.ARROW), 1, 3));
		loot.addItem(new LootItem(1, new ItemStack(Material.BONE), 1, 3));
		loot.addItem(new LootItem(0.25, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 1, 1));

		addComponent(new HealthComponent(new RealHealthAccessor()));
		addComponent(new LootComponent(loot, 10));
		
		addEntityTask(new AttackTask(entity -> entity instanceof Player || entity instanceof Villager || entity instanceof IronGolem));
		addEntityTask(new RandomTask());
	}
	
	@Override
	protected void spawn() {
		super.spawn();
	}
	
	@Override
	public void delete() {
		super.delete();
	}
		
	@Override
	public void loop() {
		super.loop();
//		if (!getEntity().isOnGround()) {
//			getEntity().setGliding(true);
//		}
		//Fall slow
		if (getEntity().getVelocity().getY() < -0.2) {
			getEntity().setVelocity(getEntity().getVelocity().setY(-0.2));
		}
	}

	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		if (event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
	}
	
	@Override
	protected Stray createEntity(Location place) {
		Stray stray = place.getWorld().spawn(place, Stray.class);
		stray.getEquipment().setChestplate(new ItemStack(Material.ELYTRA));
		stray.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		stray.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		stray.getEquipment().setChestplateDropChance(0.0f);
		stray.setLootTable(null);
		
		NMSEntityUtil.clearGoals(stray);
		
		return stray;
	}
	
	public static DeathAngel deserialize (Map<String, Object> map) {
		DeathAngel entity = new DeathAngel(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<String, Object> ());
	}

	@Override
	public boolean isTarget(Entity entity) {
		return entity instanceof Player || entity instanceof Villager || entity instanceof IronGolem;
	}

	@Override
	public void attack(Damageable entity) {
		Vector dir = entity.getLocation().subtract(getEntity().getLocation()).toVector();
		if (dir.lengthSquared() != 0.0) {
			dir.normalize().multiply(2);
		}
		Arrow arrow = getEntity().launchProjectile(Arrow.class, dir);
		arrow.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 5 * 20, 1), true);
	}
	
	@Override
	public Locatable createInRangeLocatable(Entity attacker, Entity target) {
		return new SwitchLocatable(Attacker.super.createInRangeLocatable(attacker, target), new EntityLocatable(target), true, false, true);
	}

	@Override
	public int getAttackDelay() {
		return 20;
	}
	
	@Override
	public boolean chaseWhenInRange() {
		return false;
	}
	
	@Override
	public double getAttackRange() {
		return 40;
	}
	
	@Override
	protected boolean canFly() {
		return true;
	}
	
	@Override
	protected double getFlySpeed() {
		return 0.4;
	}
	
	@Override
	public double getTargetingRange() {
		return 80;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
}
