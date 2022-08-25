package com.stinkymonkey.events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class configManager {
	private main Main;
	public configManager(main Main) {
		this.Main = Main;
		loadConfig();
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName());
		if (pluginFolder.exists() == false) {
	    	pluginFolder.mkdir();
	    	System.out.println("");
	    }
			
		File configFile = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
			Main.saveDefaultConfig();
	    	System.out.println("");
		}
	    	
	    try {
	    	Main.getConfig().load(configFile);
	    	System.out.println("[MonkeyEvent-DEBUG] Successfully Loaded Config!");
	    } catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
	    }
	}
	
	public void writeSystem(String message) {
		System.out.println(Main.getConfig().getString("console.prefix") + message);
	}
	
	public String getString(String key) {
		if (!Main.getConfig().contains(key)) {
			System.out.println("[MonkeyEvent-DEBUG] Could Not Locate " + key + " In The 'config.yml' Inside Of The " + Main.getDescription().getName() + " Folder! (DELETE THE CURRENT ONE TO RESET TO DEFAULT!)");
			return "errorCoultNotLocateInConfigYml: " + key;
		} else {
			return Main.getConfig().getString(key).replaceAll("&", "§");
		}
	}
	
	public List<String> getStringList(String key) {
		if (!Main.getConfig().contains(key)) {
			System.out.println("[MonkeyEvent-DEBUG] Could Not Locate \" + key + \" In The 'config.yml' Inside Of The \" + Main.getDescription().getName() + \" Folder! (DELETE THE CURRENT ONE TO RESET TO DEFAULT!)");
			return null;
		} else {
			return Main.getConfig().getStringList(key);
		}
	}
	
	public Integer getInteger(String key) {
		if (!Main.getConfig().contains(key)) {
			System.out.println("[MonkeyEvent-DEBUG] Could Not Locate \" + key + \" In The 'config.yml' Inside Of The \" + Main.getDescription().getName() + \" Folder! (DELETE THE CURRENT ONE TO RESET TO DEFAULT!)");
			return null;
		} else {
			return Main.getConfig().getInt(key);
		}
	}
	
	public Boolean getBoolean(String key) {
		if (!Main.getConfig().contains(key)) {
			System.out.println("[MonkeyEvent-DEBUG] Could Not Locate \" + key + \" In The 'config.yml' Inside Of The \" + Main.getDescription().getName() + \" Folder! (DELETE THE CURRENT ONE TO RESET TO DEFAULT!)");
			return null;
		} else {
			return Main.getConfig().getBoolean(key);
		}
	}
	
	@SuppressWarnings("null")
	public void sendMessage(String key, Player p, String custom, String custom2, String custom3) {
		if (!Main.getConfig().contains(key))
			return;
		List<String> message = new ArrayList<String>();
		message.add(Main.getConfig().getString(key));
		if (custom != null || !custom.equals("")) {
			message.set(0, message.get(0).replaceAll("%custom%", custom));
		}
		if (custom != null || !custom2.equals("")) {
			message.set(0, message.get(0).replaceAll("%custom2%", custom2));
		}
		if (custom != null || !custom3.equals("")) {
			message.set(0, message.get(0).replaceAll("%custom3%", custom3));
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("chat.prefix") + message.get(0)));
	}
}
