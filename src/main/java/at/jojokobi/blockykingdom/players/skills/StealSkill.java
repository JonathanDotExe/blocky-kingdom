package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterSpecies;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;

public class StealSkill extends Skill {
	
	private static final String IDENTIFIER = "steal";
	
	public StealSkill() {
		super(10, 20, "Steal");
	}
	
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
		Statable stats = StatHandler.getInstance().getStats(event.getDamager());
		if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof InventoryHolder && stats != null && stats.getCharacterStats().getSkillLevel(this) > 0) {
			double chance = stats.getCharacterStats().getSkillLevel(this)/20.0;
			EntityEquipment equipment = ((LivingEntity) event.getEntity()).getEquipment();
			if (Math.random() < chance && equipment.getItemInMainHand() != null) {
				((InventoryHolder) event.getDamager()).getInventory().addItem(equipment.getItemInMainHand().clone());
				equipment.setItemInMainHand(null);
			}
		}
	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getSpecies() == CharacterSpecies.GOBLIN;
	}
	
	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.IRON_SWORD;
	}
	
	@Override
	public String getDescription() {
		return "You can steal others items by hitting them (doesn't work on players)!";
	}
	
	@Override
	public String getRequirementsDescription() {
		return "You need to be a goblin to learn this skill!";
	}

}
