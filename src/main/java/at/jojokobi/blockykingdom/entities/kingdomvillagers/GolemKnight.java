package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.InteractEntityTask;
import at.jojokobi.mcutil.entity.ai.RandomAroundPlaceTask;
import at.jojokobi.mcutil.entity.ai.RandomTimeCondition;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public class GolemKnight extends WarriorVillager<IronGolem> {
	
	public static final int GOLEM_KNIGHT_PRICE = 10000;

	public GolemKnight(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, null);
		setPrice(GOLEM_KNIGHT_PRICE);
		//Attack
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new AttackTask(this::isTarget, 25));
		addEntityTask(new InteractEntityTask(new RandomTimeCondition(2 * 4, 15 * 4, 5 * 4, 15 * 4), 20));
		addEntityTask(new RandomAroundPlaceTask(e -> e.getSpawnPoint(), 30, 70, 8, false, false, 16));
		addEntityTask(new ReturnToSpawnTask());
	}
	
	public GolemKnight(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}

	@Override
	protected IronGolem createEntity(Location place) {
		IronGolem golem = place.getWorld().spawn(place, IronGolem.class);
		golem.setAI(true);
		golem.setCanPickupItems(false);
		golem.setRemoveWhenFarAway(false);
		
		updateArmor(golem);
		golem.getEquipment().setItemInMainHand(new ItemStack(Material.POPPY));
		
		golem.getEquipment().setHelmetDropChance(0);
		golem.getEquipment().setChestplateDropChance(0);
		golem.getEquipment().setLeggingsDropChance(0);
		golem.getEquipment().setBootsDropChance(0);
		golem.getEquipment().setItemInMainHandDropChance(0);
		
		NMSEntityUtil.clearGoals(golem);
		
		return golem;
	}
	
	private ItemStack getHelmet () {
		return new ItemStack(getLevel() >= 2 ? (getLevel() >= 6 ? Material.DIAMOND_HELMET : Material.IRON_HELMET) : Material.AIR);
	}
	
	private ItemStack getChestplate () {
		return new ItemStack(getLevel() >= 3 ? (getLevel() >= 7 ? Material.DIAMOND_CHESTPLATE : Material.IRON_CHESTPLATE) : Material.AIR);
	}
	
	private ItemStack getLeggings () {
		return new ItemStack(getLevel() >= 4 ? (getLevel() >= 8 ? Material.DIAMOND_LEGGINGS : Material.IRON_LEGGINGS) : Material.AIR);
	}
	
	private ItemStack getBoots () {
		return new ItemStack(getLevel() >= 5 ? (getLevel() >= 9 ? Material.DIAMOND_BOOTS : Material.IRON_BOOTS) : Material.AIR);
	}
	
	private void updateArmor (IronGolem golem) {
		golem.getEquipment().setHelmet(new ItemStack(getHelmet()));
		golem.getEquipment().setChestplate(new ItemStack(getChestplate()));
		golem.getEquipment().setLeggings(new ItemStack(getLeggings()));
		golem.getEquipment().setBoots(new ItemStack(getBoots()));
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.1;
	}
	
	@Override
	protected double getSprintSpeed() { 
		return 0.7;
	}
	
	@Override
	protected boolean canSwim() {
		return false;
	}

	@Override
	public void attack(Damageable entity) {
		if (isLoaded()) {
			entity.damage(5 + 5 * Math.random() + (entity instanceof Player ? 5 : 0) + 10 * getLevel()/(double) MAX_LEVEL, getEntity());
			entity.setVelocity(new Vector(0, 1.5, 0));
		}
	}

	@Override
	public int getAttackDelay() {
		return 12 - getLevel()/2;
	}
	
	@Override
	protected void onLevelUp() {
		super.onLevelUp();
		if (getEntity() != null ) {
			updateArmor(getEntity());
		}
	}
	
	public static GolemKnight deserialize (Map<String, Object> map) {
		GolemKnight entity = new GolemKnight(null, null);
		entity.load(map);
		return entity;
	}
	
//	@Override
//	public void saveData(Element element, Document document) {
//		super.saveData(element, document);
//		//Name
//		Element name = document.createElement(NAME_TAG);
//		name.setTextContent(this.name);
//		element.appendChild(name);
//		//Kingdom Point
//		if (kingdomPoint != null) {
//			Element kingdom = document.createElement(KINGDOM_TAG);
//			kingdomPoint.saveToXML(kingdom, document);
//			element.appendChild(kingdom);
//		}
//	}
//	
//	@Override
//	public void parseData(Element element) {
//		super.parseData(element);
//		//Name
//		Node name = element.getElementsByTagName(NAME_TAG).item(0);
//		if (name != null) {
//			this.name = name.getTextContent();
//		}
//		System.out.println("Loaded " + name);
//		//Kingdom Point
//		Node kingdom = element.getElementsByTagName(KINGDOM_TAG).item(0);
//		if (kingdom != null && kingdom.getNodeType() == Node.ELEMENT_NODE) {
//			this.kingdomPoint = KingdomPoint.fromXML((Element) kingdom);
//		}
//	}
//
//	@Override
//	public boolean isTarget(Entity entity) {
//		return (entity instanceof Monster && !(entity instanceof Creeper) && !(entity instanceof PigZombie)) || (kingdomPoint == null && entity instanceof Player);
//	}
	
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

	@Override
	public double getAttackRange() {
		return 3;
	}
	
}
