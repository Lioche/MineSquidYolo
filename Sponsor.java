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
			if(!p.hasPermission("lioche.compact.sponsor.vip")){
				final NMSChicken nms = new NMSChicken(p.getWorld(), p);
				int min = Main.instance.getConfig().getInt("sponsor."+p.getUniqueId());

				if(Main.instance.getConfig().contains("sponsor."+p.getUniqueId())){			
					if(min == 0){
						nms.setup();
						nms.setNoClip(false, 17); 
						EntityTypes.spawnEntity(nms, nms.getLocation());
						Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
							public void run() {
								nms.die();
							}
						}, 20*60L);

						p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
						Main.instance.getConfig().set("sponsor."+p.getUniqueId(), 30);
						Main.instance.saveConfig();
						launchCooldown(p);

						return true;
					}else{
						p.sendMessage(Main.prefix + "Votre sponsor sera disponible dans: "+ min + " min.");
						return false;
					}
				}else{
					nms.setup();
					nms.setNoClip(false, 17); 
					EntityTypes.spawnEntity(nms, nms.getLocation());
					Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
						public void run() {
							nms.die();
						}
					}, 20*60L);

					p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
					Main.instance.getConfig().set("sponsor."+p.getUniqueId(), 30);
					Main.instance.saveConfig();
					launchCooldown(p);

					return true;
				}
			}else{
				p.sendMessage(Main.prefix+"Erreur: Vous avez accès au /sponsorvip");
			}
		}
		return false;
	}

	public static void launchCooldown(final Player p){
		if(Main.instance.getConfig().getInt("sponsor."+p.getUniqueId()) != 0){

			final int c = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
				int min = Main.instance.getConfig().getInt("sponsor."+p.getUniqueId());
				public void run() {
					if(min != 0){
						if(min == 1){
							Main.instance.getConfig().set("sponsor."+p.getUniqueId(), 0);
							Main.instance.saveConfig();
						}else{
							min = min--;
							Main.instance.getConfig().set("sponsor."+p.getUniqueId(), min--);
							Main.instance.saveConfig();
						}
					}
				}
			}, 20L*60L, 20L*60L);

			Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
				public void run() {
					Bukkit.getScheduler().cancelTask(c);
				}
			}, 20*60*30L);
		}
	}
}
