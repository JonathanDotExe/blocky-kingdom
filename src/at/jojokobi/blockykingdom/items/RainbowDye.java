package at.jojokobi.blockykingdom.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.FlyingSheep;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class RainbowDye extends CustomItem{
	
	public static final String NAME = ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.AQUA + "b" + ChatColor.DARK_BLUE + "o" + ChatColor.DARK_PURPLE + "w" + ChatColor.RESET + " Dye";
	
	public static final String IDENTIFIER = "rainbow_dye";
	public static final short META = 1;
	public static final Material ITEM = Material.GHAST_TEAR;

	private EntityHandler handler;
	
	public RainbowDye(EntityHandler handler) {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		this.handler = handler;
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
		setMaxStackSize(64);
	}
	
	@Override
	public void onUse(ItemStack item, Player player) {
		handler.addSavedEntity(new FlyingSheep(player.getLocation(), handler));
		item.setAmount(item.getAmount() - 1);
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
