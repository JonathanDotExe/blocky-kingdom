package at.jojokobi.blockykingdom.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.jojokobi.blockykingdom.items.EconomicFigure;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class FigureShopGUI extends InventoryGUI {

	private static final int SELL_SLOT = 0;
	private static final int PRICE_SLOT = 1;
	private static final int MONEY_SLOT = 2;

	private EconomicFigure figure;
	private ArmorStand figureEntity;
	private Chest chest;
	private ChatInputHandler inputHandler;
	private CharacterStats stats;

	public FigureShopGUI(Player owner, CharacterStats stats, EconomicFigure figure, ArmorStand figureEntity, Chest chest,
			ChatInputHandler inputHandler) {
		super(owner, Bukkit.createInventory(owner, INV_ROW));
		this.stats = stats;
		this.figure = figure;
		this.figureEntity = figureEntity;
		this.chest = chest;
		this.inputHandler = inputHandler;
		initGUI();
	}

	@Override
	protected void initGUI() {
		// Sell
		if (chest.getBlockInventory().getItem(0) != null && chest.getBlockInventory().getItem(0).getAmount() > 0) {
			ItemStack item = new ItemStack(chest.getBlockInventory().getItem(0));
			item.setAmount(1);
			addButton(item, SELL_SLOT, (button, index, click) -> {
				int price = figure.getPrice(figureEntity.getEquipment().getHelmet());
				int money = figure.getMoney(figureEntity.getEquipment().getHelmet());
				if (chest.getBlockInventory().getItem(0) != null && chest.getBlockInventory().getItem(0).getAmount() > 0 && price >= 0&& stats.getMoney() >= price) {
					ItemStack buy = chest.getBlockInventory().getItem(0).clone();
					chest.getBlockInventory().getItem(0).setAmount(chest.getBlockInventory().getItem(0).getAmount() - 1);
					buy.setAmount(1);
					
					getOwner().getInventory().addItem(buy);
					stats.setMoney(stats.getMoney() - price);
					figureEntity.getEquipment().setHelmet(figure.setMoney(figureEntity.getEquipment().getHelmet(), money + price));
					getOwner().sendMessage(ChatColor.GREEN + "[Economic Figure] KAT-SCHING! You bought " + buy.getItemMeta().getDisplayName() + "!");
					initGUI();
				}
			});
		}
		// Price
		{
			ItemStack item = new ItemStack(Material.IRON_INGOT);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Price: " + figure.getPrice(figureEntity.getEquipment().getHelmet()));
			item.setItemMeta(meta);
			addButton(item, PRICE_SLOT, (button, index, click) -> {
				if (getOwner().getUniqueId().equals(figure.getOwner(figureEntity.getEquipment().getHelmet()))) {
					getOwner().sendMessage("[Economic Figure] Please enter the new price to the chat!");
					inputHandler.requestPlayerInput(getOwner(), new ChatInputHandler.InputProcessor() {
						@Override
						public void process(Player player, String input) {
							try {
								figureEntity.getEquipment().setHelmet(figure.setPrice(figureEntity.getEquipment().getHelmet(), Integer.parseInt(input)));
								player.sendMessage(ChatColor.GREEN + "[Economic Figure] You set the price to " + input + "!");
							}
							catch (NumberFormatException e) {
								player.sendMessage(ChatColor.RED + "[Economic Figure] You have to enter a valid number!");
							}
						}
					});
					close ();
				}
			});
		}
		// Money
		{
			ItemStack item = new ItemStack(Material.GOLD_INGOT);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Money: " + figure.getMoney(figureEntity.getEquipment().getHelmet()));
			item.setItemMeta(meta);
			addButton(item, MONEY_SLOT, (button, index, click) -> {
				int money = figure.getMoney(figureEntity.getEquipment().getHelmet());
				if (getOwner().getUniqueId().equals(figure.getOwner(figureEntity.getEquipment().getHelmet()))) {
					stats.setMoney(stats.getMoney() + money);
					getOwner().sendMessage(ChatColor.GREEN + "[Economic Figure] KAT-SCHING! You recieved " + money + " $!");
					getOwner().sendMessage(ChatColor.GREEN + "[Economic Figure] I recommend you to invest it into some new economic stuff for or shop!");
					figureEntity.getEquipment().setHelmet(figure.setMoney(figureEntity.getEquipment().getHelmet(), 0));
					initGUI();
				}
			});
		}
		fillEmpty(getFiller());
	}

}
