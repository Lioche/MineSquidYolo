package be.lioche.compact.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import be.lioche.api.utils.Others;
import be.lioche.compact.main.Main;

public class ThrownBottle implements Listener{

	public ThrownBottle(Main main) {
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public void BottleLaunch(ProjectileLaunchEvent e){
		if(e.getEntity() instanceof ThrownExpBottle){
			ThrownExpBottle bottle = (ThrownExpBottle)e.getEntity();

			Player p = (Player)e.getEntity().getShooter();
			ItemStack i = p.getItemInHand();

			String name = i.getItemMeta().getDisplayName();
			if(name != null){
				if(name.contains("§aBouteille d'exp:")){
					name = name.split(":")[1];
					int lvl = Integer.parseInt(name.substring(1, 3).replace(" ", ""));
					bottle.setCustomName(lvl+"");
					bottle.setCustomNameVisible(false);
				}
			}
		}
	}

	@EventHandler
	public void BottleExplode(ExpBottleEvent e){
		String name = e.getEntity().getCustomName();
		if(name != null){
			int xp = Integer.parseInt(name);
			Player p = (Player)e.getEntity().getShooter();
			int calcul = Others.getExpForLevelUp(p, xp);

			e.setExperience(calcul);
		}
	}
}
