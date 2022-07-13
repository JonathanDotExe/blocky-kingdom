package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public class Archer extends WarriorVillager<Villager> {
	
	public static final int ARCHER_PRICE = 2000;

	public Archer(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, ArcherType.getInstance());
		setPrice(ARCHER_PRICE);
		//Attack
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new AttackTask(this::isTarget, 30));
		addEntityTask(new ReturnToSpawnTask());
	}
	
	public Archer(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}

	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.FLETCHER);
		villager.setVillagerType(Type.PLAINS);
		villager.setBreed(false);
		villager.setAdult();
		villager.setAI(true);
		villager.setCanPickupItems(false);
		villager.setRemoveWhenFarAway(false);
		
		MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.BOW), 0, 0, false);
		recipe.addIngredient(new ItemStack(Material.BOW));
		villager.setRecipes(Arrays.asList(recipe));
		
		updateArmor(villager);
		villager.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
		
		villager.getEquipment().setHelmetDropChance(0);
		villager.getEquipment().setChestplateDropChance(0);
		villager.getEquipment().setLeggingsDropChance(0);
		villager.getEquipment().setBootsDropChance(0);
		villager.getEquipment().setItemInMainHandDropChance(0);
		
		NMSEntityUtil.clearGoals(villager);
		
		return villager;
	}
	
	@Override
	public void loop() {
		super.loop();
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.4;
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
	
	private ItemStack getHelmet () {
		return new ItemStack(getLevel() >= 2 ? (getLevel() >= 6 ? Material.IRON_HELMET : Material.LEATHER_HELMET) : Material.AIR);
	}
	
	private ItemStack getChestplate () {
		return new ItemStack(getLevel() >= 3 ? (getLevel() >= 7 ? Material.IRON_CHESTPLATE : Material.LEATHER_CHESTPLATE) : Material.AIR);
	}
	
	private ItemStack getLeggings () {
		return new ItemStack(getLevel() >= 4 ? (getLevel() >= 8 ? Material.IRON_LEGGINGS : Material.LEATHER_LEGGINGS) : Material.AIR);
	}
	
	private ItemStack getBoots () {
		return new ItemStack(getLevel() >= 5 ? (getLevel() >= 9 ? Material.IRON_BOOTS : Material.LEATHER_BOOTS) : Material.AIR);
	}
	
	private void updateArmor (Villager villager) {
		villager.getEquipment().setHelmet(new ItemStack(getHelmet()));
		villager.getEquipment().setChestplate(new ItemStack(getChestplate()));
		villager.getEquipment().setLeggings(new ItemStack(getLeggings()));
		villager.getEquipment().setBoots(new ItemStack(getBoots()));
	}
	
	@Override
	protected void onLevelUp() {
		super.onLevelUp();
		updateArmor(getEntity());
	}
	
	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		super.onProjectileHit(event);
		getHandler().runTaskLater(() -> {
			if (event.getEntity().isDead()) {
				addHappiness(0.3);
				gainXP(1);
			}
		}, 1);
	}

	@Override
	public void attack(Damageable entity) {
		if (isLoaded()) {
			Vector direction = entity.getLocation().subtract(getEntity().getLocation()).toVector();
			if (direction.lengthSquared() != 0.0) {
				direction.normalize();
				direction.multiply(2 + getLevel()/2.0);
				
				getEntity().launchProjectile(Arrow.class, direction);
				if (getLevel() == MAX_LEVEL) {
					getEntity().launchProjectile(Arrow.class, direction);
				}
			}
		}
	}

	@Override
	public int getAttackDelay() {
		return 12 - getLevel()/2;
	}
	
	@Override
	public double getAttackRange() {
		return 30;
	}
	
	@Override
	public boolean chaseWhenInRange() {
		return false;
	}
	
	public static Archer deserialize (Map<String, Object> map) {
		Archer entity = new Archer(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	public VillagerCategory getVillagerCategory() {
		return VillagerCategory.WARRIOR;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	public Function<Integer, Integer> getLevelXPFunction() {
		return HALF_QUADRATIC_LEVEL_FUNCTION;
	}
	
}
