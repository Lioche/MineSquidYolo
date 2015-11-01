package be.lioche.compact.events;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import be.lioche.api.enums.Symboles;
import be.lioche.compact.main.Main;

public class Signs implements Listener{

	public static HashMap<String, String> packs = new HashMap<>();
	public Signs(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
		packs.put("Faithful-32", "http://novacube.fr/Faithful-32.zip");
		packs.put("MineSquid", null);
		packs.put("SoulBound-PVP", "https://www.dropbox.com/s/i03xgvnpec7srq3/SoulBound.zip?dl=1");
		packs.put("RainBow-PVP", "http://download1862.mediafire.com/r5m33ab8vgng/j0jjh5v2qtd0l45/%C2%A78%5B%C2%A74StolenPvP%C2%A78%5D+%C2%A73RainbowFade.zip");
	}



	@EventHandler
	public void signClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Action a = e.getAction();
		Block b = e.getClickedBlock();

		if(a == Action.RIGHT_CLICK_BLOCK){
			if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN){
				Sign s = (Sign)b.getState();

				String s1 = s.getLine(1);
				String s2 = s.getLine(2);

				String[] ss = s.getLines();
				ss[2] = "§2"+Symboles.ARROW_LITTLE_RIGHT.get()+" "+ss[2]+" "+Symboles.ARROW_LITTLE_LEFT.get();


				if(s1.equalsIgnoreCase("Ressource pack")){
					if(packs.containsKey(s2) && packs.get(s2) != null){
						p.setResourcePack(packs.get(s2));
						p.sendMessage("§7[§2Packs§7] §2Installation du ressource pack:§a "+s2);
						p.sendSignChange(s.getLocation(), ss);

						for(int i = 0; i < packs.size(); i++){
							if(s.getLocation().add(0,0,i+1).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(0,0,i+1), ((Sign)s.getLocation().add(0,0,i+1).getBlock().getState()).getLines());
							if(s.getLocation().add(0,0,-(i+1)).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(0,0,-(i+1)), ((Sign)s.getLocation().add(0,0,-(i+1)).getBlock().getState()).getLines());

							if(s.getLocation().add(i+1,0,0).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(i+1,0,0), ((Sign)s.getLocation().add(i+1,0,0).getBlock().getState()).getLines());
							if(s.getLocation().add(-(i+1),0,0).getBlock().getState() instanceof Sign)p.sendSignChange(s.getLocation().add(-(i+1),0,0), ((Sign)s.getLocation().add(-(i+1),0,0).getBlock().getState()).getLines());
						}
					}else{
						p.sendMessage("§7[§2Packs§7] §2Impossible de trouver le pack:§a ");
					}
				}
			}
		}
	}
}
