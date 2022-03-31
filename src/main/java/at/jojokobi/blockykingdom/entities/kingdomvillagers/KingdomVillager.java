package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.blockykingdom.kingdoms.RandomWordGenerator;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.ReturnToSpawnTask;

public abstract class KingdomVillager<T extends LivingEntity> extends CustomEntity<T> implements Targeter {
	
	public static final Function<Integer, Integer> LINEAR_LEVEL_FUNCTION = l -> l;
	public static final Function<Integer, Integer> HALF_QUADRATIC_LEVEL_FUNCTION = l -> (l * l) / 2;

	public static final String NAME_TAG = "name";
	public static final String KINGDOM_TAG = "kingdom";
	public static final String HAPPINESS_TAG = "happiness";
	public static final String LEVEL_TAG = "level";
	public static final String XP_TAG = "xp";
	
	public static final int MAX_LEVEL = 10;

	private int reloadTime = 0;
	private String name;
	private KingdomPoint kingdomPoint;

	private int price = 1000;
	private double happiness = 0.0;

	private int level = 1;
	private int xp = 0;

	public KingdomVillager(Location place, EntityHandler handler, Random random, CustomEntityType<?> type) {
		super(place, handler, type);
		name = new RandomWordGenerator().generateWord(random, 2, 10);
		setDespawnTicks(5000);
		setTeleportToGoal(true);
		addEntityTask(new VillagerFollowTask());
		addEntityTask(new AttackTask(this::isTarget, 20));
		addEntityTask(new ReturnToSpawnTask());
	}

	@Override
	public void loop() {
		super.loop();
		if (reloadTime > 0) {
			int time = reloadTime / 4;
			getEntity().setCustomName("Reloading - " + time / 60 + "m " + time % 60 + "s");
			reloadTime--;
		} else {
			getEntity().setCustomName(name + " [Lvl. " + level + " " + xp + "/" + getLevelXP() + "]");
		}
		if (getEntity().getLocation().getY() < 0) {
			getEntity().setHealth(getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			reloadTime = 4 * 60 * 10;
			if (kingdomPoint != null) {
				getEntity().teleport(getSpawnPoint());
			}
			addHappiness(-1);
		}
	}

	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		
		if (event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.SUFFOCATION || reloadTime > 0) {
			event.setCancelled(true);
		} else if (Math.round(getEntity().getHealth() - event.getFinalDamage()) <= 0) {
			event.setDamage(0);
			event.setCancelled(true);
			getEntity().setHealth(getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			reloadTime = 4 * 60 * 10;
			if (kingdomPoint != null) {
				getEntity().teleport(getSpawnPoint());
			}
			addHappiness(-1);
		}
	}

	@Override
	protected void onDamageOther(EntityDamageByEntityEvent event) {
		super.onDamageOther(event);
		if (event.getEntity() instanceof Damageable && Math.round(((Damageable) event.getEntity()).getHealth() - event.getFinalDamage()) <= 0) {
			addHappiness(0.2);
			gainXP(1);
		}
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		super.onInteract(event);
//			if (following) {
//				setTask(null);
//				setGoal(null);
//				following = false;
//				if (kingdomPoint == null) {
//					KingdomPoint kingdomPoint = new KingdomPoint(getEntity().getLocation());
//					Kingdom kingdom = kingdomPoint.toKingdom();
//					CharacterStats stats = StatHandler.getInstance().getStats(event.getPlayer()).getCharacterStats();
//					if (kingdom != null && kingdomPoint.canAddVillager(getVillagerCategory(), getHandler())) {
//						if (kingdom.isOwner(event.getPlayer().getUniqueId()) && stats.getMoney() >= price) {
//							this.kingdomPoint = kingdomPoint;
//							stats.setMoney(stats.getMoney() - price);
//							event.getPlayer().sendMessage(name + " is now fighting for " + kingdom.getName() + ".");
//							setSave(true);
//							setDespawnTicks(-1);
//						} else {
//							event.getPlayer().sendMessage("You have to own this kingdom and be able to pay " + price + "$!");
//						}
//					}
//					else {
//						event.getPlayer().sendMessage("The kingdom is already full!");
//					}
//				} else {
//					event.getPlayer().sendMessage(name + " is no longer following you! " + isSave());
//				}
//				setSpawnPoint(getEntity().getLocation());
//			} else {
//				if (kingdomPoint == null || (reloadTime <= 0
//						&& KingdomHandler.getInstance().getKingdom(kingdomPoint) != null && KingdomHandler.getInstance()
//								.getKingdom(kingdomPoint).isOwner(event.getPlayer().getUniqueId()))) {
//					reloadTime = 0;
//					following = true;
//					setTask(new LegacyFollowTask(event.getPlayer(), -1));
//					event.getPlayer().sendMessage(name + " is following you! Happiness: " + happiness);
//					//Feed
//					if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BREAD) {
//						event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
//						getEntity().setHealth(Math.min(getEntity().getHealth() + 4.0, getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
//						addHappiness(0.3);
//						event.getPlayer().sendMessage("[" + getName() + "] Thank you the bread was delicous!");
//						getEntity().getEyeLocation().getWorld().spawnParticle(Particle.HEART, getEntity().getEyeLocation(), 5);
//					}
//				}
//			}
		if (event.getPlayer().isSneaking()) {
			if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BREAD) {
				event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
				getEntity().setHealth(Math.min(getEntity().getHealth() + 4.0, getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
				addHappiness(0.3);
				event.getPlayer().sendMessage("[" + getName() + "] Thank you the bread was delicous!");
				getEntity().getEyeLocation().getWorld().spawnParticle(Particle.HEART, getEntity().getEyeLocation(), 5);
			}
			event.getPlayer().sendMessage("[" + getName() + "] my happiness value is currently " + getHappiness() + "!");
		}
		
		Inventory inv = Bukkit.createInventory(event.getPlayer(), 9);
		event.getPlayer().openInventory(inv);
		getHandler().runTaskLater(() -> event.getPlayer().closeInventory(), 0L);
	}
	
//	@Override
//	protected void onPotionSplash(PotionSplashEvent event) {
//		super.onPotionSplash(event);
//		if (event.getPotion().getEffects().stream().allMatch((effect) -> effect.getType() == PotionEffectType.HEAL || effect.getType() == PotionEffectType.REGENERATION)) {
//			addHappiness(0.2);
//		}
//	}
	
	@Override
	protected void onRegainHealth(EntityRegainHealthEvent event) {
		super.onRegainHealth(event);
		if (event.getRegainReason() == RegainReason.MAGIC) {
			addHappiness(0.3);
		}
		else if (event.getRegainReason() == RegainReason.MAGIC_REGEN){
			addHappiness(0.1);
		}
	}
	
	@Override
	protected double getSwimSpeed() {
		return 0.2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KingdomPoint getKingdomPoint() {
		return kingdomPoint;
	}

	public void setKingdomPoint(KingdomPoint kingdom) {
		this.kingdomPoint = kingdom;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	protected boolean canMove() {
		return reloadTime <= 0;
	}
	
	@Override
	protected void loadData(EntityMapData data) {
		//Name
		name = data.get(NAME_TAG) + "";
		//Kingdom Point
		if (data.get(KINGDOM_TAG) instanceof KingdomPoint) {
			((KingdomPoint) data.get(KINGDOM_TAG)).addVillager(this);
		}
		//Happiness
		try {
			happiness = Double.parseDouble(data.get(HAPPINESS_TAG) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		//Level
		try {
			level = Integer.parseInt(data.get(LEVEL_TAG) + "");
		} catch (NumberFormatException e) {
//			e.printStackTrace();
		}
		//XP
		try {
			xp = Integer.parseInt(data.get(XP_TAG) + "");
		} catch (NumberFormatException e) {
//			e.printStackTrace();
		}

	}

	@Override
	public void legacySaveData(Element element, Document document) {
		super.legacySaveData(element, document);
		// Name
		Element name = document.createElement(NAME_TAG);
		name.setTextContent(this.name);
		element.appendChild(name);
		// Kingdom Point
		if (kingdomPoint != null) {
			Element kingdom = document.createElement(KINGDOM_TAG);
			kingdomPoint.saveToXML(kingdom, document);
			element.appendChild(kingdom);
		}
		//Happiness
		Element happiness = document.createElement(HAPPINESS_TAG);
		happiness.setTextContent(this.happiness + "");
		element.appendChild(happiness);
	}
	
	@Override
	protected EntityMapData saveData() {
		HashMap<String, Object> map = new HashMap<>();
		//Name
		map.put(NAME_TAG, name);
		//Kingdom Point
		if (kingdomPoint != null) {
			map.put(KINGDOM_TAG, kingdomPoint);
		}
		//Happiness
		map.put(HAPPINESS_TAG, happiness);
		//Level
		map.put(LEVEL_TAG, level);
		//XP
		map.put(XP_TAG, xp);
		return new EntityMapData(map);
	}

	@Override
	public void legacyParseData(Element element) {
		super.legacyParseData(element);
		// Name
		Node name = element.getElementsByTagName(NAME_TAG).item(0);
		if (name != null) {
			this.name = name.getTextContent();
		}
		// Kingdom Point
		Node kingdom = element.getElementsByTagName(KINGDOM_TAG).item(0);
		if (kingdom != null && kingdom.getNodeType() == Node.ELEMENT_NODE) {
			KingdomPoint point = KingdomPoint.fromXML((Element) kingdom);
			point.addVillager(this);
		}
		//Happiness
		Node happiness = element.getElementsByTagName(HAPPINESS_TAG).item(0);
		if (happiness != null) {
			try {
				this.happiness = Double.parseDouble(happiness.getTextContent());
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public double getHappiness() {
		return happiness;
	}

	public void setHappiness(double happiness) {
		this.happiness = happiness;
	}
	
	public void addHappiness (double happiness) {
		this.happiness += happiness;
		this.happiness = Math.max(-5, Math.min(this.happiness, 5));
		this.happiness = Math.round(this.happiness * 100) / 100.0;
	}
	
	public double getKingdomHappiness() {
		return Math.max(-1, Math.min(this.happiness, 1));
	}

	@Override
	public boolean isTarget(Entity entity) {
		CustomEntity<?> custom = getHandler().getCustomEntityForEntity(entity);
		return ((entity instanceof Monster || entity instanceof Phantom || entity instanceof Ghast) && !(entity instanceof Creeper) && !(entity instanceof PigZombie)
				&& (getKingdomPoint() == null || !(custom instanceof KingdomVillager<?>)
						|| !getKingdomPoint().equals(((KingdomVillager<?>) custom).getKingdomPoint())))
				|| ((kingdomPoint == null
						|| KingdomHandler.getInstance().getKingdom(kingdomPoint).getState() == KingdomState.EVIL)
						&& entity instanceof Player);
	}
	
	public abstract VillagerCategory getVillagerCategory ();
	
	/**
	 * 
	 * @return A function that takes the next level and returns the neede experience to reach it
	 */
	public abstract Function<Integer, Integer> getLevelXPFunction ();
	
	public int getLevelXP () {
		return getLevelXPFunction().apply(level + 1);
	}
	
	public boolean canLevelUp () {
		return level < MAX_LEVEL && xp >= getLevelXP();
	}
	
	public boolean levelUp () {
		boolean can = canLevelUp();
		if (can) {
			xp -= getLevelXP();
			level++;
			onLevelUp();
		}
		return can;
	}
	
	public boolean forceLevelUp () {
		xp += getLevelXP();
		boolean leveled = canLevelUp();
		if (!leveled) {
			xp += getLevelXP();
		}
		return leveled;
	}
	
	protected void onLevelUp () {
		
	}

	public int getXp() {
		return xp;
	}
	
	/**
	 * 
	 * @param amount
	 * @return the amount of times the villager leveled up
	 */
	public int gainXP (int amount) {
		xp += amount;
		int count = 0;
		while (levelUp()) {
			count++;
		}
		return count;
	}

	public int getLevel() {
		return level;
	}

	public boolean isLoaded() {
		return reloadTime <= 0;
	}

}
