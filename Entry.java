package be.lioche.compact.events;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import be.lioche.api.packet.Holo;
import be.lioche.compact.main.Main;

public class Entry implements Listener{

	public Entry(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public void join(PlayerJoinEvent e){
		final Player p = e.getPlayer();
		
		if(!Main.instance.getConfig().contains("sponsor."+p.getUniqueId())){
			Main.instance.getConfig().set("sponsor."+p.getUniqueId(), 30);
		}
		p.setMaxHealth(40.0);
		
		Set<Material> transparent = new HashSet<Material>();
        transparent.add(Material.AIR);
        Block block = p.getTargetBlock(transparent, 2);
		final Holo h = new Holo();
		h.create(block.getLocation(), "§bBienvenue, "+p.getName());
		h.show(p);
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			
			@Override
			public void run() {
				h.destroy(p);
			}
		}, 20L*3);
	}
}
