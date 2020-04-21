package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.ExecutionersScythe;
import at.jojokobi.blockykingdom.items.MagicTorch;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.BossBarComponent;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class ZombieBoss extends CustomEntity<Zombie> implements Attacker, Targeter{
	
	private int attackState = 0;

	public ZombieBoss(Location place, EntityHandler handler) {
		super(place, handler, ZombieBossType.getInstance());
		setDespawnTicks(-1);
//		setAi(ZombieBossAI.getInstance());
		LootInventory loot = new LootInventory();
		
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, ExecutionersScythe.IDENTIFIER), 1, 1));
		loot.addItem(new LootItem(1, new ItemStack(Material.ROTTEN_FLESH), 5, 10));
		loot.addItem(new LootItem(1, new ItemStack(Material.IRON_INGOT), 10, 32));
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 5, 15));
		addComponent(new LootComponent(loot, 200));
		addComponent(new BossBarComponent("Zombie Boss", BarColor.RED, BarStyle.SEGMENTED_10));
		
		addEntityTask(new AttackTask(Player.class));
	}
	
	@Override
	public void loop() {
		//Respawn
//		if (getSpawnPoint().distanceSquared(getEntity().getLocation()) > 2500 && getEntity().isOnGround()) {
//			getEntity().teleport(getSpawnPoint());
//		}
		
		if (getEntity().isConverting()) {
			getEntity().setConversionTime(100);
		}
		super.loop();
	}
	
	@Override
	protected Zombie createEntity(Location place) {
		Zombie zombie = (Zombie) place.getWorld().spawnEntity(place, EntityType.ZOMBIE);
		//Equipment
		zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		zombie.getEquipment().setItemInMainHand(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, ExecutionersScythe.IDENTIFIER));
		
		zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150.0);
		zombie.setHealth(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		zombie.setRemoveWhenFarAway(false);
		zombie.setCustomName("Zombie Boss");
		
		NMSEntityUtil.clearGoals(zombie);
		return zombie;
	}
	
	@Override
	protected void onDamageOther(EntityDamageByEntityEvent event) {
		super.onDamageOther(event);
//		event.getEntity().setVelocity(event.getEntity().getVelocity().add(new Vector(0, 2, 0)));
	}
	
	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
//		if (event instanceof EntityDamageByEntityEvent && !(((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile)) {
//			setTask(new LegacyAttackTask(((EntityDamageByEntityEvent) event).getDamager()));
//		}
		
		//Fall Damage
		if (event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<>());
	}
	
	public static ZombieBoss deserialize (Map<String, Object> map) {
		ZombieBoss entity = new ZombieBoss(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	public void attack(Damageable entity) {
		double healthPercent = getEntity().getHealth()/getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (attackState == 0) {
			//Spawn Minions
			Location place = getEntity().getLocation();
			createMinions(place);
		}
		else {
			//Hit
			entity.damage(healthPercent > 1/3 ? 6.0 : 10.0);
			Vector dir = entity.getLocation().subtract(getEntity().getLocation()).toVector();
			dir.setY(0);
			if (dir.lengthSquared() != 0) {
				dir.normalize();
				dir.setY(1.5);
				entity.setVelocity(dir);
			}
		}
		
		//Update state
		attackState++;
		if (attackState >= (healthPercent > 1/3 ? 3 : 52)) {
			attackState = 0;
		}
	}
	
	@Override
	public void delete() {
		super.delete();
	}
	
	private void createMinions (Location place) {
		for (int i = 0; i < 2; i++) {
			Zombie minion = place.getWorld().spawn(place, Zombie.class);
			minion.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
			minion.getEquipment().setItemInMainHand(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, MagicTorch.IDENTIFIER));
		}
	}

	@Override
	public int getAttackDelay() {
		return 10;
	}
	
	@Override
	public double getAttackRange() {
		return attackState == 0 ? 20.0 : 3.0;
	}
	
	@Override
	public boolean chaseWhenInRange() {
		return attackState != 0;
	}
	
	@Override
	protected boolean canClimb() {
		return true;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.3;
	}
	
	@Override
	protected double getSwimSpeed() {
		return 0.2;
	}

	@Override
	public boolean isTarget(Entity entity) {
		return entity instanceof Player;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

}
