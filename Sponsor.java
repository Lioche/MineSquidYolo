package be.lioche.compact.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import be.lioche.api.nms.EntityTypes;
import be.lioche.api.nms.NMSChicken;
import be.lioche.compact.main.Main;

public class Sponsor implements Listener, CommandExecutor{

	public Sponsor(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {

		if(sender instanceof Player){
			Player p = (Player)sender;
			NMSChicken nms = new NMSChicken(p.getWorld(), p);
//			Location loc; 
//			Block bl;
			int min = Main.instance.getConfig().getInt(p.getUniqueId()+".sponsor");

			if(Main.instance.getConfig().contains(p.getUniqueId().toString())){				
				if(min == 0){
//					REMPLACE PAR LA FONCTION SETNOCLIP, TRAVERSE LES MURS.
//					
//					for(int b = (int) p.getLocation().getY(); b < p.getLocation().getY()+22; b++){
//						loc = p.getLocation();
//						loc.setY(b);
//
//						bl = loc.getBlock();
//						if(bl.getType() != Material.AIR){
//							p.sendMessage(Main.prefix+"Il ne doit pas y avoir de bloc au dessus de vous.");
//							return false;
//						}
//					}
					nms.setup();
					nms.setNoClip(false, 17); 
					EntityTypes.spawnEntity(nms, nms.getLocation());
					
					p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
					Main.instance.getConfig().set(p.getUniqueId()+".sponsor", 30);
					Main.instance.saveConfig();
					launchCooldown(p);
					
					return true;
				}else{
					p.sendMessage(Main.prefix + "Votre sponsor sera disponible dans: "+ min + " min.");
					return false;
				}
			}else{
//				REMPLACE PAR LA FONCTION SETNOCLIP, TRAVERSE LES MURS.
//				
//				for(int b = (int) p.getLocation().getY(); b < p.getLocation().getY()+22; b++){
//					loc = p.getLocation();
//					loc.setY(b);
//
//					bl = loc.getBlock();
//					if(bl.getType() != Material.AIR){
//						p.sendMessage(Main.prefix+"Il ne doit pas y avoir de bloc au dessus de vous.");
//						return false;
//					}
//				}
				nms.setup();
				nms.setNoClip(false, 17); 
				EntityTypes.spawnEntity(nms, nms.getLocation());
				
				p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
				Main.instance.getConfig().set(p.getUniqueId()+".sponsor", 30);
				Main.instance.saveConfig();
				launchCooldown(p);
				
				return true;
			}

		}
		return false;
	}

	public static void launchCooldown(final Player p){
		if(Main.instance.getConfig().getInt(p.getUniqueId()+".sponsor") != 0){
			final int c = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
				int min = Main.instance.getConfig().getInt(p.getUniqueId()+".sponsor");
				public void run() {
					if(min != 0){
						min = min--;
						Main.instance.getConfig().set(p.getUniqueId()+".sponsor", min--);
						Main.instance.saveConfig();
					}
				}
			}, 20*60L, 20*60L);

			Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
				public void run() {
					Bukkit.getScheduler().cancelTask(c);
				}
			}, 20*60*30L);
		}
	}
}
