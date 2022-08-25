package com.stinkymonkey.events.Games.MiningEvent;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi.Color;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.RegionCommands;
import com.sk89q.worldedit.extension.factory.parser.DefaultBlockParser;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.util.WorldEditRegionConverter;
import com.stinkymonkey.events.main;
import com.stinkymonkey.events.dataManagement.fileManager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;

public class MineEvent {
	private main Main; 
	public MineEvent(main Main) {
		this.Main = Main;
	}
	
	FileConfiguration presetName;
	Location tpLoc;
	File folderDir;
	RegionContainer container;
	RegionManager regions;
	ProtectedRegion region;
	Region weRegion;
	DefaultDomain member;
	int numOfRound = 0;
	
	public void runEvent(Player p, String preName) {
		if (!checkConfig(preName)) {
			p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Event Preset Does Not Exist!");
			return;
		}
		try {
			//Create Example File
			File configFile = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "MINING EVENT" + System.getProperty("file.separator") + "exampleMine.yml");
			FileUtils.copyToFile(Main.getResource("exampleMine.yml"), configFile);
			
			container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld(presetName.getString("settings.tpLocation.world"))));
			region = regions.getRegion(presetName.getString("settings.regionForMine"));
			region.setPriority(1000);
			region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
			weRegion = WorldEditRegionConverter.convertToRegion(region);
			p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "EVENT HAS BEEN SUCCESSFULLY LOADED START EVENT WITH /event next");
			region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "AN ERROR HAS OCCURED EXIT CODE: " + e);
			System.out.println("[MonkeyEVENT-DEBUG] ERROR HAS OCCURED ");
			e.printStackTrace();
			return;
		}
		main.eventRunning[0] = "MINING EVENT";
		main.eventRunning[1] = preName;
		main.eventRunning[2] = "0";
		runRound(p);
	}
	
	public void runRound(Player p) {
		// MODULES
		// DELAY, BROADCAST, TITLE, BROADCASTALL, COMMAND, BREAKABLE, CONSOLE
		// ADD HELP MENU . ADD EXAMPLE . ADD TABABLE
		int roundNum = Integer.parseInt(main.eventRunning[2]);
		int Delay = 0;
		if (roundNum > numOfRound) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "There Are No More Rounds To Execute");
			return;
		}
		for (String s : presetName.getStringList("round" + Integer.toString(roundNum))) {
			if(s.contains("DELAY")) {
				try {
					Double milliDelay = (20.00 / 1000 * Double.parseDouble(s.replace("DELAY ", "")));
					Delay += milliDelay.intValue();
					System.out.println("[MonkeyEvent-DEBUG] DELAYING TASK BY " + Delay + " TICKS // " + s.replace("DELAY ", "") + " Miliseconds");
				} catch (Exception e) {
					System.out.println("[MonkeyEvent-DEBUG] AN ERROR HAS OCCURED WHILE ATTEMPTING TO ACCESS A DELAY COMMAND EXIT CODE: " + e);
					e.printStackTrace();
				}
			} else if(s.contains("BROADCAST ")) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
					public void run() {
						for (UUID PE : main.playerInEvent)
							Bukkit.getPlayer(PE).sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("BROADCAST ", "")));
					}
				}, Delay);
			} else if (s.contains("BROADCASTALL ")) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
					public void run() {
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', s).replace("BROADCASTALL ", ""));
					}
				}, Delay);
			} else if (s.contains("COMMANDS ")) {
				try {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
						public void run() {
							p.performCommand(s.replace("COMMANDS ", ""));
						}
					}, Delay );
				} catch (Exception e) {
					System.out.println("[MonkeyEvent-DEBUG] ERROR EXECUTING COMMAND");
					p.sendMessage(ChatColor.RED + "ERROR EXECUTING COMMAND CHECK CONSOLE");
					e.printStackTrace();
				}
			} else if (s.contains("BREAKABLE ")) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
					public void run() {
						if (s.equals("BREAKABLE YES")) {
							System.out.println("BREAKABLE ON!");
							region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
						} else if (s.equals("BREAKABLE NO")) {
							System.out.println("BREAKABLE OFF!");
							region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
						} else {
							System.out.println("[MonkeyEvent-DBUG] BREAKABLE ARUGMENTS ARE NOT ACCEPTABLE ONLY USE YES/NO");
						}
					}
				}, Delay );
			} else if (s.contains("TITLE ")) {
				try {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
						public void run() {
							for (UUID PE : main.playerInEvent) {
								Bukkit.getPlayer(PE).sendTitle(parseFormattingCodes(s.replace("TITLE ", "")), "");
							}
						}
					}, Delay );
				} catch (Exception e) {
					System.out.println("TITLE BROKE");
				}
			} else if (s.contains("CONSOLE ")) {
				try {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
						public void run() {
							if (s.contains("%eplayer%")) {
								for (UUID PE : main.playerInEvent) {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("CONSOLE ", "").replace("%eplayer%", Bukkit.getPlayer(PE).getName()));
								}
							} else {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("CONSOLE ", ""));
							}
						}
					}, Delay );
				} catch (Exception e) {
					
				}
			}
		
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
			public void run() {
				p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Event Round Has Been Ended With No Errors!");
			}
		}, Delay);
		main.eventRunning[2] = Integer.toString(roundNum + 1);
		if (Integer.parseInt(main.eventRunning[2]) >= numOfRound) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
				public void run() {
					endEvent(p);
				}
			}, Delay);
				
		}
	}
	
	public String parseFormattingCodes(String message) {
		message = message.replaceAll("&0", ChatColor.BLACK + "");
		message = message.replaceAll("&1", ChatColor.DARK_BLUE + "");
		message = message.replaceAll("&2", ChatColor.DARK_GREEN + "");
		message = message.replaceAll("&3", ChatColor.DARK_AQUA + "");
		message = message.replaceAll("&4", ChatColor.DARK_RED + "");
		message = message.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		message = message.replaceAll("&6", ChatColor.GOLD + "");
		message = message.replaceAll("&7", ChatColor.GRAY + "");
		message = message.replaceAll("&8", ChatColor.DARK_GRAY + "");
		message = message.replaceAll("&9", ChatColor.BLUE + "");
		message = message.replaceAll("&a", ChatColor.GREEN + "");
		message = message.replaceAll("&b", ChatColor.AQUA + "");
		message = message.replaceAll("&c", ChatColor.RED + "");
		message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		message = message.replaceAll("&e", ChatColor.YELLOW + "");
		message = message.replaceAll("&f", ChatColor.WHITE + "");
		message = message.replaceAll("&l", ChatColor.BOLD + "");
		message = message.replaceAll("&o", ChatColor.ITALIC + "");
		message = message.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		message = message.replaceAll("&n", ChatColor.UNDERLINE + "");
		message = message.replaceAll("&k", ChatColor.MAGIC + "");
		message = message.replaceAll("&r", ChatColor.RESET + "");
		return message;
	}
	
	public boolean checkConfig(String name) {
		if (new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "MINING EVENT" + System.getProperty("file.separator") + name + ".yml").exists()) {
			presetName = Main.getFileManger().loadConfig(name + ".yml", new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "MINING EVENT" + System.getProperty("file.separator") + name + ".yml"));
			try {
				//tpLoc = new Location(Bukkit.getWorld(presetName.getString("settings.tpLocation.world")), Double.parseDouble(presetName.getString("settings.tpLocation.x")), Double.parseDouble(presetName.getString("settings.tpLocation.y")), Double.parseDouble(presetName.getString("settings.tpLocation.z")));
				folderDir = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "MINING EVENT" + System.getProperty("file.separator"));
				while(presetName.contains("round" + Integer.toString(numOfRound))) {
					numOfRound++;
				}
			} catch (Exception e) {
				System.out.println("[MonkeyEvent-DEBUG] IF THIS ERROR OCCURS IT IS PROBABLY YOUR PRESET FORMATTING ERROR CHECK INDENTATIONS AND QUOTATIONS!");
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public void playerJoin(Player p) {
		if (!main.playerInEvent.contains(p.getUniqueId())) {
			main.playerInEvent.add(p.getUniqueId());
			for (UUID PE : main.playerInEvent)
				Bukkit.getPlayer(PE).sendMessage(ChatColor.translateAlternateColorCodes('&', presetName.getString("settings.playerJoinMessage").replace("%player%", p.getName())));
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), presetName.getString("settings.tpLocation.warp").replace("%player%", p.getName()));
	}
	
	public void endEvent(Player p) {
		if (!main.eventRunning[0].equals("")) {
			main.eventRunning[0] = "";
			main.eventRunning[1] = "";
			main.eventRunning[2] = "";
			main.playerInEvent.clear();
			region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
			Bukkit.getScheduler().cancelTasks(Main);
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "EVENT ENDED!");
			System.out.println("[MonkeyEvent-DEBUG] EVENT ENDED!");
		} else {
			p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "NO EVENTS ARE RUNNING!");
		}
	}
}
