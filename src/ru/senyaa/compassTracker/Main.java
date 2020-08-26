package ru.senyaa.compassTracker;

import static ru.senyaa.compassTracker.Locale.localText;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		Locale.setRussian();
		this.getCommand("compass").setExecutor(new Compass());
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("compass").setTabCompleter(new TabController());
		getLogger().info("CompassTracker has been loaded successfully!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("CompassTracker has been disabled successfully!");
	}
	
	public static List<TrackingCompass> compasses = new ArrayList<TrackingCompass>();
	public static List<Player> compassUsers = new ArrayList<Player>();
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains(localText[18]) && event.getItemDrop().getItemStack().getItemMeta().hasLore()) 
			event.setCancelled(true);
	}
	@EventHandler
	public void onSwapHand(PlayerSwapHandItemsEvent event) {
		if(event.getOffHandItem().getItemMeta().getDisplayName().contains(localText[18]) && event.getOffHandItem().getItemMeta().hasLore()) {
			event.setCancelled(true);
		}
	}
	public void onClick(InventoryClickEvent event) {
		if(event.getCurrentItem().getItemMeta().getDisplayName().contains(localText[18]) && event.getCurrentItem().getItemMeta().hasLore()) {
			event.setCancelled(true);
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		for(TrackingCompass compass : compasses) {
			if(compass.target == event.getPlayer()) {
				compass.lastLocation = compass.getLastLocation(compass.target);
					CompassMeta meta = (CompassMeta) compass.item.getItemMeta();
					meta.setLodestoneTracked(false);
					meta.setLodestone(compass.lastLocation);
					compass.item.setItemMeta((ItemMeta)meta);
					try {	
						compass.player.getInventory().remove(Material.COMPASS);
						compass.player.getInventory().setItem(8, compass.item);
					} catch(Exception e) {}
				
					switch(compass.target.getWorld().getName()) {
						case "world":
							compass.player.sendTitle("", ChatColor.BLUE + compass.target.getDisplayName() + ChatColor.AQUA + localText[6] + ChatColor.GREEN + localText[3]);
							break;
							
						case "world_nether":
							compass.player.sendTitle("", ChatColor.BLUE + compass.target.getDisplayName() + ChatColor.AQUA + localText[6] + ChatColor.RED + localText[4]);
							break;
							
						case "world_the_end":
							compass.player.sendTitle("", ChatColor.BLUE + compass.target.getDisplayName() + ChatColor.AQUA + localText[6] + ChatColor.YELLOW + localText[5]);
							break;
				}
			}
		}
	}
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Action eventAction = event.getAction();
		if(event.getHand() == EquipmentSlot.OFF_HAND) {return;}
		if(eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK) {
			try {
				if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(localText[18])
						&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					for(TrackingCompass compass : compasses) {
						if(compass.player == event.getPlayer() && compass.target != null) {
							event.getPlayer().sendMessage(ChatColor.AQUA + localText[7]);
							event.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "X: " + String.valueOf(Math.round(compass.target.getLocation().getX())));
							event.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Z: " + String.valueOf(Math.round(compass.target.getLocation().getZ())));
						}
					}
				}
			} catch (Exception e) {}
		}
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		TrackingCompass toRemove = null;
		boolean remove = false;
		for(TrackingCompass compass : compasses) {
			if(compass.target == player) {
				compass.target = null;
				compass.item = Compass.getItem();
				try {	
					compass.player.getInventory().remove(Material.COMPASS);
					compass.player.getInventory().setItem(8, compass.item);
				} catch(Exception e) {}
				compass.player.sendMessage(ChatColor.RED + player.getDisplayName() + localText[19]);
			} else if(compass.player == player) {
				compass.player = null;
				compass.target = null;
				compass.item = null;
				try {	
					player.getInventory().remove(Material.COMPASS);
				} catch(Exception e) {}
				remove = true;
				toRemove = compass;
			}
		}
		if(remove) {
			compasses.remove(toRemove);
		}
	}
}

