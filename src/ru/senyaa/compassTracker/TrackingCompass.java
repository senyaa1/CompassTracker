package ru.senyaa.compassTracker;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrackingCompass {
	public ItemStack item;
	public Player player;
	public Player target;
	public Location lastLocation;
	public boolean isActive;
	
	private Map<Player, Location> lastLocations = new HashMap<Player, Location>();
	
	public TrackingCompass(ItemStack item, Player player, Player target) {
		this.item = item;
		this.player = player;
		this.target = target;
		runLastLoc();
	}
	
	private void runLastLoc() {	
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
	        @Override
	        public void run() {
	           for(TrackingCompass compass : Main.compasses) {
	        	   try {lastLocations.put(compass.target, compass.target.getLocation());} catch(Exception e) {}
	           }
	        }
	    }, 2, 20);
	}
	
	public Location getLastLocation(Player player) {
		return lastLocations.get(player);
	}
}