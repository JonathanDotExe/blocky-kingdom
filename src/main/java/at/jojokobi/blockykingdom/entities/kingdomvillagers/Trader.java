package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import at.jojokobi.blockykingdom.gui.shop.Buyable;
import at.jojokobi.blockykingdom.gui.shop.BuyableItemStack;
import at.jojokobi.blockykingdom.gui.shop.BuyableSkillPoint;
import at.jojokobi.blockykingdom.items.CloudParticle;
import at.jojokobi.blockykingdom.items.CursedFigure;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DiamondKatana;
import at.jojokobi.blockykingdom.items.EconomicFigure;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.HealingFigure;
import at.jojokobi.blockykingdom.items.Katana;
import at.jojokobi.blockykingdom.items.MagicTorch;
import at.jojokobi.blockykingdom.items.ProtectingFigure;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.item.ItemHandler;

public class Trader extends ShopVillager<Villager>{
	
	private static final String OFFERS_KEY = "offers";
	
	private static final Map<Integer, List<Buyable>> POSSIBLE_OFFERS;
	
	static {
		Map<Integer, List<Buyable>> offers = new HashMap<>();
		
		offers.put(1, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.BREAD, 5), 750, 0),
				new BuyableItemStack(new ItemStack(Material.COOKED_BEEF, 5), 1500, 5),
				new BuyableItemStack(new ItemStack(Material.STONE_PICKAXE, 1), 250, 0),
				new BuyableItemStack(new ItemStack(Material.STONE_SWORD, 1), 250, 0))));
		offers.put(2, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.CHAINMAIL_BOOTS, 5), 1000, 5),
				new BuyableItemStack(new ItemStack(Material.CHAINMAIL_HELMET, 5), 1000, 5),
				new BuyableItemStack(new ItemStack(Material.IRON_PICKAXE, 1), 1000, 8),
				new BuyableItemStack(new ItemStack(Material.IRON_SWORD, 1), 1000, 8),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Katana.IDENTIFIER), 1500, 8),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1000, 8))));
		offers.put(3, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 5), 1500, 8),
				new BuyableItemStack(new ItemStack(Material.CHAINMAIL_LEGGINGS, 5), 1500, 8),
				new BuyableItemStack(new ItemStack(Material.CARROT, 10), 2000, 10),
				new BuyableItemStack(new ItemStack(Material.POTATO, 10), 2000, 10),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 2000, 10),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, GrapplingHook.IDENTIFIER), 2000, 10))));
		offers.put(4, Collections.unmodifiableList(Arrays.asList(
				new BuyableSkillPoint(),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, MagicTorch.IDENTIFIER), 2000, 10),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, CursedFigure.IDENTIFIER), 2000, 10),
				new BuyableItemStack(new ItemStack(Material.EXPERIENCE_BOTTLE, 16), 2000,  15)
				)));
		offers.put(5, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DiamondKatana.IDENTIFIER), 20000, 20),
				new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, EconomicFigure.IDENTIFIER), 2000, 10),
				new BuyableItemStack(new ItemStack(Material.IRON_INGOT, 16), 2500, 12),
				new BuyableItemStack(new ItemStack(Material.MELON, 10), 1000, 10),
				new BuyableItemStack(new ItemStack(Material.PUMPKIN, 2), 1000, 10)
				)));
		offers.put(6, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.NAME_TAG, 1), 2000, 15),
				new BuyableItemStack(new ItemStack(Material.SADDLE, 1), 10000, 15),
				new BuyableItemStack(new ItemStack(Material.ENDER_PEARL, 1), 2000, 15)
				)));
		ItemStack cloudParticle = ItemHandler.getItemStack(CloudParticle.class);
		cloudParticle.setAmount(16);
		offers.put(7, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.GLOWSTONE, 32), 2000, 10),
				new BuyableItemStack(new ItemStack(Material.END_STONE, 32), 5000, 15),
				new BuyableItemStack(new ItemStack(Material.LAPIS_LAZULI, 32), 2000, 10),
				new BuyableItemStack(cloudParticle, 2000, 15)
				)));
		offers.put(8, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.SLIME_BALL, 8), 2000, 10),
				new BuyableItemStack(new ItemStack(Material.BLAZE_ROD, 3), 5000, 15),
				new BuyableItemStack(new ItemStack(Material.WITHER_SKELETON_SKULL, 1), 10000, 15)
				)));
		offers.put(9, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(ItemHandler.getItemStack(HealingFigure.class), 10000, 20),
				new BuyableItemStack(ItemHandler.getItemStack(ProtectingFigure.class), 5000, 15)
				)));
		offers.put(10, Collections.unmodifiableList(Arrays.asList(
				new BuyableItemStack(new ItemStack(Material.MOOSHROOM_SPAWN_EGG), 5000, 20),
				new BuyableItemStack(new ItemStack(Material.BEDROCK, 16), 10000, 15),
				new BuyableItemStack(new ItemStack(Material.OBSIDIAN, 32), 5000, 15)
				)));
		
		POSSIBLE_OFFERS = Collections.unmodifiableMap(offers);
	}

	public Trader(Location place, EntityHandler handler, Random random) {
		super(place, handler, random, TraderType.getInstance());
		List<Buyable> offers = getOffers();
//		offers.add(new BuyableItemStack(new ItemStack(Material.BREAD, 5), 1000, 0));
//		offers.add(new BuyableItemStack(new ItemStack(Material.COOKED_BEEF, 5), 2000, 5));
//		offers.add(new BuyableItemStack(new ItemStack(Material.IRON_PICKAXE, 1), 1000, 8));
//		offers.add(new BuyableItemStack(new ItemStack(Material.IRON_SWORD, 1), 1000, 8));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Katana.IDENTIFIER), 1500, 8));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Dagger.IDENTIFIER), 1000, 8));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, Hammer.IDENTIFIER), 2000, 10));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, GrapplingHook.IDENTIFIER), 2000, 10));
//		offers.add(new BuyableSkillPoint());
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, DiamondKatana.IDENTIFIER), 20000, 20));
//		offers.add(new BuyableItemStack(new ItemStack(Material.ENDER_PEARL, 1), 2000, 15));
//		offers.add(new BuyableItemStack(new ItemStack(Material.SADDLE, 1), 10000, 15));
//		offers.add(new BuyableItemStack(new ItemStack(Material.NAME_TAG, 1), 2000, 15));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, MagicTorch.IDENTIFIER), 2000, 10));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, CursedFigure.IDENTIFIER), 2000, 15));
//		offers.add(new BuyableItemStack(ItemHandler.getItemStack(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, EconomicFigure.IDENTIFIER), 2000, 10));
		offers.add(createOffer(random, 1));
	}
	
	public Trader(Location place, EntityHandler handler) {
		this(place, handler, new Random());
	}
	
	private Buyable createOffer (Random random, int level) {
		List<Buyable> offers = POSSIBLE_OFFERS.get(level);
		return offers.get(random.nextInt(offers.size()));
	}
	
	@Override
	protected Villager createEntity(Location place) {
		Villager villager = place.getWorld().spawn(place, Villager.class);
		villager.setProfession(Profession.CARTOGRAPHER);
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
	protected void onLevelUp() {
		super.onLevelUp();
		getOffers().add(createOffer(new Random(), getLevel()));
	}

	@Override
	protected double getBuyHappiness() {
		return 0.3;
	}
	
	@Override
	protected void loadData(EntityMapData data) {
		super.loadData(data);
		if (data.get(OFFERS_KEY) instanceof List<?>) {
			getOffers().clear();
			for (Object obj : (List<?>) data.get(OFFERS_KEY)) {
				if (obj instanceof Buyable) {
					getOffers().add((Buyable) obj);
				}
			}
		}
	}
	
	@Override
	protected EntityMapData saveData() {
		Map<String, Object> map = super.saveData().getData();
		map.put(OFFERS_KEY, getOffers());
		return new EntityMapData(map);
	}
	
	public static Trader deserialize (Map<String, Object> map) {
		Trader entity = new Trader(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.6;
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.2;
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
