package be.lioche.compact.events;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

import be.lioche.compact.main.Main;
import be.lioche.compact.obj.NMSChicken;

public class ChickenInteract implements Listener{

	public ChickenInteract(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public static void openChicken(PlayerInteractAtEntityEvent e){
		if(e.getRightClicked() instanceof Chicken){
			Player p = e.getPlayer();
			final Chicken chicken = (Chicken)e.getRightClicked();
			final NMSChicken nms = new NMSChicken(p.getWorld(), p);

			if(chicken.getUniqueId().equals(nms.getUniqueID()) && chicken.getUniqueId().equals(p.getUniqueId())){
				Inventory inv = nms.getInventory();
				p.openInventory(inv);
				chicken.remove();

			}else{
				p.sendMessage(Main.prefix+"Ce sponsor ne vous est pas destiné.");
			}
		}
	}
}
