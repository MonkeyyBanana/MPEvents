package com.stinkymonkey.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class commandManager implements CommandExecutor{
	private main Main;
	public commandManager(main Main) {
		this.Main = Main;
	}
	
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		Player p;
		if (label.equalsIgnoreCase("event")) {
			if (args.length == 0) {
				p = (Player) sender;
				if (p.hasPermission("MonkeyEvent.admin")) {
					Main.getGuiCmd().runCmd(sender);
				} else {
					Main.getJoinCmd().runCmd(sender);
				}
			} else if (args[0].equalsIgnoreCase("help")) {
				p = (Player) sender;
				sendHelp(p);
			} else if (args[0].equalsIgnoreCase("join")) {
				Main.getJoinCmd().runCmd(sender);
			} else if (args[0].equalsIgnoreCase("next")) {
				Main.getNextCmd().runCmd(sender);
			} else if (args[0].equalsIgnoreCase("reload")) {
				Main.getReloadCmd().runCmd(sender);
			} else if (args[0].equalsIgnoreCase("end")) {
				Main.getEndCmd().runCmd(sender);
			}
		}
		return true;
	}
	
	public void sendHelp(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("MonkeyEvent.admin")) {
				for (String str : Main.getConfigManager().getStringList("helpMessage.Admin")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
				}
			} else {
				for (String str : Main.getConfigManager().getStringList("helpMessage.Player")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
				}
			}
		} else {
			System.out.println("[MonkeyEvent-DEBUG] NOT MUCH YOU CAN DO IN CONSOLE...");
		}
	} 
}
