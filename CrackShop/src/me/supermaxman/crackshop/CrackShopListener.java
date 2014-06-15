package me.supermaxman.crackshop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class CrackShopListener implements Listener {


	@EventHandler
	public void onSignchange(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[SellShop]") && e.getPlayer().isOp() && !e.getLine(1).equals("") && !e.getLine(2).equals("") && !e.getLine(3).equals("")) {
			try {
				int id = Integer.parseInt(e.getLine(1));
				int amt = Integer.parseInt(e.getLine(2));
				int price = Integer.parseInt(e.getLine(3));
				String loc = CrackShop.makeString(e.getBlock().getLocation());
				CrackShop.signs.put(loc, new Shop(loc, id, amt, price));
				CrackShop.plugin.saveFiles();
				e.getPlayer().sendMessage(ChatColor.AQUA+"Shop created.");
				e.setLine(0, ChatColor.AQUA+"[SellShop]");
				e.setLine(1, ChatColor.YELLOW+"ID: "+id);
				e.setLine(2, ChatColor.GREEN+"Amount: "+amt);
				e.setLine(3, ChatColor.GOLD+"Price: "+price);
			}catch(NumberFormatException er) {
				e.getPlayer().sendMessage(ChatColor.RED+"Incorrect format, please use this format:");
				e.getPlayer().sendMessage(ChatColor.AQUA+"[SellShop]");
				e.getPlayer().sendMessage(ChatColor.AQUA+"ID");
				e.getPlayer().sendMessage(ChatColor.AQUA+"Ammount");
				e.getPlayer().sendMessage(ChatColor.AQUA+"Price");
			}
		}
	}


	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(CrackShop.signs.containsKey(CrackShop.makeString(e.getBlock().getLocation())) && !e.isCancelled()) {
			CrackShop.signs.remove(CrackShop.makeString(e.getBlock().getLocation()));
			e.getPlayer().sendMessage(ChatColor.AQUA+"Shop removed.");
			CrackShop.plugin.saveFiles();
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock() == null
				|| e.isCancelled()
				|| e.getPlayer() == null) {
			return;
		}
		Action action = e.getAction();
		if (action.equals(Action.LEFT_CLICK_BLOCK )){
			if(CrackShop.signs.containsKey(CrackShop.makeString(e.getClickedBlock().getLocation()))) {
				e.setCancelled(true);
				Shop shop = CrackShop.signs.get(CrackShop.makeString(e.getClickedBlock().getLocation()));
				Player p = e.getPlayer();
				if(e.getItem()!=null) {
					ItemStack i = e.getItem();
					if(i.getTypeId()==shop.getId()) {
						if(i.getAmount()>=shop.getAmount()) {
							if(i.getAmount()==shop.getAmount()) {
								p.setItemInHand(null);
							}
							i.setAmount(i.getAmount()-shop.getAmount());
							CrackShop.economy.depositPlayer(p.getName(), shop.getPrice());
							e.getPlayer().sendMessage(ChatColor.AQUA+"You have sold "+ChatColor.GREEN+ shop.getAmount()+ChatColor.AQUA+ " of ID "+ ChatColor.YELLOW + shop.getId()+ ChatColor.AQUA+" for "+ ChatColor.GOLD + shop.getPrice() +" "+CrackShop.economy.currencyNamePlural()+ ChatColor.AQUA+".");
						}else {
							e.getPlayer().sendMessage(ChatColor.RED+"Not enough of item.");
						}
					}else {
						e.getPlayer().sendMessage(ChatColor.RED+"Not correct item.");
					}
				}
			}

		}
	}

}
