package at.jojokobi.blockykingdom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.jojokobi.blockykingdom.gui.SelectSpeciesGUI;
import at.jojokobi.blockykingdom.players.StatHandler;
import at.jojokobi.blockykingdom.players.Statable;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;

public class ResetStatsCommand implements CommandExecutor {
	
	public static final String COMMAND_NAME = "resetstats";
	
	private InventoryGUIHandler guiHandler;

	public ResetStatsCommand(InventoryGUIHandler guiHandler) {
		super();
		this.guiHandler = guiHandler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Statable stats = StatHandler.getInstance().getStats(sender);
				stats.getCharacterStats().reset();
				
				SelectSpeciesGUI gui = new SelectSpeciesGUI(player, stats);
				guiHandler.addGUI(gui);
				gui.show();
				return true;
			}
		}
		return false;
	}

}
