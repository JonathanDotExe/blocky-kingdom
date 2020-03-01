package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.AirGrenade;
import at.jojokobi.blockykingdom.items.Cloud;
import at.jojokobi.blockykingdom.items.CloudParticle;
import at.jojokobi.blockykingdom.items.FrozenLightning;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.Sunglasses;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.EntityUtil;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.SwitchLocatable;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class Airhead extends CustomEntity<Skeleton> implements Attacker, Targeter{
	
//	private static final int MAX_ATTACK_STATE = 5;
	
	private static final int SPAWN_DEATH_ANGELS = 1;
	private static final int SHOOT_THUNDER = 2;
	private static final int SHOOT_WIND = 4;
	
	private static final LootInventory LOOT = new LootInventory();
	
	static {
		LOOT.addItem(new LootItem(1, ItemHandler.getCustomItem(Sunglasses.class).createItem(), 1, 1));
		LOOT.addItem(new LootItem(1, ItemHandler.getCustomItem(FrozenLightning.class).createItem(), 1, 2));
		LOOT.addItem(new LootItem(0.5, ItemHandler.getCustomItem(Cloud.class).createItem(), 1, 3));
		LOOT.addItem(new LootItem(1, ItemHandler.getCustomItem(CloudParticle.class).createItem(), 5, 15));
		LOOT.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 5, 15));
	}
	
	private int attackState = 0;
	private BossBar bossBar;
	
	public Airhead(Location place, EntityHandler handler) {
		super(place, handler, null);
		setDespawnTicks(10000);
//		setAi(AirheadAI.getInstance());
		addEntityTask(new AttackTask(Player.class));
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<String, Object> ());
	}
	@Override
	protected Skeleton createEntity(Location place) {
		Skeleton skeleton = place.getWorld().spawn(place, Skeleton.class);
		skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(400);
		skeleton.setHealth(400);
		skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000000, 1, true, false), true);
		skeleton.setLootTable(null);
		skeleton.setSilent(true);
		skeleton.setRemoveWhenFarAway(false);
		skeleton.setCustomName("Air Head");
		
		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setUnbreakable(true);
		meta.setCustomModelData(2);
		helmet.setItemMeta(meta);
		
		skeleton.getEquipment().setHelmet(helmet);
		skeleton.getEquipment().setHelmetDropChance(0);
		skeleton.getEquipment().setItemInMainHand(ItemHandler.getCustomItem(Cloud.class).createItem());
		skeleton.getEquipment().setItemInOffHand(ItemHandler.getCustomItem(Cloud.class).createItem());
		
		NMSEntityUtil.clearGoals(skeleton);
		
		bossBar = Bukkit.createBossBar("Air Head", BarColor.BLUE, BarStyle.SEGMENTED_20);
		
		return skeleton;
	}
	
	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		if (event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
		else if (getEntity().getHealth() - event.getFinalDamage() <= 0.0) {
			Location place = getEntity().getLocation();
			//Drop
			for (ItemStack item : LOOT.populateLoot(new Random(), null)) {
				place.getWorld().dropItem(place, item);
			}
			//Experience
			for (int i = 0; i < 20; i++) {
				place.getWorld().spawn(place, ExperienceOrb.class).setExperience(10);
			}
		}
		else {
			//Update Boss Bar
			bossBar.setProgress((getEntity().getHealth() - event.getFinalDamage())/getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}
	}
	
	@Override
	public void delete() {
		bossBar.removeAll();
		super.delete();
	}

	@Override
	public boolean isTarget(Entity entity) {
		return entity instanceof Player;
	}

	@Override
	public void attack(org.bukkit.entity.Damageable entity) {
		if (!EntityUtil.canHit(getEntity(), entity) && !(attackState < SPAWN_DEATH_ANGELS)) {
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 5, 1));
			}
		}
		else if (getEntity().getLocation().distanceSquared(entity.getLocation()) > 4 * 4) {
//			switch (attackState % MAX_ATTACK_STATE) {
//			case SPAWN_DEATH_ANGELS:
//				getHandler().addEntity(new DeathAngel(getEntity().getLocation(), getHandler()));
//				break;
//			case SHOOT_WIND:
//				Snowball ball = getEntity().launchProjectile(Snowball.class);
//				ball.setVelocity(ball.getVelocity().multiply(3));
//				ArmorStand stand = ItemHandler.getCustomItem(ProtectingFigure.class).getItemEntity(ball.getLocation(), new Vector());
//				ball.addPassenger(stand);
//				getHandler().runTaskLater(() -> stand.remove(), 20 * 10);
//				break;
//			case SHOOT_THUNDER:
//				ItemHandler.getCustomItem(ThunderWand.class).shootThunderProjectile(getEntity());
//				break;
//			}
			if (attackState < SPAWN_DEATH_ANGELS) {
				DeathAngel angel = new DeathAngel(getEntity().getLocation(), getHandler());
				angel.setDespawnTicks(500);
				getHandler().addEntity(angel);
			}
			else if (attackState <= SHOOT_THUNDER) {
//				Snowball ball = ItemHandler.getCustomItem(ThunderWand.class).shootThunderProjectile(getEntity());
//				Vector velocity = entity.getLocation().toVector() .subtract(getEntity().getLocation().toVector());
//				if (velocity.length() != 0) {
//					velocity.normalize();
//					velocity.multiply(5);
//				}
//				ball.setVelocity(velocity);
				entity.getWorld().strikeLightning(entity.getLocation());
			}
			else if (attackState < SHOOT_WIND) {
//				Snowball ball = getEntity().launchProjectile(Snowball.class);
//				ball.setVelocity(ball.getVelocity().multiply(3));
//				ArmorStand stand = ItemHandler.getCustomItem(ProtectingFigure.class).getItemEntity(ball.getLocation(), new Vector());
//				ball.addPassenger(stand);
//				getHandler().runTaskLater(() -> stand.remove(), 20 * 10);
				Snowball ball = ItemHandler.getCustomItem(AirGrenade.class).shootAirGrenade(getEntity());
				Vector velocity = entity.getLocation().toVector() .subtract(getEntity().getLocation().toVector());
				if (velocity.length() != 0) {
					velocity.normalize();
					velocity.multiply(5);
				}
				ball.setVelocity(velocity);
			}
		}
		else {
			entity.damage(20, getEntity());
			Vector velocity = entity.getLocation().subtract(getEntity().getLocation()).toVector();
			if (velocity.lengthSquared() != 0) {
				velocity.normalize();
				velocity.multiply(3);
				entity.setVelocity(velocity);
			}
		}
		
		if (entity instanceof Player) {
			bossBar.addPlayer((Player) entity);
		}
	}
	
	@Override
	public void attack(Set<org.bukkit.entity.Damageable> entities) {
		Attacker.super.attack(entities);
		attackState++;
		if (attackState >= SHOOT_THUNDER) {
			attackState = 0;
		}
	}
	
	@Override
	public boolean isMultiTarget() {
		return true;
	}
	
	@Override
	public boolean chaseWhenInRange() {
		return false;
	}

	@Override
	public int getAttackDelay() {
		return 16;
	}
	
	@Override
	public Locatable createInRangeLocatable(Entity attacker, Entity target) {
		return new SwitchLocatable(Attacker.super.createInRangeLocatable(attacker, target), new EntityLocatable(target), true, false, true);
	}
	
	@Override
	public boolean attackWhenNoLineOfSight() {
		return true;
	}
	
	@Override
	public double getAttackRange() {
		return 20;
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
	
	public static Airhead deserialize (Map<String, Object> map) {
		Airhead entity = new Airhead(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

}
