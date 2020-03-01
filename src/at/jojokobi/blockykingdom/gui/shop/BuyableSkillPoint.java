package at.jojokobi.blockykingdom.gui.shop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.players.CharacterStats;

public class BuyableSkillPoint implements Buyable{

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.EXPERIENCE_BOTTLE);
	}

	@Override
	public int getPrice() {
		return 500;
	}

	@Override
	public void onBuy(Player player, CharacterStats stats) {
		stats.setSkillPoints(stats.getSkillPoints() + 1);
	}

	@Override
	public int getMinLevel() {
		return 15;
	}
	
	@Override
	public Map<String, Object> serialize() {
		return new HashMap<String, Object> ();
	}
	
	public static BuyableSkillPoint deserialize (Map<String, Object> map) {
		return new BuyableSkillPoint();
	}

}
