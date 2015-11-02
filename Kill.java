package be.lioche.compact.events;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_8_R2.EnumParticle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import be.lioche.api.enums.Particles;
import be.lioche.api.enums.Symboles;
import be.lioche.api.packet.Holo;
import be.lioche.compact.main.Main;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

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
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(e.getDamager() instanceof Player){	
				Player k = (Player)e.getDamager();
				p.setMaximumNoDamageTicks(Main.instance.getConfig().getInt("PvP-Ticks"));
				if(p.getHealth() <= 0.0){
					p.sendMessage(Main.prefix+"Il restait "+ be.lioche.api.main.Main.df.format(k.getHealth()/2) + " " + Symboles.HEARTH.get()+" a "+k.getName()+".");
				}
			}else{
				p.setMaximumNoDamageTicks(10);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(e.getCause() != DamageCause.ENTITY_ATTACK){
				p.setMaximumNoDamageTicks(20);
			}
		}
	}

	@EventHandler
	public void creeper(EntityDeathEvent e){
		if(e.getEntity() instanceof Creeper){
			Creeper c = (Creeper)e.getEntity();
			e.getDrops().clear();
			ItemStack i = new ItemStack(Material.MONSTER_EGG, 1, (byte)50);
			double dropTnT = 100 - Main.instance.getConfig().getInt("Drops.tnt");
			double dropCreep = 100 - Main.instance.getConfig().getInt("Drops.creepers");

			if(Math.random() * 100 >= dropCreep){
				c.getWorld().dropItemNaturally(c.getLocation(), i);
			}

			if(Math.random() * 100 >= dropTnT){
				c.getWorld().dropItemNaturally(c.getLocation(), new ItemStack(Material.TNT));
			}
		}
	}

	@EventHandler(priority=EventPriority.LOW)
	public void combo(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){



			Player p = (Player)e.getEntity();
			MPlayer mp = MPlayer.get(p);

			Player k = (Player)e.getDamager();
			MPlayer mk = MPlayer.get(k);

			Faction fp = mp.getFaction();
			Faction fk = mk.getFaction(); 

			Rel rpk = fp.getRelationWish(fk); 

			Faction lfp = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation())); 
			Faction lfk = BoardColl.get().getFactionAt(PS.valueOf(k.getLocation())); 
			
			/* Intégration du plugin Faction, pour vérifier si un combo doit être appliqué ou non. 01-11-15 */

			if(!Main.isOnSpawn(p) && !Main.isOnSpawn(k) && e.getDamage() > 0 && !e.isCancelled()){
				if(fp != fk && rpk != Rel.ALLY){ 
					if(lfk == fk && lfp != fp || rpk == Rel.ENEMY){
						
						Kill killP = getPlayerKill(p, p.getUniqueId(), k);
						Kill killK = getPlayerKill(k, k.getUniqueId(), p);

						if(killK.t.equals(p)){

							killK.count = killK.count + 1;
							killP.count = 0;

							if(killK.count  > 1){
								Holo h = new Holo();
								if(killK.count >= 10){
									if(p.getFireTicks() != 0){
										p.setFireTicks(killK.count);
									}
									for(double t = 0; t < 3.5 * Math.PI; t += 0.39){
										Particles.sendParticle(EnumParticle.REDSTONE, k.getLocation(), Math.cos(t)/1.5, t/5, Math.sin(t)/1.5, 2, 5);
										Particles.sendParticle(EnumParticle.SNOW_SHOVEL, k.getLocation(), Math.sin(t)/1.5, t/5, Math.cos(t)/1.5, 10, 25);
									}

									h.create(p.getLocation().add(0,1,0), "§6§lMEGA COMBOOO §e(§6§l"+killK.count+"§e)");
									h.showToAll();
									Holo.followPlayer(p, h, 1L);
									k.getWorld().playSound(k.getLocation(), Sound.FIZZ, 1, 3);
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
		}
	}
}
