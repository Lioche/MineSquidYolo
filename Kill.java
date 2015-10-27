package be.lioche.compact.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import be.lioche.api.enums.Symboles;
import be.lioche.api.packet.Holo;
import be.lioche.compact.main.Main;

public class Kill implements Listener{

	public static HashMap<UUID, Kill> registeredCombo = new HashMap<>();

	public int count;
	public Player p;
	public UUID uuid;
	public Player t;

	public Kill(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	public Kill(Player pl, Player t){
		this.p = pl;
		this.count = 0;
		this.uuid = pl.getUniqueId();
		this.t = t;

		registeredCombo.put(this.uuid, this);
	}

	public static Kill getPlayerKill(Player p, UUID uuid, Player t){
		Kill kill;
		if(!registeredCombo.containsKey(p.getUniqueId())){
			kill = new Kill(p, t);
		}else{
			kill = registeredCombo.get(uuid);
		}
		return kill;
	}


	@EventHandler
	public void onKill(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player)e.getEntity();
			Player k = (Player)e.getDamager();
			double dmg = e.getDamage();
			if(p.getHealth()-dmg <= 0){
				p.sendMessage(Main.prefix+"Il restait "+ k.getHealth()/2 + " " + Symboles.HEARTH.get()+" a "+k.getName()+".");
			}
		}
	}

	@EventHandler
	public void combo(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player)e.getEntity();
			Player k = (Player)e.getDamager();

			Kill killP = getPlayerKill(p, p.getUniqueId(), k);
			Kill killK = getPlayerKill(k, k.getUniqueId(), p);

			if(killK.t.equals(p)){

				killK.count = killK.count + 1;
				killP.count = 0;

				if(killK.count  > 1){
					Holo h = new Holo();
					if(killK.count >= 10){
						h.create(p.getLocation().add(0,1,0), "§6§lMEGA COMBOOO §e(§6§l"+killK.count+"§e)");
						h.showToAll();
						Holo.followPlayer(p, h, 1L);
					}else{
						h.create(p.getLocation().add(0,1,0), "§c§lCombo x"+killK.count);
						h.showToAll();
						Holo.followPlayer(p, h, 1L);
					}
				}

			}else{
				killK.t = p;
				killK.count = 1;
				killP.count = 0;
			}
		}
	}
}
