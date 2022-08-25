package com.stinkymonkey.events.KothEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.stinkymonkey.events.ScoreHelper;
import com.stinkymonkey.events.main;

public class KothEvent implements Listener {
	private main Main;
	public KothEvent(main Main) {
		this.Main = Main;
	}
	
	FileConfiguration presetFile;
	File folderDir;
	Location loc1;
	Location loc2;
	Player leader;
	int numOfRound = 0;
	int time;
	int taskTimer;
	
	public HashMap<UUID, Integer> playerTimers = new HashMap<UUID, Integer>();
	public List<UUID> playerInZone = new ArrayList<UUID>();
	
	public void runEvent(Player p, String preName) {
		if (!checkConfig(preName)) {
			p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Event Preset Does Not Exist!");
			return;
		}
		try {
			File configFile = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "KOTH EVENT" + System.getProperty("file.separator") + "exampleKoth.yml");
			FileUtils.copyToFile(Main.getResource("exampleKoth.yml"), configFile);
			
			main.eventRunning[0] = "KOTH EVENT";
			main.eventRunning[1] = preName;
			main.eventRunning[2] = "0";
			runRound(p);
		} catch (Exception e) {
			
		}
	}
	
	public void runRound(Player p) {
		int roundNum = Integer.parseInt(main.eventRunning[2]);
		int Delay = 0;
		if (roundNum > numOfRound) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "There Are No More Rounds To Execute");
			return;
		}
		for (String s : presetFile.getStringList("round" + Integer.toString(roundNum))) {
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
			} else if (s.contains("TITLE ")) {
				try {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
						public void run() {
							for (UUID PE : main.playerInEvent) {
								Bukkit.getPlayer(PE).sendTitle(ChatColor.translateAlternateColorCodes('&', s).replace("TITLE ", ""), "");
							}
						}
					}, Delay );
				} catch (Exception e) {
					System.out.println("[MonkeyEvent-DEBUG] ERROR EXECUTING TITLE");
					p.sendMessage(ChatColor.RED + "ERROR EXECUTING TITLE CHECK CONSOLE");
					e.printStackTrace();
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
					System.out.println("[MonkeyEvent-DEBUG] ERROR EXECUTING COMMAND");
					p.sendMessage(ChatColor.RED + "ERROR EXECUTING COMMAND CHECK CONSOLE");
					e.printStackTrace();
				}
			} else if (s.contains("RUN KOTH")) {
				try {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
						public void run() {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Starting Koth!");
							runGame(p);
						}
					}, Delay);
					Delay += presetFile.getInt("settings.timeLimit") * 20;
				} catch (Exception e) {
					System.out.println("[MonkeyEvent-DEBUG] ERROR RUNNING KOTH");
					p.sendMessage(ChatColor.RED + "ERROR EXECUTING KOTH CHECK CONSOLE");
					e.printStackTrace();
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
	
	public void runGame(Player p) {
		time = 0;
	    System.out.println("[MonkeyEvents] Launching KOTH Settings Loaded");
		taskTimer = Main.getServer().getScheduler().scheduleSyncRepeatingTask(Main, new Runnable() {
			@Override
			public void run() {
				String contester;
				String contesterTime;
				int playerIn = playerInZone.size();
				// check time
				if (time >= presetFile.getInt("settings.timeLimit")) {
					endKoth();
				}
				// check zone for 1 player
				if (playerIn == 1) {
					playerTimers.replace(playerInZone.get(0), playerTimers.get(playerInZone.get(0)) + 1);
					contester = Bukkit.getPlayer(playerInZone.get(0)).getName();
					contesterTime = Integer.toString(playerTimers.get(playerInZone.get(0)));
				} else if (playerIn > 1) {
					contester = "CONTESTED";
					contesterTime = "N/A";
				} else {
					contester = "NONE";
					contesterTime = "N/A";
				}
				// Get Leader
				Optional<Entry<UUID, Integer>> uidLeader = playerTimers.entrySet().stream().filter(e -> e.getValue().equals(Collections.max(playerTimers.values()))).findFirst();
				leader = Bukkit.getPlayer(uidLeader.get().getKey());
				if (playerTimers.get(leader.getUniqueId()) >= presetFile.getInt("settings.timeToWin")) {
					endKoth();
				}
				// edit scoreboards
				List<String> scores = presetFile.getStringList("settings.scoreboard.scores");
				for (UUID pl : main.playerInEvent) {
					int index = presetFile.getStringList("settings.scoreboard.scores").size();
					ScoreHelper helper = ScoreHelper.getByPlayer(Bukkit.getPlayer(pl));
					for (String s : scores) {
						String slotMsg = ChatColor.translateAlternateColorCodes('&', s
								.replace("%cplayer%", contester)
								.replace("%ctime%", contesterTime)
								.replace("%lplayer%", leader.getName())
								.replace("%ltime%", Integer.toString(playerTimers.get(leader.getUniqueId())))
								.replace("%twin%", Integer.toString(presetFile.getInt("settings.timeToWin")))
								.replace("%timeleft%", Integer.toString(presetFile.getInt("settings.timeLimit") - time)));
						helper.setSlot(index, slotMsg);
						
						index--;
					}
				}
				// timer
				time++;
			}
		}, 20, 20);
	}
	
	public void endKoth() {
		for (Player play : Bukkit.getOnlinePlayers()) {
			play.sendMessage(ChatColor.translateAlternateColorCodes('&', presetFile.getString("settings.playerWinMessage")).replace("%player%", leader.getName()));
		}
		for (UUID play : main.playerInEvent) {
			ScoreHelper.removeScore(Bukkit.getPlayer(play));
			ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();   
			Bukkit.getPlayer(play).setScoreboard(sm.getNewScoreboard());
		}
		if (presetFile.getBoolean("settings.fireworkWin")) {
			int delay = 0;
			for (int i = 0; i <= 3; i++) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
					public void run() {
					Location loc = leader.getLocation();
					Firework firework = leader.getWorld().spawn(loc, Firework.class);
					FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
					data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL_LARGE).build());
					data.setPower(3);
					firework.setFireworkMeta(data);
					Bukkit.getScheduler().cancelTask(taskTimer);
					}
				}, delay);
				delay += 15;
			}
		}
	}
	
	public void endEvent(Player p) {
		if (!main.eventRunning[0].equals("")) {
			for (UUID play : main.playerInEvent) {
				if(ScoreHelper.hasScore(Bukkit.getPlayer(play))) {
					ScoreHelper.removeScore(Bukkit.getPlayer(play));
					ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();   
					Bukkit.getPlayer(play).setScoreboard(sm.getNewScoreboard());
				}
			}
			main.eventRunning[0] = "";
			main.eventRunning[1] = "";
			main.eventRunning[2] = "";
			main.playerInEvent.clear();
			playerInZone.clear();
			playerTimers.clear();
			Bukkit.getScheduler().cancelTasks(Main);
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "EVENT ENDED!");
			System.out.println("[MonkeyEvent-DEBUG] EVENT ENDED!");
		} else {
			p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "NO EVENTS ARE RUNNING!");
		}
	}
	
	public void playerJoin(Player p) {
		if (!main.playerInEvent.contains(p.getUniqueId())) {
			main.playerInEvent.add(p.getUniqueId());
			for (UUID PE : main.playerInEvent)
				Bukkit.getPlayer(PE).sendMessage(ChatColor.translateAlternateColorCodes('&', presetFile.getString("settings.playerJoinMessage").replace("%player%", p.getName())));
			playerTimers.put(p.getUniqueId(), 0);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), presetFile.getString("settings.warp").replace("%player%", p.getName()));
			ScoreHelper helper = ScoreHelper.createScore(p);
			helper.setTitle(ChatColor.translateAlternateColorCodes('&', presetFile.getString("settings.scoreboard.Title")));
		} else 
			p.sendMessage(ChatColor.RED + "You are already in the event!");
	}
	
	public boolean checkConfig(String name) {
		if (new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "KOTH EVENT" + System.getProperty("file.separator") + name + ".yml").exists()) {
			presetFile = Main.getFileManger().loadConfig(name + ".yml", new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "KOTH EVENT" + System.getProperty("file.separator") + name + ".yml"));
			try {
				folderDir = new File("plugins" + System.getProperty("file.separator") + Main.getDescription().getName() + System.getProperty("file.separator") + "Events" + System.getProperty("file.separator") + "KOTH EVENT" + System.getProperty("file.separator"));
				while (presetFile.contains("round" + Integer.toString(numOfRound)))
					numOfRound++;
			} catch (Exception e) {
				System.out.println("[MonkeyEvent-DEBUG] IF THIS ERROR OCCURS IT IS PROBABLY YOUR PRESET FORMATTING ERROR CHECK INDENTATIONS AND QUOTATIONS!");
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public boolean inRegion(Player p) {
		World world = Bukkit.getWorld(presetFile.getString("settings.world"));
		Location pointA = new Location(world, presetFile.getDouble("settings.Location1.x"), presetFile.getDouble("settings.Location1.y"), presetFile.getDouble("settings.Location1.z"));
		Location pointB = new Location(world, presetFile.getDouble("settings.Location2.x"), presetFile.getDouble("settings.Location2.y"), presetFile.getDouble("settings.Location2.z"));
		boolean w = pointA.getWorld().getName() == (p.getLocation().getWorld().getName());
		boolean x = p.getLocation().getBlockX() >= Math.min(pointA.getBlockX(), pointB.getBlockX()) &&  p.getLocation().getBlockX() <= Math.max(pointA.getBlockX(), pointB.getBlockX());
		boolean y = p.getLocation().getBlockY() >= Math.min(pointA.getBlockY(), pointB.getBlockY()) &&  p.getLocation().getBlockY() <= Math.max(pointA.getBlockY(), pointB.getBlockY());
		boolean z = p.getLocation().getBlockZ() >= Math.min(pointA.getBlockZ(), pointB.getBlockZ()) &&  p.getLocation().getBlockZ() <= Math.max(pointA.getBlockZ(), pointB.getBlockZ());
		return w && x && y && z;
	}
	
	@EventHandler
	public void onMovement(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (main.playerInEvent.contains(p.getUniqueId())) {
			if (inRegion(p)) {
				if (!playerInZone.contains(p.getUniqueId())) {
					playerInZone.add(p.getUniqueId());
				}
			} else {
				if (playerInZone.contains(p.getUniqueId())) {
					playerInZone.remove(p.getUniqueId());
				}
			}
		}
	}
}
