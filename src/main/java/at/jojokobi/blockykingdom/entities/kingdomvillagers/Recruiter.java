package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.gui.shop.BuyableVillager;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.item.ItemUtil;

public class Recruiter extends ShopVillager<Villager>{

	public Recruiter(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, RecruiterType.getInstance());
	}
	
	public Recruiter(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}
	
	@Override
	protected void spawn() {
		super.spawn();
		getOffers().add(new BuyableVillager(KnightType.getInstance(), getHandler(), ItemUtil.rename(new ItemStack(Material.IRON_SWORD), "Knight"), Knight.KNGIHT_PRICE, 0));
		getOffers().add(new BuyableVillager(ArcherType.getInstance(), getHandler(), ItemUtil.rename(new ItemStack(Material.BOW), "Archer"), Archer.ARCHER_PRICE, 8));
		getOffers().add(new BuyableVillager(HealerType.getInstance(), getHandler(), ItemUtil.rename(new ItemStack(Material.POTION), "Healer"), Healer.HEALER_PRICE, 10));
		getOffers().add(new BuyableVillager(GolemKnightType.getInstance(), getHandler(), ItemUtil.rename(new ItemStack(Material.IRON_BLOCK), "Golem Knight"), GolemKnight.GOLEM_KNIGHT_PRICE, 15));
	}

	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.MASON);
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
	protected double getBuyHappiness() {
		return 0.5;
	}
	
	public static Recruiter deserialize (Map<String, Object> map) {
		Recruiter entity = new Recruiter(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	public VillagerCategory getVillagerCategory() {
		return VillagerCategory.TRADER;
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
