package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.w3c.dom.Element;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.dimensions.HeavenDimensionHandlerHandler;
import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.ai.RidingTask;

public class FlyingSheep extends CustomEntity<Sheep>{

	public FlyingSheep(Location place, EntityHandler handler) {
		super(place, handler, FlyingSheepType.getInstance());
		setDespawnTicks(-1);
		addEntityTask(new RidingTask());
	}
	
	@Override
	public void loop() {
		super.loop();
		if (getTime() % 12 == 0) {
			getEntity().setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
		}
		
		//Go to normal world
		if (getEntity().getLocation().getY() < 2 && HeavenDimension.getInstance().isDimension(getEntity().getWorld())) {
			getEntity().eject();
			Location place = new Location (HeavenDimension.getInstance().getNormalWorld(getEntity().getWorld()), getEntity().getLocation().getX(), 255, getEntity().getLocation().getZ());
			getEntity().teleport(place);
		}
	}
	
	@Override
	protected void onGetDismounted(EntityDismountEvent event) {
		super.onGetDismounted(event);
		if (event.getEntity() instanceof Player && getEntity().getLocation().getY() > 200) {
			HeavenDimensionHandlerHandler.getInstance().doSuperJump((Player) event.getEntity(), 10);
		}
	}
	
	@Override
	protected void onDamage(EntityDamageEvent event) {
		super.onDamage(event);
		if (event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		super.onInteract(event);
		getEntity().addPassenger(event.getPlayer());
	}

	@Override
	protected Sheep createEntity(Location place) {
		Sheep sheep = place.getWorld().spawn(place, Sheep.class);
		sheep.setGliding(true);
		sheep.getEquipment().setChestplate(new ItemStack(Material.ELYTRA));
		sheep.getEquipment().setChestplateDropChance(0.0f);
		return sheep;
	}
	
	@Override
	protected boolean canFly() {
		return true;
	}
	
	@Override
	protected double getFlySpeed() {
		return 0.5;
	}
	
	@Override
	public void legacyParseData(Element element) {
		super.legacyParseData(element);
	}
	
	public static FlyingSheep deserialize (Map<String, Object> map) {
		FlyingSheep entity = new FlyingSheep(null, null);
		entity.load(map);
		return entity;
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<>());
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

}
