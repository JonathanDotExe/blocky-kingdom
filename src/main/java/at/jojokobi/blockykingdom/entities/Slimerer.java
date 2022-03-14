package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.RainbowDye;
import at.jojokobi.blockykingdom.items.SlimerersHeart;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.BossBarComponent;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.EntityUtil;
import at.jojokobi.mcutil.entity.HealthAccessor;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.PseudoHealthAccessor;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class Slimerer extends CustomEntity<ArmorStand> implements Attacker, Targeter {

	private int attackState = 0;
	private HealthAccessor health;

	public Slimerer(Location place, EntityHandler handler) {
		super(place, handler, null);
//		setAi(SlimererAI.getInstance());
		setDespawnTicks(10000);
		
		LootInventory loot = new LootInventory();
		
		
		
		loot.addItem(new LootItem(1, new ItemStack(Material.SLIME_BALL), 10, 20));
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, SlimerersHeart.IDENTIFIER), 1, 2));
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, RainbowDye.IDENTIFIER), 1, 0));
		loot.addItem(new LootItem(1, ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Money.IDENTIFIER), 10, 20));
		addComponent(new LootComponent(loot, 200));
		addComponent(new HealthComponent(health = new PseudoHealthAccessor(200)));
		addComponent(new BossBarComponent("Slimerer", BarColor.GREEN, BarStyle.SEGMENTED_20));
		addEntityTask(new AttackTask(Player.class));
	}

	@Override
	protected void spawn() {
		super.spawn();
		Location place = getEntity().getLocation();
		place.setY(getEntity().getWorld().getHighestBlockYAt(place));
		getEntity().teleport(place);
	}

	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		getEntity().getWorld().spawnParticle(Particle.CRIT, getEntity().getEyeLocation(), 10);
		
		if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || Math.random() < (health.getHealth(this) > 75 ? 0.1 : 0.3)) {
			Vector dir = new Vector(Math.random() - 0.5, 0, Math.random() - 0.5);
			dir.normalize();
			dir.multiply(0.5);
			dir.setY(1);
			getEntity().setVelocity(dir);
		}
		
		// Death
		if (health.getHealth(this) <= 0) {
			Location place = getEntity().getLocation();
			//Effect
			place.getWorld().strikeLightningEffect(place);
			place.getWorld().playSound(place, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1, 1);
			place.getWorld().spawnParticle(Particle.SLIME, place.add(0, 1, 0), 50);
			place.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, place, 10);
		}
	}

	@Override
	public void loop() {
		super.loop();

		//Jump
		if (getEntity().isOnGround()) {
			getEntity().setVelocity(getEntity().getVelocity().setY(0.3));
		}
		// Regenerate
		if (getTime() % 8 == 0) {
			health.setHealth(health.getHealth(this) + 5, this);
			getEntity().getWorld().spawnParticle(Particle.HEART,
					getEntity().getLocation().add(Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 4 - 2),
					1);
		}
		//Fire
		if (getEntity().getFireTicks() > 40) {
			getEntity().setFireTicks(40);
		}
	}

	@Override
	protected ArmorStand createEntity(Location place) {
		ArmorStand stand = place.getWorld().spawn(place, ArmorStand.class);
		stand.setVisible(false);
		stand.setCanPickupItems(false);

		// Item
		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setCustomModelData(1);
		helmet.setItemMeta(meta);
		stand.getEquipment().setHelmet(helmet);
		return stand;
	}

	@Override
	protected void loadData(EntityMapData data) {

	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<String, Object>());
	}
	
	@Override
	public void attack(org.bukkit.entity.Damageable entity) {
		Vector dir = entity.getLocation().subtract(getEntity().getLocation()).toVector();
		// Meelee Attack
		if (dir.lengthSquared() < 16) {
			entity.damage((health.getHealth(this) < 75) ? 12 : 8, getEntity());
			dir.normalize();
			dir.setY(1);
			dir.multiply(0.5);
			entity.setVelocity(dir);
		}
		else {
			//Distance attack
			switch (AttackPhase.values()[attackState]) {
			case SPAWN_SLIME_TRAP:
				dir.normalize();
				dir.multiply(3);
				SlimeTrap.launchSlimeTrap(getEntity(), dir, getHandler());
				break;
			case DAMAGE:
				// Attack
				if (EntityUtil.canHit(getEntity(), entity)) {
					if (dir.lengthSquared() != 0.0) {
						dir.normalize();
						dir.multiply(3);
					}
					getEntity().launchProjectile(Arrow.class, dir);
					ThrownPotion potion = getEntity().launchProjectile(ThrownPotion.class, dir);
					ItemStack item = new ItemStack(Material.SPLASH_POTION);
					if (item.getItemMeta() instanceof PotionMeta) {
						PotionMeta meta = (PotionMeta) item.getItemMeta();
						meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, false));
						item.setItemMeta(meta);
					}
					potion.setItem(item);
				}
				//Heal self
				else {
					// Heal
					health.setHealth(health.getHealth(this) + 5, this);
					getEntity().getWorld().strikeLightningEffect(getEntity().getLocation());
				}
				break;
			case SPAWN_SLIMES:
				entity.getWorld().spawn(getEntity().getLocation(), Slime.class).setSize(2);
				break;
			}
			//Update attack state
			attackState++;
			if (attackState >= AttackPhase.values().length) {
				attackState = 0;
			}
		}
	}

	@Override
	public int getAttackDelay() {
		return 4 * 3;
	}

	@Override
	public boolean chaseWhenInRange() {
		return false;
	}

	@Override
	public double getAttackRange() {
		return 15;
	}

	@Override
	protected boolean canFly() {
		return true;
	}

	@Override
	protected double getFlySpeed() {
		return 0.7;
	}

	@Override
	public boolean attackWhenNoLineOfSight() {
		return true;
	}

	@Override
	public boolean isTarget(Entity entity) {
		return entity instanceof Player;
	}
	
	@Override
	public boolean isMultiTarget() {
		return true;
	}

	public static Slimerer deserialize (Map<String, Object> map) {
		Slimerer entity = new Slimerer(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
}

enum AttackPhase {
	SPAWN_SLIME_TRAP, DAMAGE, SPAWN_SLIMES
}
