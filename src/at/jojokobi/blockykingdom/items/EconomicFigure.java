package at.jojokobi.blockykingdom.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.gui.FigureShopGUI;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.item.ItemUtil;
import at.jojokobi.mcutil.item.PersistentUUIDDataType;
import at.jojokobi.mcutil.item.PlaceableItem;
import at.jojokobi.mcutil.item.Rotation;

public class EconomicFigure extends PlaceableItem{

	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Economic Figure";
	public static final String IDENTIFIER = "economic_figure";
	public static final short META = 4;
	public static final Material ITEM = Material.IRON_SHOVEL;
	
	public static final String OWNER_TAG = "owner";
	public static final String MONEY_TAG = "money";
	public static final String PRICE_TAG = "price";
	
	private final NamespacedKey ownerKey;
	private final NamespacedKey moneyKey;
	private final NamespacedKey priceKey;

	public EconomicFigure(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		ownerKey = new NamespacedKey(plugin, OWNER_TAG);
		moneyKey = new NamespacedKey(plugin, MONEY_TAG);
		priceKey = new NamespacedKey(plugin, PRICE_TAG);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setRotation(Rotation.CARDINAL);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape(" G ", "SWS", "SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('W', Material.OAK_WOOD);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		return recipe;
	}
	
	public ItemStack setOwner (ItemStack item, UUID owner) {
		setItemData(item, ownerKey, PersistentUUIDDataType.getInstance(), owner);
		return item;
	}
	
	public UUID getOwner (ItemStack item) {
		return item.getItemMeta().getPersistentDataContainer().getOrDefault(ownerKey, PersistentUUIDDataType.getInstance(), new UUID (0, 0));
	}
	
	public ItemStack setMoney (ItemStack item, int money) {
		setItemDataInt (item, moneyKey, money);
		return item;
	}
	
	public int getMoney (ItemStack item) {
		return item.getItemMeta().getPersistentDataContainer().getOrDefault(moneyKey, PersistentDataType.INTEGER, 0);
	}
	
	public ItemStack setPrice (ItemStack item, int price) {
		setItemDataInt (item, priceKey, price);
		return item;
	}
	
	public int getPrice (ItemStack item) {
		return item.getItemMeta().getPersistentDataContainer().getOrDefault(priceKey, PersistentDataType.INTEGER,0);
	}
	
	@Override
	public ItemStack createItem() {
		ItemStack item = super.createItem();
		setMoney(item, 0);
		setPrice(item, 0);
		setOwner(item, new UUID(0, 0));
		return item;
	}
	
	@Override
	protected void fixItem(ItemStack item) {
		super.fixItem(item);
		//Money
		int money = ItemUtil.getNBTInt(item, MONEY_TAG);
		ItemUtil.removeNBTTag(item, MONEY_TAG);
		setMoney(item, money);
		//Owner
		UUID owner = UUID.fromString(ItemUtil.getNBTString(item, OWNER_TAG));
		ItemUtil.removeNBTTag(item, OWNER_TAG);
		setOwner(item, owner);
		//Price
		int price = ItemUtil.getNBTInt(item, PRICE_TAG);
		ItemUtil.removeNBTTag(item, PRICE_TAG);
		setPrice(item, price);
	}
	
	@Override
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		super.onPlayerInteract(event);
		if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
			Entity[] entities = event.getClickedBlock().getChunk().getEntities();
			Location loc = event.getClickedBlock().getLocation();
			boolean found = false;
			for (int i = 0; i < entities.length && !found; i++) {
				Location entityLoc = entities[i].getLocation().add(0, -0.1, 0);
				if (loc.getBlockX() == entityLoc.getBlockX() && loc.getBlockY() == entityLoc.getBlockY() && loc.getBlockZ() == entityLoc.getBlockZ()) {
					if (isItemEntity(entities[i])) {
						found = true;
						ItemUtil.printTagCompount(((ArmorStand) entities[i]).getEquipment().getHelmet());
						if (!event.getPlayer().getUniqueId().equals(getOwner(((ArmorStand) entities[i]).getEquipment().getHelmet()))) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity (PlayerInteractAtEntityEvent event) {
		if (isItemEntity(event.getRightClicked())) {
			Location chestLoc = event.getRightClicked().getLocation().add(0, -0.1, 0);
			if (chestLoc.getBlock().getState() instanceof Chest) {
				FigureShopGUI gui = new FigureShopGUI(event.getPlayer(), StatHandler.getInstance().getStats(event.getPlayer()).getCharacterStats(), this, ((ArmorStand) event.getRightClicked()), (Chest) chestLoc.getBlock().getState(), plugin.getInputHandler());
				gui.show();
				event.getPlayer().sendMessage(event.getPlayer().getUniqueId() + "/" + getOwner(((ArmorStand) event.getRightClicked()).getEquipment().getHelmet()));
				plugin.getGuiHandler().addGUI(gui);
			}
		}
	}
	
	@Override
	protected boolean canRemove(EntityDamageEvent event) {
		boolean canRemove = false;
		if (event instanceof EntityDamageByEntityEvent) {
			if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
				Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
				ItemStack item = ((ArmorStand) event.getEntity()).getEquipment().getHelmet();
				
				if (getOwner(item).equals(player.getUniqueId())) {
					canRemove = true;
				}
			}
		}
		return canRemove;
	}
	
	@Override
	public ArmorStand getItemEntity(Location place, Vector facing) {
		ArmorStand stand = super.getItemEntity(place, facing);
		stand.setGravity(false);
		return stand;
	}

	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}
	
	@Override
	protected void onPlaceItem(Player player, ArmorStand entity) {
		super.onPlaceItem(player, entity);
		entity.getEquipment().setHelmet(setOwner(entity.getEquipment().getHelmet(), player.getUniqueId()));
	}

}
