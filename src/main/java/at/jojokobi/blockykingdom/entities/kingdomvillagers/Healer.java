package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public class Healer extends KingdomVillager<Villager> implements Attacker, Targeter{
	
	public static final int HEALER_PRICE = 7500;

	public Healer(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, HealerType.getInstance());
		setPrice(HEALER_PRICE);
		//Approach other villagers
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new AttackTask(this::isTarget, 20));
		addEntityTask(new ReturnToSpawnTask());
	}

	public Healer(Location place, EntityHandler handler) {
		this(place, handler, new Random ());
	}
	
	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.LIBRARIAN);
		villager.setVillagerType(Type.PLAINS);
		villager.setBreed(false);
		villager.setAdult();
		villager.setAI(true);
		villager.setCanPickupItems(false);
		villager.setRemoveWhenFarAway(false);
		
		MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.POTION), 0, 0, false);
		recipe.addIngredient(new ItemStack(Material.POTION));
		villager.setRecipes(Arrays.asList(recipe));
		
		villager.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		villager.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		villager.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		villager.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
		villager.getEquipment().setItemInMainHand(new ItemStack(Material.POTION));
		
		villager.getEquipment().setHelmetDropChance(0);
		villager.getEquipment().setChestplateDropChance(0);
		villager.getEquipment().setLeggingsDropChance(0);
		villager.getEquipment().setBootsDropChance(0);
		villager.getEquipment().setItemInMainHandDropChance(0);

		
		NMSEntityUtil.clearGoals(villager);
		
		return villager;
	}
	
	@Override
	public boolean isTarget(Entity entity) {
		//Target villagers to heal
		CustomEntity<?> custom = getHandler().getCustomEntityForEntity(entity);
		return custom instanceof KingdomVillager<?> && ((KingdomVillager<?>) custom).getEntity().getHealth() < ((KingdomVillager<?>) custom).getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() && getKingdomPoint() != null && getKingdomPoint().equals(((KingdomVillager<?>) custom).getKingdomPoint());
	}
	
	@Override
	public boolean defeatedEnemy(Damageable enemy) {
		return !(enemy instanceof LivingEntity) || ((LivingEntity) enemy).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() <= enemy.getHealth();
	}

	@Override
	public void attack(Damageable entity) {
		if (isLoaded()) {
			Vector direction = entity.getLocation().toVector().subtract(getEntity().getLocation().toVector());
			if (direction.lengthSquared() != 0.0) {
				direction.normalize();
				direction.multiply(5);
			}
			ThrownPotion potion = getEntity().launchProjectile(ThrownPotion.class, direction);
			ItemStack item = new ItemStack(Material.SPLASH_POTION);
			if (item.getItemMeta() instanceof PotionMeta) {
				PotionMeta meta = (PotionMeta) item.getItemMeta();
				meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL,  false, getLevel() > 7));
				item.setItemMeta(meta);
			}
			potion.setItem(item);
			addHappiness(0.1);
			if (Math.random() < 0.2) {
				gainXP(1);
			}
		}
	}

	@Override
	public int getAttackDelay() {
		return 14 - getLevel()/2;
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.6;
	}
	
	@Override
	protected double getSwimSpeed() {
		return 0.2;
	}
	
	@Override
	protected boolean canClimb() {
		return true;
	}
	
	@Override
	protected double getClimbSpeed() {
		return 0.5;
	}
	
	public static Healer deserialize (Map<String, Object> map) {
		Healer entity = new Healer(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	public VillagerCategory getVillagerCategory() {
		return VillagerCategory.HEALER;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
	@Override
	public Function<Integer, Integer> getLevelXPFunction() {
		return HALF_QUADRATIC_LEVEL_FUNCTION;
	}
	
	@Override
	public double getAttackRange() {
		return 3;
	}
	
}
