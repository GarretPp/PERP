package me.garret.PERP;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
	
	public ArrayList<Player> police = new ArrayList<Player>();
	
	public void onEnable() {
		getLogger().info("Plugin enabled");
		

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		getLogger().info("Plugin Disabled");
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
		Player player = (Player) sender;
		String command = cmd.getName();

		if(sender instanceof Player) {
			if (command.equalsIgnoreCase("roll")) { //roll random number 1-100
				Random rand = new Random();
				int randomNum = rand.nextInt((100 - 1) + 1) + 1; //random number generator
				String number = Integer.toString(randomNum);
				Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has rolled a " + number);
				
			} else if (command.equalsIgnoreCase("describe") || command.equalsIgnoreCase("desc")) { //allows user to describe something
				if (args.length == 0) {
					player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "You must describe with text.");
				} else {
					
					String description = StringUtils.join(args, ' ');
					Bukkit.broadcastMessage(ChatColor.GOLD + "(" + player.getName() + ") " + description);
				}
				
			} else if (command.equalsIgnoreCase("advert")) { //allows user to describe something
				if (args.length == 0) {
					player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "You must add text to your advert.");
				} else {
					
					String advert = StringUtils.join(args, ' ');
					Bukkit.broadcastMessage(ChatColor.BOLD + "[Advert] (" + player.getName() + ") " + advert);
				}
				
			} else if (command.equals("act")) { //acts that will be described in chat since minecraft can't manipulate player stance...
				if (args.length > 1) {
					player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "No such act.");
				} else {
					switch(args[0]) {
						case "surrender": //to add, search inventory for weapons and drop.
							Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " has surrendered.");
						
							Material[] weapons = {Material.WOODEN_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.BOW};

					          for(Material m : weapons){
					        	  
					              if(player.getInventory().contains(m)){ //drops items 3 blocks in front of players
					                  ItemStack weaponStack = new ItemStack(m, 1); // TODO copy item meta and reuse that to take specialty items
					                  Location playerFront = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(3));
					                  player.getInventory().remove(weaponStack);
					                  player.getWorld().dropItemNaturally(playerFront, weaponStack);
					              }
					          }
							break;
						case "zombie":
							Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " waves arms like a " + ChatColor.DARK_GREEN + "zombie.");
							player.sendMessage(ChatColor.BOLD + "No sudden movements!");
							break;
						case "wave":
							Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " waves to nearby players.");
							break;
						case "salute":
							Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " salutes.");
							break;
						default:
							player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "No such act");
					}
				}
			} else if (command.equalsIgnoreCase("911")) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "You must include text in your 911 call.");
				} else {
					
					String callPolice = StringUtils.join(args, ' ');
					
					for(Player s : police) {
						s.sendMessage(ChatColor.RED + "[911]" + ChatColor.GOLD + "(" + player.getName() + ") " + callPolice);
					}
					
				}
				
			} else if (command.equalsIgnoreCase("pdLobby")) { //pd lobby menu or police menu launch
				if(!police.contains(player)) {
					openPDLobby(player);
				} else {
					policePDLobby(player);
				}
			} else if (command.equalsIgnoreCase("radio") || command.equalsIgnoreCase("gov")) { //plyer use government radio as police
				if (police.contains(player)) {
					if (args.length == 0) {
						player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "You must include text in your radio message.");
					} else {
						String radioMessage = StringUtils.join(args, ' '); // build radio message
						govRadio(radioMessage, player);
					}
				} else {
					player.sendMessage(ChatColor.GOLD + "[PERP] " + ChatColor.RED + "You cannot use the government radio.");
				}
			}
			
		} //end of IsntanceOf Player
		return false;
	}
	

	public void openPDLobby(Player p) { //PD Lobby menu
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_BLUE + "PD Lobby");

		ItemStack policeJob = new ItemStack (Material.BOW);
		ItemMeta policeJobMeta = policeJob.getItemMeta();
		policeJobMeta.setDisplayName(ChatColor.BLUE + "Go on duty?");
		policeJob.setItemMeta(policeJobMeta);
		
		ItemStack makeComplaint = new ItemStack (Material.BOOK);
		ItemMeta makeComplaintMeta = makeComplaint.getItemMeta();
		makeComplaintMeta.setDisplayName(ChatColor.RED + "Make a Complaint");
		makeComplaint.setItemMeta(makeComplaintMeta);
		
		ItemStack reportCrime = new ItemStack (Material.SIGN);
		ItemMeta reportCrimeMeta = reportCrime.getItemMeta();
		reportCrimeMeta.setDisplayName(ChatColor.GOLD + "Report a Crime");
		reportCrime.setItemMeta(reportCrimeMeta);



		ItemStack cancel = new ItemStack(Material.PAPER);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName("Cancel");
		cancel.setItemMeta(cancelMeta);

		inv.setItem(0, cancel);
		inv.setItem(1, cancel);
		inv.setItem(2, cancel);
		inv.setItem(3, policeJob);
		inv.setItem(4, makeComplaint);
		inv.setItem(5, reportCrime);
		inv.setItem(6, cancel);
		inv.setItem(7, cancel);
		inv.setItem(8, cancel);

		p.openInventory(inv);

	}
	

	public void policePDLobby(Player p) { //PD Lobby menu
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_BLUE + "Police Lobby Menu");

		ItemStack policeJob = new ItemStack (Material.BOW);
		ItemMeta policeJobMeta = policeJob.getItemMeta();
		policeJobMeta.setDisplayName(ChatColor.BLUE + "Go off duty?");
		policeJob.setItemMeta(policeJobMeta);



		ItemStack cancel = new ItemStack(Material.PAPER);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName("Cancel");
		cancel.setItemMeta(cancelMeta);

		inv.setItem(0, cancel);
		inv.setItem(1, cancel);
		inv.setItem(2, cancel);
		inv.setItem(3, cancel);
		inv.setItem(4, policeJob);
		inv.setItem(5, cancel);
		inv.setItem(6, cancel);
		inv.setItem(7, cancel);
		inv.setItem(8, cancel);

		p.openInventory(inv);

	}

	////////////////////////
	// Check Item Clicked //
	////////////////////////
	@EventHandler
	public void onInventoryEvent(InventoryClickEvent e) {

		Player p =(Player) e.getWhoClicked();
		String chest = ChatColor.stripColor(e.getInventory().getName());
		Material itemClicked = e.getCurrentItem().getType();


		if(e.getCurrentItem()==null || e.getCurrentItem().getType()==Material.AIR) {
			return;

		}
		
		if(chest.equalsIgnoreCase("PD Lobby")) { //if for each chest
			switch(itemClicked){
			case BOW:
				e.setCancelled(true);
				police.add(p);
				govRadio(p.getName() + " is now 10-8.", p);
				p.closeInventory();
				break;
			case BOOK:
				e.setCancelled(true);
				p.sendMessage(ChatColor.GOLD + "[PERP]" + ChatColor.RED + " To make a complaint go to https://plpd.online/internalaffairs/complaint/create");
				p.closeInventory();
				break;
			case SIGN:
				e.setCancelled(true);
				p.sendMessage(ChatColor.GOLD + "[PERP]" + ChatColor.RED + " Send a 911 message with /911 <text>");
				p.closeInventory();
				break;
			case PAPER:
				e.setCancelled(true);
				p.closeInventory();
				break;
			default:
				e.setCancelled(true);
				break;
			}
			return;
		} else if (chest.equalsIgnoreCase("Police Lobby Menu")) {

			switch(itemClicked){
				case BOW:
					e.setCancelled(true);
					govRadio(p.getName() + " is now 10-7.", p);
					police.remove(p);
					p.closeInventory();
					break;
				case PAPER:
					e.setCancelled(true);
					p.closeInventory();
					break;
				default:
					e.setCancelled(true);
					break;
			}
		}

	}
	
	private void govRadio(String s, Player p) { //government radio method to make easier and more uniform
		for (Player cops : police) {
			cops.sendMessage(ChatColor.BLUE + "[Officer " + p.getName() + "] " + s);
		}
		
	}

	public void onQuit(final PlayerQuitEvent event) {
		Player p = event.getPlayer(); //remove from Police on disconnect
		if (police.contains(p)) {
			police.remove(p);
		}
	}
	
}
