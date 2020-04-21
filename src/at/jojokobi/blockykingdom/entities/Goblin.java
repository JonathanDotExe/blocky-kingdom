package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.GoblinFang;
import at.jojokobi.blockykingdom.items.GoblinSkin;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.RealHealthAccessor;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class Goblin extends CustomEntity<Zombie> implements Attacker{
	
	public static final NamespacedEntry GOBLIN_SPAWN_KEY = new NamespacedEntry(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "goblin");

	public Goblin(Location place, EntityHandler handler) {
		super(place, handler, null);
		setDespawnTicks(5000);
		LootInventory loot = new LootInventory ();
		loot.addItem(new LootItem(0.7, ItemHandler.getItemStack(GoblinSkin.class), 1, 2));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(GoblinFang.class), 1, 1));
		
		addComponent(new LootComponent(loot, 0));
		addComponent(new HealthComponent(new RealHealthAccessor()));
		
		addEntityTask(new AttackTask(Player.class));
		addEntityTask(new RandomTask());
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	protected Zombie createEntity(Location place) {
		Zombie entity = place.getWorld().spawn(place, Zombie.class);
		
		NMSEntityUtil.clearGoals(entity);
		
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(8.0);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		entity.setRemoveWhenFarAway(true);
		
		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setCustomModelData(3);
		meta.setUnbreakable(true);
		helmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(helmet);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInMainHandDropChance(1);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
		entity.setLootTable(null);
		
		return entity;
	}
	
	@Override
	public void setSave(boolean save) {
		super.setSave(save);
		getEntity().setRemoveWhenFarAway(save);
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<> ());
	}

	@Override
	public void attack(Damageable entity) {
		entity.damage(2);
		if (getEntity().getEquipment().getItemInMainHand().getType() == Material.AIR && entity instanceof LivingEntity && Math.random() < 0.05) {
			getEntity().getEquipment().setItemInMainHand(((LivingEntity) entity).getEquipment().getItemInMainHand().clone());
			getEntity().getEquipment().setItemInMainHandDropChance(1);
			((LivingEntity) entity).getEquipment().setItemInMainHand(null);
		}
	}

	@Override
	public int getAttackDelay() {
		return 8 ;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.6;
	}

}
