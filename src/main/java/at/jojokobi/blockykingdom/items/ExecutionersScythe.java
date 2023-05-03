package at.jojokobi.blockykingdom.items;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.SkullMeta;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class ExecutionersScythe extends CustomTool{
	
	public static final String NAME = "Excutioners Scythe";
	public static final String IDENTIFIER = "executioners_scythe";
	public static final short META = 2;
	public static final Material ITEM = Material.IRON_AXE;

	public ExecutionersScythe() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setDamage(4);
		setSpeed(-2.0);
		setMaxDurability(312);
		setRepairMaterial(Material.IRON_INGOT);
		ItemHandler.addCustomItem(this);
	}
	
	@Override
	public Recipe getRecipe() {
		return null;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		return false;
	}
	
	@Override
	public boolean hit(ItemStack item, EntityDamageByEntityEvent event) {
		double damage = event.getDamage();
		for (Entity entity : event.getDamager().getNearbyEntities(4, 4, 4)) {
			if (!entity.equals(event.getEntity()) && !entity.equals(event.getDamager())) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).damage(damage);
				}
			}
		}
		
		if (event.getEntity() instanceof Damageable) {
			if (Math.round(((Damageable) event.getEntity()).getHealth() - event.getFinalDamage()) <= 0.5 && Math.random() < 0.2) {
				ItemStack skull = null;
				if (event.getEntity() instanceof Zombie) {
					skull = new ItemStack(Material.ZOMBIE_HEAD);
				}
				else if (event.getEntity() instanceof Skeleton) {
					skull = new ItemStack(Material.SKELETON_SKULL);
				}
				else if (event.getEntity() instanceof Creeper) {
					skull = new ItemStack(Material.CREEPER_HEAD);
				}
				else if (event.getEntity() instanceof Player) {
					skull = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					meta.setOwningPlayer((Player) event.getEntity());
					skull.setItemMeta(meta);
				}
				if (skull != null) {
					event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), skull);
				}
			}
		}
		return super.hit(item, event);
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		return true;
	}

}
