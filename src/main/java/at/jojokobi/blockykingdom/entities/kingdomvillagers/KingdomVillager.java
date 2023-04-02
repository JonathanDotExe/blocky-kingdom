package at.jojokobi.blockykingdom.entities.kingdomvillagers;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.blockykingdom.kingdoms.RandomWordGenerator;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.CustomEntityType;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;

public abstract class KingdomVillager<T extends LivingEntity> extends CustomEntity<T> {
	
	public static final Function<Integer, Integer> LINEAR_LEVEL_FUNCTION = l -> l;
	public static final Function<Integer, Integer> HALF_QUADRATIC_LEVEL_FUNCTION = l -> (l * l) / 2;

	public static final String NAME_TAG = "name";
	public static final String KINGDOM_TAG = "kingdom";
	public static final String HAPPINESS_TAG = "happiness";
	public static final String LEVEL_TAG = "level";
	public static final String XP_TAG = "xp";
	public static final String RELOAD_TIME_TAG = "reloadTime";
	
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
	}

	@Override
	public void loop() {
		super.loop();
		//Reload logic
		if (reloadTime > 0) {
			int time = reloadTime / 4;
			getEntity().setCustomName("Reloading - " + time / 60 + "m " + time % 60 + "s");
			reloadTime--;
		} else {
			getEntity().setCustomName(name + " [Lvl. " + level + " " + xp + "/" + getLevelXP() + "]");
		}
		if (getEntity().getLocation().getY() < 0) {
			kill();
			getEntity().teleport(getSpawnPoint());
		}
	}

	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		//Respawning
		if (event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.SUFFOCATION || reloadTime > 0) {
			event.setCancelled(true);
		} else if (Math.round(getEntity().getHealth() - event.getFinalDamage()) < 1) {
			event.setDamage(0);
			event.setCancelled(true);
			kill();
		}
	}
	
	void kill() {
		getEntity().setHealth(getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		reloadTime = 4 * 60 * 10;
		//Teleport to spawn if the kingdom is set
		if (kingdomPoint != null) {
			if (KingdomHandler.getInstance().getKingdom(kingdomPoint).getState() == KingdomState.EVIL) {
				//Extra respawn time in evil kingdom
				reloadTime += 4 * 60 * 5;
			}
			getEntity().teleport(getSpawnPoint());
		}
		addHappiness(-1);
	}

	@Override
	protected void onDamageOther(EntityDamageByEntityEvent event) {
		super.onDamageOther(event);
		//XP on damage other
		if (event.getEntity() instanceof Damageable && Math.round(((Damageable) event.getEntity()).getHealth() - event.getFinalDamage()) <= 0) {
			addHappiness(0.2);
			gainXP(1);
		}
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		super.onInteract(event);
		//Non sneaking => Toggle follow
		//Sneaking => Feed bread (only players who own the kingdom)
		if (getKingdomPoint() != null) {
			if (event.getPlayer().isSneaking() && KingdomHandler.getInstance().getKingdom(getKingdomPoint()).getOwners().contains(event.getPlayer().getUniqueId())) {
				if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BREAD) {
					event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
					getEntity().setHealth(Math.min(getEntity().getHealth() + 4.0, getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
					addHappiness(0.3);
					event.getPlayer().sendMessage("[" + getName() + "] Thank you the bread was delicous!");
					getEntity().getEyeLocation().getWorld().spawnParticle(Particle.HEART, getEntity().getEyeLocation(), 5);
				}
				event.getPlayer().sendMessage("[" + getName() + "] my happiness value is currently " + getHappiness() + "!");
			}
		}
		//Check if player owns kingdom
		else if (getReloadTime() > 0) {
			//TODO make some movement logic to bring villager to other kingdom
			Statable s = StatHandler.getInstance().getStats(event.getPlayer());
			KingdomPoint point = new KingdomPoint(getEntity().getLocation());
			Kingdom kingdom = KingdomHandler.getInstance().getKingdom(point);
			if (event.getPlayer().isSneaking() && kingdom.getOwners().contains(event.getPlayer().getUniqueId())) {
				if (s != null && s.getCharacterStats().getMoney() >= getPrice()) {
					//Buy it
					s.getCharacterStats().setMoney(s.getCharacterStats().getMoney() - getPrice());
					point.addVillager(this);
					setSave(true);
					setReloadTime(0);
				}
				else {
					event.getPlayer().sendMessage("You don't have enough money!");
				}
			}
			else {
				event.getPlayer().sendMessage("You can buy me for " + getPrice() + "$ if you sneak-click me!");
			}
		}
		
		Inventory inv = Bukkit.createInventory(event.getPlayer(), 9);
		event.getPlayer().openInventory(inv);
		getHandler().runTaskLater(() -> event.getPlayer().closeInventory(), 0L);
	}
	
	@Override
	protected void onRegainHealth(EntityRegainHealthEvent event) {
		super.onRegainHealth(event);
		//Happiness regain
		if (event.getRegainReason() == RegainReason.MAGIC) {
			addHappiness(0.3);
		}
		else if (event.getRegainReason() == RegainReason.MAGIC_REGEN){
			addHappiness(0.1);
		}
	}
	
	@Override
	protected void onPortalTeleport(EntityPortalEvent event) {
		super.onPortalTeleport(event);
		//No teleport to the nether
		event.setCancelled(true);
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
		//Reload time
		try {
			reloadTime = Integer.parseInt(data.get(RELOAD_TIME_TAG) + "");
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
		//Reload Time
		map.put(RELOAD_TIME_TAG, reloadTime);
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
	
	public int getReloadTime() {
		return reloadTime;
	}

	public void setReloadTime(int reloadTime) {
		this.reloadTime = reloadTime;
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
