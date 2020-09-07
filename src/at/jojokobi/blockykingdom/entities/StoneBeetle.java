package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.PseudoHealthAccessor;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.entity.ai.RidingTask;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class StoneBeetle extends CustomEntity<ArmorStand>{
	
	public static final NamespacedEntry STONE_BEETLE_KEY = new NamespacedEntry(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "stone_beetle");

	private static final LootInventory LOOT = new LootInventory();
	
	static {
		LOOT.addItem(new LootItem(1, new ItemStack(Material.COBBLESTONE), 1, 3));
		LOOT.addItem(new LootItem(0.5, new ItemStack(Material.COBBLESTONE), 1, 2));
		LOOT.addItem(new LootItem(0.2, new ItemStack(Material.REDSTONE), 1, 1));
	}
	
	public StoneBeetle(Location place, EntityHandler handler) {
		super(place, handler, null);
		addEntityTask(new RidingTask());
		addEntityTask(new RandomTask(7));
		addComponent(new HealthComponent(new PseudoHealthAccessor(30)));
		addComponent(new LootComponent(LOOT, 5));
	}
	
	@Override
	public void loop() {
		super.loop();
		Vector velocity = getEntity().getVelocity();
		Locatable goal = getGoal();
		if (velocity.lengthSquared() > 1.4 * 1.4 && goal != null) {
			Vector goalDirection = goal.getLocation().toVector().subtract(getEntity().getLocation().toVector());
			if (getEntity().isOnGround() && Math.signum(goalDirection.getX()) == Math.signum(velocity.getX()) && Math.signum(goalDirection.getZ()) == Math.signum(velocity.getZ())) {
				//Block smash
				Location center = getEntity().getLocation().add(velocity);
				if (center.getBlock().getType().isSolid()) {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								Block relative = center.getBlock().getRelative(x, y, z);
								if (relative.getType().getBlastResistance() <= 6) {
									relative.breakNaturally();
								}
							}
						}
					}
				}
				velocity.multiply(-1);
				velocity.setY(Math.abs(velocity.getY()));
				getEntity().setVelocity(velocity);
			}
		}
	}
	
	@Override
	protected void move(Vector velocity) {
		if (getEntity().isOnGround() || getEntity().getLocation().getBlock().getType() == Material.WATER) {
			super.move(velocity);
		}
	}
	
	@Override
	protected void onInteract(PlayerInteractEntityEvent event) {
		super.onInteract(event);
		getEntity().addPassenger(event.getPlayer());
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	protected ArmorStand createEntity(Location place) {
		ArmorStand entity = place.getWorld().spawn(place, ArmorStand.class);
		entity.setVisible(false);
		entity.setCanPickupItems(false);
		ItemStack item = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(7);
		item.setItemMeta(meta);
		entity.getEquipment().setHelmet(item);
		
		return entity;
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<String, Object>());
	}

	@Override
	protected double getSprintSpeed() {
		return 1.5;
	}
	
	@Override
	protected double getWalkSpeed() {
		return 0.7;
	}
	
	@Override
	protected double getClimbSpeed() {
		return 1;
	}
	
	@Override
	protected boolean canClimb() {
		return true;
	}
	
}
