package at.jojokobi.blockykingdom.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class CloudParticle extends CustomItem{
	
	public static final String NAME = ChatColor.AQUA + "Cloud Particle";
	public static final String IDENTIFIER = "cloud_particle";
	public static final short META = 2;
	public static final Material ITEM = Material.GHAST_TEAR;

	public CloudParticle() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
	}
	
	@Override
	public void onUse(ItemStack item, Player player) {
		
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
