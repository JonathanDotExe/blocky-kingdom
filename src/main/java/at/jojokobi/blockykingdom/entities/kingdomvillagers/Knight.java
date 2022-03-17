package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;

public class Knight extends KingdomVillager<Villager> implements Attacker{
	
	public static final int KNGIHT_PRICE = 1000;

	public Knight(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, KnightType.getInstance());
		setPrice(KNGIHT_PRICE);
	}
	
	public Knight(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}

	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.WEAPONSMITH);
		villager.setVillagerType(Type.PLAINS);
		villager.setBreed(false);
		villager.setAdult();
		villager.setAI(true);
		villager.setCanPickupItems(false);
		villager.setRemoveWhenFarAway(false);
		
		MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.IRON_SWORD), 0, 0, false);
		recipe.addIngredient(new ItemStack(Material.IRON_SWORD));
		villager.setRecipes(Arrays.asList(recipe));
		
		updateArmor(villager);
		villager.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
		
		villager.getEquipment().setHelmetDropChance(0);
		villager.getEquipment().setChestplateDropChance(0);
		villager.getEquipment().setLeggingsDropChance(0);
		villager.getEquipment().setBootsDropChance(0);
		villager.getEquipment().setItemInMainHandDropChance(0);
		
		NMSEntityUtil.clearGoals(villager);
		
		return villager;
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
	
	private void updateArmor (Villager villager) {
		villager.getEquipment().setHelmet(new ItemStack(getHelmet()));
		villager.getEquipment().setChestplate(new ItemStack(getChestplate()));
		villager.getEquipment().setLeggings(new ItemStack(getLeggings()));
		villager.getEquipment().setBoots(new ItemStack(getBoots()));
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.4;
	}
	
	@Override
	protected boolean canClimb() {
		return true;
	}
	
	@Override
	protected double getClimbSpeed() {
		return 0.5;
	}

	@Override
	public void attack(Damageable entity) {
		if (isLoaded()) {
			entity.damage(3 + 3 * Math.random() + (entity instanceof Player  ? 3 : 0) + 6 * getLevel()/(double) MAX_LEVEL, getEntity());
		}
	}

	@Override
	public int getAttackDelay() {
		return 10 - getLevel()/2;
	}
	
	@Override
	protected void onLevelUp() {
		super.onLevelUp();
		updateArmor(getEntity());
	}
	
	public static Knight deserialize (Map<String, Object> map) {
		Knight entity = new Knight(null, null);
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
	
}
