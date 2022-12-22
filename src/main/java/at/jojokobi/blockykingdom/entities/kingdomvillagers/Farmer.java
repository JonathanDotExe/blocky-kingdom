package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.ai.CarryTask;
import at.jojokobi.mcutil.entity.ai.ExamineBlockTask;
import at.jojokobi.mcutil.entity.ai.InteractEntityTask;
import at.jojokobi.mcutil.entity.ai.RandomAroundPlaceTask;
import at.jojokobi.mcutil.entity.ai.RandomTimeCondition;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public class Farmer extends KingdomVillager<Villager> {
	
	public static final int FARMER_PRICE = 2500;
	
	static final List<Material> CROP_ITEMS = Arrays.asList(Material.CARROT, Material.BEETROOT, Material.WHEAT, Material.WHEAT_SEEDS, Material.POTATO, Material.POISONOUS_POTATO);
	static final List<Material> CROP_BLOCKS = Arrays.asList(Material.CARROTS, Material.BEETROOTS, Material.POTATOES, Material.WHEAT);
	
	public Farmer(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, RecruiterType.getInstance());
		//Peaceful AI
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new CarryTask(e -> e.getType() == EntityType.DROPPED_ITEM && CROP_ITEMS.contains(((Item) e).getItemStack().getType()), 15)); //Carry the dropped items
		addEntityTask(new ExamineBlockTask(b -> CROP_BLOCKS.contains(b.getType()) && ((Ageable) b.getBlockData()).getAge() == ((Ageable) b.getBlockData()).getMaximumAge(), b -> b.breakNaturally(), 15));
		addEntityTask(new ExamineBlockTask(b -> b.getType() == Material.CHEST && !getEntity().getPassengers().isEmpty(), b -> {
			Chest chest = (Chest) b.getState();
			for (Entity e : getEntity().getPassengers()) {
				if (e instanceof Item) {
					Item i = (Item) e;
					chest.getInventory().addItem(i.getItemStack());
					e.remove();
				}
			}
			getEntity().eject();
		}, 15));
		addEntityTask(new InteractEntityTask(new RandomTimeCondition(1 * 4, 10 * 4, 5 * 4, 15 * 4), 10));
		addEntityTask(new RandomAroundPlaceTask(e -> e.getSpawnPoint(), 20, 50, 8, false, false));
		addEntityTask(new ReturnToSpawnTask());
	}
	
	public Farmer(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}
	
	@Override
	protected void spawn() {
		super.spawn();
	}

	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.FARMER);
		villager.setVillagerType(Type.PLAINS);
		villager.setBreed(false);
		villager.setAdult();
		villager.setAI(true);
		villager.setCanPickupItems(false);
		villager.setRemoveWhenFarAway(false);
		
		villager.setRecipes(Arrays.asList());
		
		NMSEntityUtil.clearGoals(villager);
		
		return villager;
	}
	
	@Override
	public int getPrice() {
		return FARMER_PRICE;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.6;
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.2;
	}
	
	public static Farmer deserialize (Map<String, Object> map) {
		Farmer entity = new Farmer(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	public VillagerCategory getVillagerCategory() {
		return VillagerCategory.WORKER;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
	@Override
	public Function<Integer, Integer> getLevelXPFunction() {
		return LINEAR_LEVEL_FUNCTION;
	}

}
