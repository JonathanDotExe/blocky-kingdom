package at.jojokobi.blockykingdom.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.siege.KingdomSiegeHandler;

public class StartSiegeCommand implements CommandExecutor {
	
	public static final String COMMAND_NAME = "startsiege";
	
	private KingdomSiegeHandler handler;

	public StartSiegeCommand(KingdomSiegeHandler handler) {
		super();
		this.handler = handler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME) && sender instanceof Entity && sender.isOp()) {
			sender.sendMessage("Starting siege ...");
			handler.startSiege(new KingdomPoint(((Entity) sender).getLocation()));
			return true;
		}
		return false;
	}

}
