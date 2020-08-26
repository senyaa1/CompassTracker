package ru.senyaa.compassTracker;

import static ru.senyaa.compassTracker.Locale.localText;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Compass implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("compass")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args.length == 0 || args.length > 2) {
					sender.sendMessage(ChatColor.RED + localText[0]);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("set") && args.length == 2) {
					if(Bukkit.getPlayer(args[1]) != null) {
						for(TrackingCompass compass : Main.compasses) {
							if(compass.player == player) {
								Player target = Bukkit.getPlayer(args[1]);
								compass.target = target;
								ItemStack newItem = compass.item;
								ItemMeta newMeta = newItem.getItemMeta();
								
								List<String> lore = new ArrayList<String>();
								lore.add("");
								lore.add(ChatColor.GREEN + localText[8] + ChatColor.GOLD + "" + ChatColor.ITALIC + compass.target.getDisplayName());
								newMeta.setLore(lore);
								newItem.setItemMeta(newMeta);
								compass.item = newItem;
								try {
									if(compass.player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(localText[18])
											&& compass.player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
										compass.player.getInventory().setItemInMainHand(compass.item);
										compass.isActive = true;
										runSearch(target);
										sender.sendMessage(ChatColor.GREEN + localText[8] + ChatColor.RED + compass.target.getDisplayName());
										return true;
									}
								} catch(Exception e) {
									sender.sendMessage(ChatColor.RED + localText[9]);
									return true;
								}
								sender.sendMessage(ChatColor.RED + localText[9]);
								return true;
							}
						}
						sender.sendMessage(ChatColor.RED + localText[10] + ChatColor.GOLD + localText[1] + ChatColor.RED + localText[11]);
						return true;
					}
					sender.sendMessage(ChatColor.RED + localText[12]);
					return true;
				} else if(args[0].equalsIgnoreCase("get") && args.length == 1) {
					
					if(!Main.compassUsers.contains(player)) {
						player.getInventory().setItem(8, getItem());
						Main.compasses.add(new TrackingCompass(getItem(), player, null));
						Main.compassUsers.add(player);
						sender.sendMessage(ChatColor.GREEN + localText[13]);
						player.setCompassTarget(new Location(player.getWorld(),0.0d,0.0d,0.0d));
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + localText[14]);
						return true;
					}
				} else if(args[0].equalsIgnoreCase("remove") && args.length == 1) {
					try {
						if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(localText[18]) 
								&& player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
							for(TrackingCompass compass : Main.compasses) {
								if(compass.player == player) {
									compass.isActive = false;
								}
							}
							player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Main.compassUsers.remove(player);
							player.sendMessage(ChatColor.GREEN + localText[15]);
						}
					} catch(Exception e) {
						sender.sendMessage(ChatColor.RED + localText[9]);
					}
				} else { 
					
					sender.sendMessage(ChatColor.RED + localText[0]);
					return true;
					
				}
			} else {
				sender.sendMessage("You are not a player");
				return true;
			}
		}
		return false;
	}
	
	public static ItemStack getItem () {
		
		ItemStack compassItem = new ItemStack(Material.COMPASS);
		ItemMeta meta = compassItem.getItemMeta();
		
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + localText[18]);
		
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.RED + localText[16] + ChatColor.GOLD + localText[2] + ChatColor.RED + localText[17]);
		meta.setLore(lore);
		
		meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setUnbreakable(true);
		
		compassItem.setItemMeta(meta);
		
		return compassItem;
	}
	
	public void runSearch(Player target) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
	        @Override
	        public void run() {
	        	for (TrackingCompass compass : Main.compasses) {
	    			try {
	    				if(compass.target.getWorld() == compass.player.getWorld()) {
	    					if(compass.target == target) {
	    						if(compass.isActive) {
		    						CompassMeta meta = (CompassMeta) compass.item.getItemMeta();
		    						meta.setLodestoneTracked(false);
		    						meta.setLodestone(compass.target.getLocation());
		    						compass.item.setItemMeta((ItemMeta)meta);
		    						try {
		    							compass.player.getInventory().remove(Material.COMPASS);
		    							compass.player.getInventory().setItem(8, compass.item);
		    						} catch(Exception e) {}
	    						} 
	    					}
	    				}
	    			} catch(Exception e) {}
	    		}
	        }
	    }, 2, 60);		
	}
}
