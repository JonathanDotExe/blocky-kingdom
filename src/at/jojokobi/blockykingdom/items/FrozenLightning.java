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

public class FrozenLightning extends CustomItem{
	
	public static final String NAME = ChatColor.YELLOW + "Frozen Lightning";
	public static final String IDENTIFIER = "frozen_lightning";
	public static final short META = 4;
	public static final Material ITEM = Material.GHAST_TEAR;

	public FrozenLightning() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
