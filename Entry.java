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
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.potion.PotionEffectType;

import be.lioche.api.packet.Holo;
import be.lioche.api.utils.Others;
import be.lioche.compact.main.Main;

public class Entry implements Listener{

	public Entry(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public void join(PlayerJoinEvent e){
		final Player p = e.getPlayer();
		final Holo h = new Holo();
		Set<Material> transparent = new HashSet<Material>();
		transparent.add(Material.AIR);
		Block block = p.getTargetBlock(transparent, 2);

		if(!Main.instance.getConfig().contains("sponsor."+p.getUniqueId())){
			Main.instance.getConfig().set("sponsor."+p.getUniqueId(), 30);
		}
		if(p.getMaxHealth() != 40.0)p.setMaxHealth(40.0);
		Others.addPotion(p, PotionEffectType.REGENERATION, 20*10, 1);

		h.create(block.getLocation(), "§b* Bienvenue, "+p.getName() +" *");
		h.show(p);
		
		p.setExhaustion(p.getExhaustion()/3);

		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			@Override
			public void run(){
				h.destroy(p);
				if(Main.instance.getConfig().getString("Packs."+p.getUniqueId()) != null){
					p.setResourcePack(Signs.packs.get(Main.instance.getConfig().getString("Packs."+p.getUniqueId())));
					p.sendMessage(Main.prefix+"Récupération de votre resource pack..");
				}
			}
		}, 20L*3);



	}

	@EventHandler
	public void tryJoin(PlayerLoginEvent e){
		if(e.getResult() != Result.ALLOWED){
			if(e.getResult() == Result.KICK_WHITELIST){
				e.setKickMessage(Main.prefix+"\n\n§bLe serveur sera ouvert le:\n§bMercredi 04-11 à 16:30");
			}else if(e.getResult() == Result.KICK_FULL){
				e.setKickMessage(Main.prefix+"\n\n§bLe serveur est full.");
			}
			Bukkit.broadcastMessage("§7[§c§lINFO§7]§c "+e.getPlayer().getName()+" a essayé de rejoindre le serveur.");
		}
	}
}
