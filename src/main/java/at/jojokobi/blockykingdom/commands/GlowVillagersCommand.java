package at.jojokobi.blockykingdom.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.entities.kingdomvillagers.KingdomVillager;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.mcutil.entity.EntityHandler;

public class GlowVillagersCommand implements CommandExecutor {
	
	public static final String COMMAND_NAME = "glowvillagers";
	
	private EntityHandler entityHandler;

	public GlowVillagersCommand(EntityHandler entityHandler) {
		super();
		this.entityHandler = entityHandler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender instanceof Entity) {
				KingdomPoint kingdom = new KingdomPoint(((Entity) sender).getLocation());
				List<KingdomVillager<?>> villagers = kingdom.getVillagers(entityHandler);
				for (KingdomVillager<?> villager : villagers) {
					if (villager.getEntity() instanceof LivingEntity) {
						villager.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 60 * 15, 1));
					}
				}
				sender.sendMessage("Giving all villagers in this kingdom a glow effect for 15 minutes ...");
				return true;
			}
		}
		return false;
	}

}
