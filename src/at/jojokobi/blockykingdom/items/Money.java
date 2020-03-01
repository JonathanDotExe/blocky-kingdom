package at.jojokobi.blockykingdom.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.item.CustomItem;
import at.jojokobi.mcutil.item.ItemHandler;

public class Money extends CustomItem{
	
	public static final String NAME = "Money";
	public static final String IDENTIFIER = "money";
	public static final short META = 0;
	public static final Material ITEM = Material.GOLD_INGOT;

	public Money() {
		super(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, IDENTIFIER);
		setName(NAME);
		setMeta(META);
		setMaterial(ITEM);
		ItemHandler.addCustomItem(this);
	}
	
	@Override
	public ItemStack createItem() {
		ItemStack item = super.createItem();
		item.addUnsafeEnchantment(Enchantment.LURE, 1);
		return item;
	}

	@Override
	public void onUse(ItemStack item, Player player) {
		Statable statable = StatHandler.getInstance().getStats(player);
		if (statable != null) {
			statable.getCharacterStats().setMoney(statable.getCharacterStats().getMoney() + 100);
			item.setAmount(item.getAmount() - 1);
		}
	}

	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

}
