package at.jojokobi.blockykingdom.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class SlimerersHeart extends CustomItem{
	
	public static final String NAME = ChatColor.DARK_RED + "Slimerers Heart";
	public static final String IDENTIFIER = "slimerers_heart";
	public static final short META = 1;
	public static final Material ITEM = Material.COOKED_BEEF;

	public SlimerersHeart() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(0);
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		if (player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			item.setAmount(item.getAmount() - 1);
			player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.AMBIENT, 1, 1);
		}
		return true;
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
