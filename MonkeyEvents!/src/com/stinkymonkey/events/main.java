package com.stinkymonkey.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.stinkymonkey.events.Games.MiningEvent.MineEvent;
import com.stinkymonkey.events.KothEvent.KothEvent;
import com.stinkymonkey.events.commands.endCmd;
import com.stinkymonkey.events.commands.guiCmd;
import com.stinkymonkey.events.commands.joinCmd;
import com.stinkymonkey.events.commands.nextCmd;
import com.stinkymonkey.events.commands.reloadCmd;
import com.stinkymonkey.events.dataManagement.fileManager;

public class main extends JavaPlugin {
	private static WorldGuard WG;
	private static configManager cM;
	private static commandManager coM;
	private static fileManager fM;
	private static Title T;
	
	// COMMANDS
	private static guiCmd gC;
	private static joinCmd jC;
	private static nextCmd nC;
	private static reloadCmd rC;
	private static endCmd eC;
	
	// Events
	private static MineEvent MEe;
	private static KothEvent KOe;
	
	public static String[] eventRunning = new String[3];
	public static List<UUID> eventBanned = new ArrayList<UUID>();
	public static List<String> eventsFile = new ArrayList<String>();
	public static HashMap<String, List<String>> eventsPresets = new HashMap<String, List<String>>();
	public static List<UUID> playerInEvent = new ArrayList<UUID>();
	@Override
	public void onEnable() {
		
		// ADD EVENTS
		main.eventsFile.add("MINING EVENT");
		main.eventsFile.add("PILLOW FIGHT");
		main.eventsFile.add("KOTH EVENT");
		
		// EVENT REGISTRY
		MEe = new MineEvent(this);
		KOe = new KothEvent(this);
		
		// REGISTERY
		PluginManager PM = getServer().getPluginManager();
		PM.registerEvents(new mainListener(this), this);
		PM.registerEvents(KOe, this);
		commandManager coM = new commandManager(this);
		getCommand("event").setExecutor(coM);
		getCommand("event").setTabCompleter(new tabManager());
		cM = new configManager(this);
		fM = new fileManager(this);
		
		
		// COMMAND REGISTERY
		gC = new guiCmd(this);
		jC = new joinCmd(this);
		nC = new nextCmd(this);
		rC = new reloadCmd(this);
		eC = new endCmd(this);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		for (UUID p : playerInEvent) {
			if(ScoreHelper.hasScore(Bukkit.getPlayer(p))) {
				ScoreHelper.removeScore(Bukkit.getPlayer(p));
			}
        }
	}
	
	//Getters
	public configManager getConfigManager() {
		return cM;
	}
	public fileManager getFileManger() {
		return fM;
	}
	public WorldGuard getWorldGuard() {
		return WG;
	}
	public Title getTitle() {
		return T;
	}
	public reloadCmd getReloadCmd() {
		return rC;
	}
	
	//COMMANDS
	public guiCmd getGuiCmd() {
		return gC;
	}
	public joinCmd getJoinCmd() {
		return jC;
	}
	public nextCmd getNextCmd() {
		return nC;
	}
	public endCmd getEndCmd() {
		return eC;
	}
	
	// EVENTS
	public MineEvent getMineEvent() {
		return MEe;
	}
	public KothEvent getKothEvent() {
		return KOe;
	}
}
