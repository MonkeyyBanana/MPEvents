package com.stinkymonkey.events.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stinkymonkey.events.main;

import net.md_5.bungee.api.ChatColor;

public class joinCmd {
	private main Main;
	public joinCmd(main Main) {
		this.Main = Main;
	}
	
	public void runCmd(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("MonkeyEvent.event")) {
				if (!main.eventBanned.contains(p.getUniqueId())) {
					try {
						if (!main.eventRunning[0].equals("")) {
							//REGISTER HERE
							switch(main.eventRunning[0]) {
								case "MINING EVENT":
									Main.getMineEvent().playerJoin(p);
								case "KOTH EVENT":
									Main.getKothEvent().playerJoin(p);
							}
						} else {
							p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "There Are No Events Running!");
						}
					} catch (Exception e) {
						p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "There Are No Events Running!");
					}
				}
			}
		}
	}
}
