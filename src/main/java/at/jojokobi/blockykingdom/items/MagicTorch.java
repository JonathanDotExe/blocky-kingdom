package at.jojokobi.blockykingdom.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class MagicTorch extends CustomTool{
	
	private BlockyKingdomPlugin plugin;
	
	public static final String NAME = "Magic Torch";
	public static final String IDENTIFIER = "magic_torch";
	public static final short META = 2;
	public static final Material ITEM = Material.IRON_SHOVEL;

	public MagicTorch(BlockyKingdomPlugin plugin) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.plugin = plugin;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		setMaxDurability(128);
		setDamage(3);
		setSpeed(-1);
		setRepairMaterial(Material.FLINT);
		ItemHandler.addCustomItem(this);
		Bukkit.getScheduler().runTask(plugin, () -> registerRecipe());
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe (new NamespacedKey(plugin, getIdentifier()), createItem());
		recipe.shape(" CF", "ILC", "II ");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('C', Material.COAL);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		return recipe;
	}

	@Override
	public boolean useItem(ItemStack item, Player player) {
		doAreaDamage(player, player.getLocation());
		spawnParticles(player.getLocation());
		return true;
	}

	@Override
	public boolean hit(ItemStack item, Entity damager, Entity defender) {
		doAreaDamage(damager, defender.getLocation());
		spawnParticles(defender.getLocation());
		return true;
	}
	
	private void spawnParticles (Location loc) {
		loc.getWorld().spawnParticle(Particle.FLAME, loc, 20);
	}
	
	private void doAreaDamage (Entity attacker, Location defLoc) {
		int level = 0;
		Statable statable = StatHandler.getInstance().getStats(attacker);
		if (statable != null) {
			CharacterStats stats = statable.getCharacterStats();
			level = stats.getMagic();
		}
		
		Blaze blaze = defLoc.getWorld().spawn(defLoc, Blaze.class);
		for (Entity entity : blaze.getNearbyEntities(2 + level * 0.5, 2 + level * 0.5, 2 + level * 0.5)) {
			if (entity != attacker && (attacker instanceof Player || entity instanceof Player)) {
				entity.setFireTicks(80 + level * 4);
			}
		}
		blaze.remove();
	}

}
