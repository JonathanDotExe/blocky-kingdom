package at.jojokobi.blockykingdom.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.ChatInputHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.gui.InventoryGUI;

public class PlayerGUI extends InventoryGUI{
	
	private Statable statable;
	
	private static final Material ATTACK_MATERIAL = Material.IRON_SWORD;
	private static final Material DEFENSE_MATERIAL = Material.IRON_CHESTPLATE;
	private static final Material SPEED_MATERIAL = Material.IRON_BOOTS;
	private static final Material MAGIC_MATERIAL = Material.POTION;
	private static final Material HEALTH_MATERIAL = Material.COOKED_BEEF;
	
	private static final Material QUESTS_MATERIAL = Material.WRITABLE_BOOK;
	private static final Material KINGDOM_MATERIAL = Material.MAP;
	private static final Material SKILLS_MATERIAL = Material.EMERALD;
	
	private KingdomPoint kingdom;
	private ChatInputHandler inputHandler;
	private EntityHandler handler;

	public PlayerGUI(Player owner, Statable statable, EntityHandler handler, KingdomPoint kingdom, ChatInputHandler inputHandler) {
		super(owner, Bukkit.createInventory(owner, INV_ROW * 3, owner.getDisplayName() + "'s data"));
		this.statable = statable;
		this.kingdom = kingdom;
		this.handler = handler;
		this.inputHandler = inputHandler;
		initGUI();
	}

	@Override
	protected void initGUI() {
		getInventory().clear ();
		//Player Icon
		{
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwningPlayer(getOwner());
			meta.setDisplayName(getOwner().getDisplayName());
			item.setItemMeta(meta);
			addButton(item, 0, null);
		}
		//Money
		{
			ItemStack item = new ItemStack(Material.GOLD_INGOT);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getMoney() + " $");
			List<String> lore = new ArrayList<>();
			lore.add(" * Your money");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, 1, null);
		}
		//Kingdom
		{
			ItemStack item = new ItemStack(KINGDOM_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(kingdom.toKingdom().getName());
			List<String> lore = new ArrayList<>();
			lore.add(" * " + kingdom.toKingdom().getState().getDescrition());
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, 2, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				setNext(new KingdomGUI(getOwner(), stats, kingdom, handler, inputHandler));
				close ();
			});
		}
		
		//Quests
		{
			ItemStack item = new ItemStack(QUESTS_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Quests");
			List<String> lore = new ArrayList<>();
			lore.add(" * Your quests");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW - 5, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				setNext(new QuestGUI(getOwner(), stats));
				close();
			});
		}
		
		//Skills
		{
			ItemStack item = new ItemStack(SKILLS_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Skills");
			List<String> lore = new ArrayList<>();
			lore.add(" * Your skills");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW - 4, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				setNext(new SkillGUI(getOwner(), stats));
				close();
			});
		}
		
		//Skill points
		{
			ItemStack item = new ItemStack(Material.GOLD_NUGGET);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("" + statable.getCharacterStats().getSkillPoints());
			List<String> lore = new ArrayList<>();
			lore.add(" * Your skill points");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW - 3, null);
		}
		
		//Experience
		{
			ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getXp() + "/" + statable.getCharacterStats().getXpToNextLevel());
			List<String> lore = new ArrayList<>();
			lore.add(" * Your experience");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW - 2, null);
		}
		
		//Level
		{
			ItemStack item = new ItemStack(Material.DIAMOND);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("" + statable.getCharacterStats().getLevel());
			List<String> lore = new ArrayList<>();
			lore.add(" * Your level");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW - 1, null);
		}
		
		//Attack
		{
			ItemStack item = new ItemStack(ATTACK_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getTotalAttack() + "/" + (statable.getCharacterStats().getSpecies().getAttackOffset() + CharacterStats.MAX_SKILL_LEVEL));
			List<String> lore = new ArrayList<>();
			lore.add(" * Your attack level");
			lore.add(" * Needed Skill Points " + statable.getCharacterStats().getNeededSkillPoints(statable.getCharacterStats().getAttack()));
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 2 + 0, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				stats.doAttackPowerUp();
				initGUI();
			});
		}
		
		//Defense
		{
			ItemStack item = new ItemStack(DEFENSE_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getTotalDefense() + "/" + (statable.getCharacterStats().getSpecies().getDefenseOffset() + CharacterStats.MAX_SKILL_LEVEL));
			List<String> lore = new ArrayList<>();
			lore.add(" * Your defense level");
			lore.add(" * Needed Skill Points " + statable.getCharacterStats().getNeededSkillPoints(statable.getCharacterStats().getDefense()));
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 2 + 1, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				stats.doDefensePowerUp();
				initGUI();
			});
		}
		//Speed
		{
			ItemStack item = new ItemStack(SPEED_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getTotalSpeed() + "/" + (statable.getCharacterStats().getSpecies().getSpeedOffset() + CharacterStats.MAX_SKILL_LEVEL));
			List<String> lore = new ArrayList<>();
			lore.add(" * Your speed level");
			lore.add(" * Needed Skill Points " + statable.getCharacterStats().getNeededSkillPoints(statable.getCharacterStats().getSpeed()));
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 2 + 2, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				stats.doSpeedPowerUp();
				initGUI();
			});
		}
		//Magic
		{
			ItemStack item = new ItemStack(MAGIC_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getTotalMagic() + "/" + (statable.getCharacterStats().getSpecies().getMagicOffset() + CharacterStats.MAX_SKILL_LEVEL));
			List<String> lore = new ArrayList<>();
			lore.add(" * Your magic level");
			lore.add(" * Needed Skill Points " + statable.getCharacterStats().getNeededSkillPoints(statable.getCharacterStats().getMagic()));
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 2 + 3, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				stats.doMagicPowerUp();
				initGUI();
			});
		}
		//Health
		{
			ItemStack item = new ItemStack(HEALTH_MATERIAL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getTotalHealth() + "/" + (statable.getCharacterStats().getSpecies().getHealthOffset() + CharacterStats.MAX_SKILL_LEVEL));
			List<String> lore = new ArrayList<>();
			lore.add(" * Your health level");
			lore.add(" * Needed Skill Points " + statable.getCharacterStats().getNeededSkillPoints(statable.getCharacterStats().getHealth()));
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 2 + 4, (button, index, click) -> {
				CharacterStats stats = statable.getCharacterStats();
				stats.doHealthPowerUp();
				initGUI();
			});
		}
		//Species
		{
			ItemStack item = new ItemStack(statable.getCharacterStats().getSpecies().getIcon());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getSpecies().getName());
			List<String> lore = new ArrayList<>();
			lore.add(" * Your species");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 3 - 2, null);
		}
		//Profession
		{
			ItemStack item = new ItemStack(statable.getCharacterStats().getProfession().getIcon());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(statable.getCharacterStats().getProfession().getName());
			List<String> lore = new ArrayList<>();
			lore.add(" * Your profession");
			meta.setLore(lore);
			item.setItemMeta(meta);
			addButton(item, INV_ROW * 3 - 1, null);
		}
		
		fillEmpty(getFiller());
	}

}
