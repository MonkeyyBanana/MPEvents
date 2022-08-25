package com.stinkymonkey.events.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stinkymonkey.events.main;

import net.md_5.bungee.api.ChatColor;

public class guiCmd {
	private main Main;
	public guiCmd(main Main) {
		this.Main = Main;
	}
	public Inventory eventsGui;
	public Inventory presetGui;
	
	public void runCmd(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("MonkeyEvent.admin")) {
				createEventGui(p);
				p.openInventory(eventsGui);
			}
		}
	}
	
	public void createEventGui(Player p) {
		eventsGui = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "MONKEY EVENTS");
		
		//Creating Event Template
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		eventsGui.setItem(36, item);
		eventsGui.setItem(37, item);
		eventsGui.setItem(38, item);
		eventsGui.setItem(39, item);
		eventsGui.setItem(40, item);
		eventsGui.setItem(41, item);
		eventsGui.setItem(42, item);
		eventsGui.setItem(43, item);
		eventsGui.setItem(44, item);
		item.setType(Material.BOOK);
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Need Help?");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click On An Event To Chose Preset");
		meta.setLore(lore);
		item.setItemMeta(meta);
		eventsGui.setItem(45, item);
		item.setType(Material.EMERALD_BLOCK);
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Events Manager");
		List<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Designed And Created By KingMonkeyy");
		meta.setLore(lore1);
		item.setItemMeta(meta);
		eventsGui.setItem(49, item);
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLOSE");
		List<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "CLICK TO CLOSE");
		meta.setLore(lore2);
		item.setItemMeta(meta);
		eventsGui.setItem(53, item);
		
		//EVENTS FOR GUI
		List<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GRAY + "MINE MINE MINE!");
		meta.setLore(lore3);
		item.setType(Material.DIAMOND_PICKAXE);
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "MINING EVENT");
		item.setItemMeta(meta);
		eventsGui.setItem(0, item);
		
		List<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GRAY + "Special Thanks To Kai and DroomKet");
		meta.setLore(lore4);
		item.setType(Material.PINK_WOOL);
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PILLOW FIGHT");
		item.setItemMeta(meta);
		eventsGui.setItem(1, item);
		
		List<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GRAY + "King of The Hill");
		meta.setLore(lore5);
		item.setType(Material.IRON_AXE);
		meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KOTH EVENT");
		item.setItemMeta(meta);
		eventsGui.setItem(2, item);
	}
	
	public void creaatePresetGui(String name, Player p) {
		presetGui = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + name);
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		for (String str : main.eventsPresets.get(ChatColor.stripColor(name))) {
			meta.setDisplayName(ChatColor.BOLD + str);
			item.setItemMeta(meta);
			presetGui.addItem(item);
		}
		item.setType(Material.BLACK_STAINED_GLASS_PANE);
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		presetGui.setItem(36, item);
		presetGui.setItem(37, item);
		presetGui.setItem(38, item);
		presetGui.setItem(39, item);
		presetGui.setItem(40, item);
		presetGui.setItem(41, item);
		presetGui.setItem(42, item);
		presetGui.setItem(43, item);
		presetGui.setItem(44, item);
		item.setType(Material.BOOK);
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Need Help?");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click On An Event To Chose Preset");
		meta.setLore(lore);
		item.setItemMeta(meta);
		presetGui.setItem(45, item);
		item.setType(Material.EMERALD_BLOCK);
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + name);
		List<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Preset Selection");
		meta.setLore(lore1);
		item.setItemMeta(meta);
		presetGui.setItem(49, item);
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "GO BACK");
		List<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "CLICK TO GO BACK");
		meta.setLore(lore2);
		item.setItemMeta(meta);
		presetGui.setItem(53, item);
	}
}
