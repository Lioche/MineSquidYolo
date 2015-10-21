package be.lioche.compact.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

import be.lioche.api.nms.NMSChicken;
import be.lioche.compact.main.Main;

public class ChickenInteract implements Listener{

	public ChickenInteract(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public static void openChicken(PlayerInteractAtEntityEvent e){
		if(e.getRightClicked() instanceof Chicken){
			Player p = e.getPlayer();
			final Chicken chicken = (Chicken)e.getRightClicked();
			List<UUID> uuids = new ArrayList<>();

			for(Player pl : Bukkit.getOnlinePlayers()){
				uuids.add(pl.getUniqueId());
			}
			
			if(uuids.contains(chicken.getUniqueId())){
				NMSChicken nms = new NMSChicken(p.getWorld(), p);
				nms.setupItems();
				Inventory inv = nms.getChickInventory();

				if(chicken.getUniqueId().equals(p.getUniqueId())){
					p.openInventory(inv);

					Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
						public void run() {
							chicken.remove();
						}
					}, 50L);
				}else{
					p.sendMessage(Main.prefix+"Ce sponsor ne vous est pas destiné.");
				}
				
				nms.die();
			}
			
			
		}
	}	
}
