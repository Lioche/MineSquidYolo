package be.lioche.compact.events;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.lioche.api.enums.Symboles;
import be.lioche.api.utils.Files;
import be.lioche.compact.cmd.Packs;
import be.lioche.compact.main.Main;

public class Signs implements Listener{

	public static String[] liste = {"MineSquid","Faithful-32", "RainBow-PVP", "LegitBlue-PVP", "MathoX-PVP"};

	public static HashMap<String, String> packs = new HashMap<>();
	public static HashMap<String, String> size = new HashMap<>();
	public Signs(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);

		packs.put("Faithful-32", "http://novacube.fr/Faithful-32.zip");
		packs.put("MineSquid", null);

		packs.put("RainBow-PVP", "http://download1862.mediafire.com/r5m33ab8vgng/j0jjh5v2qtd0l45/%C2%A78%5B%C2%A74StolenPvP%C2%A78%5D+%C2%A73RainbowFade.zip");
		packs.put("LegitBlue-PVP", "http://download1605.mediafire.com/tzu8wauzarsg/zaj9w6ki1zi315z/%C2%A71%23LegitBlue.zip");
		packs.put("MathoX-PVP", "http://download1391.mediafire.com/ks4aod40s2qg/40bh8agbdqgbv45/MathoXPackv3.zip");

		size.put("Faithful-32", "4.51 MB");
		size.put("MineSquid", "0 MB");

		size.put("RainBow-PVP", "40.49 MB");
		size.put("LegitBlue-PVP", "31.11 MB");
		size.put("MathoX-PVP", "19.45 MB");

	}



	@EventHandler
	public void signClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Action a = e.getAction();
		Block b = e.getClickedBlock();

		if(a == Action.RIGHT_CLICK_BLOCK){
			if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN){
				Sign s = (Sign)b.getState();

				String s1 = s.getLine(0);
				String s2 = s.getLine(2).replace("§f", "");

				String[] ss = s.getLines();
				ss[2] = "§b"+Symboles.ARROW_LITTLE_RIGHT.get()+" "+ss[2].replace("§f", "")+" "+Symboles.ARROW_LITTLE_LEFT.get();


				if(s1.equalsIgnoreCase("§f[§bResource pack§f]")){
					if(packs.containsKey(s2) && packs.get(s2) != null){
						if(!(Packs.getPack(p) == s2)){
							p.setResourcePack(packs.get(s2));
							p.sendMessage(Main.prefix+"Installation du ressource pack:§3 "+s2);
							p.sendMessage("§b "+Symboles.GUILL_ARROW_RIGHT.get()+" Tapez /packclear pour l'enlever.");
							p.sendSignChange(s.getLocation(), ss);
							Files.writeJSON(Main.folder, "", "packs", p.getUniqueId().toString(), s2);

							for(int i = 0; i < packs.size(); i++){
								if(s.getLocation().add(0,0,i+1).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(0,0,i+1), ((Sign)s.getLocation().add(0,0,i+1).getBlock().getState()).getLines());
								if(s.getLocation().add(0,0,-(i+1)).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(0,0,-(i+1)), ((Sign)s.getLocation().add(0,0,-(i+1)).getBlock().getState()).getLines());

								if(s.getLocation().add(i+1,0,0).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(i+1,0,0), ((Sign)s.getLocation().add(i+1,0,0).getBlock().getState()).getLines());
								if(s.getLocation().add(-(i+1),0,0).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(-(i+1),0,0), ((Sign)s.getLocation().add(-(i+1),0,0).getBlock().getState()).getLines());
							}
						}else{
							p.sendMessage(Main.prefix+"Vous avez déjà installé le pack:§3 "+s2);
						}
					}else{
						p.sendMessage(Main.prefix+"Impossible de trouver le pack:§3 "+s2);
					}
				}
			}
		}
	}

	@EventHandler
	public void placeSign(SignChangeEvent e){
		String name = e.getLines()[1];

		if(e.getLine(0).equalsIgnoreCase("[Packs]")){
			e.setLine(0, "§f[§bResource pack§f]");
			e.setLine(1, "");
			e.setLine(2, "§f"+name);
			e.setLine(3, "§f("+size.get(name)+")");
		}
	}
}
