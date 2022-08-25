package com.stinkymonkey.events.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stinkymonkey.events.main;

import net.md_5.bungee.api.ChatColor;

public class reloadCmd {
	private main Main;
	public reloadCmd(main Main) {
		this.Main = Main;
	}
	
	public void runCmd(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("MonkeyEvent.admin")) {
				p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "RELOADING MONKEY EVENTS!");
				Main.reloadConfig();
				Main.getFileManger().loadPresets();
			}
		} else {
			Main.reloadConfig();
			System.out.println("[MonkeyEvent-DEBUG] RELOADING CONFIG");
		}
	}
}
