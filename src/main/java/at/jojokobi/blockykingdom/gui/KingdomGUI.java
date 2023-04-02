package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.VillagerCategory;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class KingdomGUI extends InventoryGUI{

	private static final int CLAIM_INDEX = 0;
	private static final int ADD_OWNER_INDEX = 2;
	
	private KingdomPoint point;
	private CharacterStats stats;
	private EntityHandler handler;
	private ChatInputHandler inputHandler;
	
	public KingdomGUI(Player owner, CharacterStats stats, KingdomPoint kingdom, EntityHandler handler, ChatInputHandler inputHandler) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 3));
		this.stats = stats;
		this.point = kingdom;
		this.handler = handler;
		this.inputHandler = inputHandler;
		initGUI();
	}

	@Override
	protected void initGUI() {
		Kingdom kingdom = this.point.toKingdom ();
		double defeatedPercentage = this.point.getDefeatedPercentage(handler);
		//Kingdom
		{
			ItemStack item = new ItemStack(Material.MAP);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(kingdom.getName());
			List<String> lore = new ArrayList<>();
			lore.add(" * " + kingdom.getState().getDescrition());
			if (kingdom.getOwners().isEmpty()) {
				if (getStatePrice(kingdom.getState(), defeatedPercentage) >= 0) {
					lore.add(" * " + getStatePrice(kingdom.getState(), defeatedPercentage) + "$ - Click to claim");
				}
				else {
					lore.add(" * You have to fight to claim this kingdom.");
				}
			}
			lore.add(" * Level " + kingdom.getLevel());
			for (Map.Entry<VillagerCategory, Integer> entry : point.countVillagersByCatergory(handler).entrySet()) {
				lore.add(" * " + ChatColor.GREEN + entry.getValue() + "/" + entry.getKey().getMaxAmount(kingdom.getLevel()) + ChatColor.DARK_PURPLE + " " + entry.getKey());
			}
			lore.add(" * Happiness: " + (kingdom.getHappiness() >= 0.0 ? ChatColor.GREEN : ChatColor.RED) + kingdom.getHappiness());
			if (kingdom.isOwner(getOwner().getUniqueId())) {
				lore.add(" * Level up cost: " + kingdom.getLevelUpPrice());
				lore.add(" * Your folk has to have a happiness of at least " + kingdom.getLevelUpHappiness() + "!");
			}
			lore.add(" * Defeated percentage: " + defeatedPercentage);
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, CLAIM_INDEX, (button, index, click) -> {
				//Claim
				if (kingdom.getOwners().isEmpty()) {
					if (kingdom.claimable() && getStatePrice(kingdom.getState(), defeatedPercentage) >= 0 && stats.getMoney() >= getStatePrice(kingdom.getState(), defeatedPercentage)) {
						stats.setMoney(stats.getMoney() - getStatePrice(kingdom.getState(), defeatedPercentage));
						kingdom.addOwner(getOwner().getUniqueId());
						getOwner().sendMessage("You claimed " + kingdom.getName() + "!");
						for (KingdomVillager<?> villager : point.getVillagers(handler)) {
							villager.setHappiness(0);
						}
						close ();
					}
				}
				//Upgrade
				else if (kingdom.isOwner(getOwner().getUniqueId())){
					if (stats.getMoney() >= kingdom.getLevelUpPrice() && kingdom.getHappiness() >= kingdom.getLevelUpHappiness()) {
						stats.setMoney(stats.getMoney() - kingdom.getLevelUpPrice());
						kingdom.setLevel(kingdom.getLevel() + 1);
						getOwner().sendMessage(kingdom.getName() + " is now on level " + kingdom.getLevel() + "!");
						close ();
					}
				}
			});
		}
		//Owners
		{
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Owners");
			List<String> lore = new ArrayList<>();
			for (UUID owner : kingdom.getOwners()) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
				if (player != null) {
					lore.add(" * " + player.getName());
				}
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, 1, null);
		}
		//Add Owner
		{
			ItemStack item = new ItemStack(Material.SKELETON_SKULL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Add Owner");
			List<String> lore = new ArrayList<>();
			lore.add(" * Click to add an owner");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, ADD_OWNER_INDEX, (button, index, click) -> {
				if (kingdom.isOwner(getOwner().getUniqueId())) {
					getOwner().sendMessage("Enter the name of the new owner!");
					inputHandler.requestPlayerInput(getOwner(), new ChatInputHandler.InputProcessor() {
						@Override
						public void process(Player player, String input) {
							Player pl = Bukkit.getPlayer(input);
							if  (pl != null && !kingdom.isOwner(pl.getUniqueId())) {
								kingdom.addOwner(pl.getUniqueId());
								player.sendMessage(pl.getName() + " is now an owner of " + kingdom.getName());
							}
							else {
								player.sendMessage("That player does not exist or isn't online!");
							}
						}
					});
				}
			});
		}
		fillEmpty(getFiller());
	}
	
	private int getStatePrice (KingdomState state, double defeatPercentage) {
		switch (state) {
		case EVIL:
			return defeatPercentage >= 0.75 ? 5000 : -1;
		case GOOD:
			return 20000;
		case UNCLAIMED:
			return 2000;
		}
		return -1;
	}

}
