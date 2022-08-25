package com.stinkymonkey.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class tabManager implements TabCompleter {
	
	List<String> tab = new ArrayList<String>();
	public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		List<String> result = new ArrayList<String>();
		if (p.hasPermission("MonkeyEvent.event")) {
			tab.add("join");
			tab.add("help");
		}
		if (p.hasPermission("MonkeyEvent.Admin")) {
			tab.add("reload"); tab.add("end");
			tab.add("next");
		}
		if (args.length == 1) {
			for (String s : tab) 
				if (s.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(s);
			return result;
		}
		return result;
	}
}
