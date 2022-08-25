package com.stinkymonkey.events.dataManagement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.stinkymonkey.events.main;

public class fileManager extends YamlConfiguration{
	private main Main;
	public fileManager(main Main) {
		this.Main = Main;
		loadPresets();
	}
	
	public void loadPresets() {
		File eventsFolder = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events");
		if (!eventsFolder.exists()) 
			eventsFolder.mkdir();
		for (String str : main.eventsFile) {
			File eFolder = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + str);
			if (!eFolder.exists()) 
				eFolder.mkdir();
			File[] presetFiles = eFolder.listFiles();
			if (eFolder != null) {
				List<String> presetNames = new ArrayList<String>();
				for (File s : presetFiles) {
					presetNames.add(s.getName().replace(".yml", ""));
				}
				main.eventsPresets.put(str, presetNames);
			}
		}
	}
	
	public FileConfiguration loadConfig(String fileName, File file) {
		try {
			InputStream in = Main.getResource(fileName);
			
			if (!file.exists()) {
				Main.getConfigManager().writeSystem(fileName + " NOT FOUND PLEASE CHECK THE EVENTS FOLDER");
				return null;
			}
			FileConfiguration fi = YamlConfiguration.loadConfiguration(file);
			if (in != null) {
				InputStreamReader inReader = new InputStreamReader(in);
				fi.setDefaults(YamlConfiguration.loadConfiguration(inReader));
				fi.save(file);
			}
			
			return fi;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
