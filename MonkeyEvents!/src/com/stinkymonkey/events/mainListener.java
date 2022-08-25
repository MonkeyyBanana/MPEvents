package com.stinkymonkey.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class mainListener implements Listener{
	private main Main;
	public mainListener(main Main) {
		this.Main = Main;
	}
	
	@EventHandler
	public void inventoryEvents(InventoryClickEvent event) {
		if (!event.getInventory().equals(Main.getGuiCmd().eventsGui)) return;
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		if (main.eventsFile.contains(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))) {
			Main.getGuiCmd().creaatePresetGui(event.getCurrentItem().getItemMeta().getDisplayName(), p);
			p.closeInventory();
			p.openInventory(Main.getGuiCmd().presetGui);
		}
		if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals("CLOSE")) {
			p.closeInventory();
		}
	}
	
	List<UUID> playerExecute = new ArrayList<UUID>();
	List<String> playerEventName = new ArrayList<String>();
	List<String> playerPresetName = new ArrayList<String>();
	@EventHandler
	public void inventoryPresets(InventoryClickEvent event) {
		if (!event.getInventory().equals(Main.getGuiCmd().presetGui)) return;
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		if (main.eventsPresets.get(ChatColor.stripColor(p.getOpenInventory().getTitle())).contains(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))) {
			if (main.playerInEvent.isEmpty()) {
				p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Run Event? Type 'run' To Start Event Or Type 'cancel' To Cancel The Event!!");
				playerExecute.add(p.getUniqueId());
				playerEventName.add(ChatColor.stripColor(p.getOpenInventory().getTitle()));
				playerPresetName.add(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
				p.closeInventory();
			} else {
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Event Is Already Runing! End Event With /event end");
				p.closeInventory();
			}
		}
		if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals("GO BACK")) {
			p.openInventory(Main.getGuiCmd().eventsGui);
		}
	}
	
	@EventHandler
	public void chatListener(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (playerExecute.contains(p.getUniqueId())) {
			if (event.getMessage().equalsIgnoreCase("run")) {
				event.setCancelled(true);
				int index = playerExecute.indexOf(p.getUniqueId());
				// REGISTER IN HERE 
				switch (ChatColor.stripColor(playerEventName.get(index))) {
					case "MINING EVENT":
						Main.getMineEvent().runEvent(p, playerPresetName.get(index));
					case "KOTH EVENT":
						Main.getKothEvent().runEvent(p, playerPresetName.get(index));
				}
				playerEventName.remove(index);
				playerExecute.remove(index);
				playerPresetName.remove(index);
			} else if (event.getMessage().equalsIgnoreCase("cancel")) {
				event.setCancelled(true);
				int index = playerExecute.indexOf(p.getUniqueId());
				playerEventName.remove(index);
				playerExecute.remove(index);
				playerPresetName.remove(index);
				p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Event Has Been Cancelled!");
			}
		}
	}
}
