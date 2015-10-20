package be.lioche.compact.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import be.lioche.compact.main.Main;
import be.lioche.compact.obj.EntityTypes;
import be.lioche.compact.obj.NMSChicken;

public class Sponsor implements Listener, CommandExecutor{

	public Sponsor(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {

		if(sender instanceof Player){
			Player p = (Player)sender;
			int min = Main.instance.getConfig().getInt(p.getUniqueId()+".sponsor");

			if(Main.instance.getConfig().contains(p.getUniqueId().toString())){
				if(p.hasPermission("lioche.sponsor")){
					NMSChicken nms = new NMSChicken(p.getWorld(), p);
					EntityTypes.spawnEntity(nms, nms.getLocation());
					p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
				}else{
					if(min == 0){
						NMSChicken nms = new NMSChicken(p.getWorld(), p);
						EntityTypes.spawnEntity(nms, nms.getLocation());
						p.sendMessage(Main.prefix+"Ton sponsor arrive par les airs.");
						Main.instance.getConfig().set(p.getUniqueId()+".sponsor", 0);
						Main.instance.saveConfig();
					}else{
						p.sendMessage(Main.prefix + " Votre sponsor sera disponible dans: "+ min + " min.");
					}
				}
			}else{
				Main.instance.getConfig().set(p.getUniqueId()+".sponsor", 0);
				Main.instance.saveConfig();
			}

		}
		return false;
	}

}
