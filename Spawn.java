package be.lioche.compact.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R2.EnumParticle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import be.lioche.api.enums.Particles;
import be.lioche.compact.main.Main;

public class Spawn implements Listener{

	public static final List<UUID> canNotBack = new ArrayList<>();

	public Spawn(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public void buildProtect(BlockPlaceEvent e){
		if(Main.isBuildProtected(e.getBlockPlaced().getLocation())){
			if(!e.getPlayer().isOp() || !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				e.setBuild(false);
				e.setCancelled(true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void noEntryCombat(final Player p){
		if(!canNotBack.contains(p)){
			canNotBack.add(p.getUniqueId());

			checkCombat(p);

			for(Location loc : barriers){
				p.sendBlockChange(loc, 166, (byte) 0);
				Particles.sendParticle(p, EnumParticle.BARRIER, loc, 0, 0, 0, 1, 1);
			}
			
			Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
				@Override
				public void run() {
					canNotBack.remove(p.getUniqueId());
					for(Location loc : barriers){
						p.sendBlockChange(loc, 0, (byte) 0);
						Particles.sendParticle(p, EnumParticle.VILLAGER_HAPPY, loc, 0, 0, 0, 1, 1);
					}
				}
			}, 20L*20);
		}
	}

	public static List<Location> barriers = new ArrayList<>();

	public static void setBarriers() {
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2689));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2688));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2687));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2686));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2685));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2684));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2683));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2682));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 77, -2681));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -614, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -615, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -616, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -617, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -618, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -619, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -620, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -621, 77, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -622, 77, -2638));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2689));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2688));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2687));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2686));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2685));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2684));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2683));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2682));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 77, -2681));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -614 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -615 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -616 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -617 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -618 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -619 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -620 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -621 , 77, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -622 , 77, -2732));


		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2689));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2688));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2687));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2686));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2685));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2684));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2683));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2682));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -665, 79, -2681));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -614, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -615, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -616, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -617, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -618, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -619, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -620, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -621, 79, -2638));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -622, 79, -2638));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2689));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2688));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2687));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2686));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2685));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2684));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2683));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2682));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -571, 79, -2681));

		barriers.add(new Location(Bukkit.getWorld("Spawn"), -614 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -615 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -616 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -617 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -618 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -619 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -620 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -621 , 79, -2732));
		barriers.add(new Location(Bukkit.getWorld("Spawn"), -622 , 79, -2732));
	}

	private static void checkCombat(final Player p) {
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			@Override
			public void run() {
				if(canNotBack.contains(p.getUniqueId())){
					checkCombat(p);
				}
			}
		}, 5L);
	}
	
	@EventHandler
	public void noClic(PlayerInteractEvent e){
		Action a = e.getAction();
		
		if(a == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.BARRIER){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void spawnMobs(EntitySpawnEvent e){
		if(e.getEntity() instanceof Monster){
			Monster m = (Monster)e.getEntity();
			if(m.getCustomName() == null){
				if(Main.isBuildProtected(e.getLocation())){
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void doubleDrop(BlockBreakEvent e){
		Material i = e.getBlock().getType();

		if(Main.isBuildProtected(e.getBlock().getLocation())){
			if(!e.getPlayer().isOp() || !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				e.setCancelled(true);
				return;
			}
		}

		if(e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0){
			e.setExpToDrop(e.getExpToDrop()*2);
			if(i == Material.IRON_ORE){
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.IRON_INGOT, 2));
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}

			if(i == Material.DIAMOND_ORE){
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.DIAMOND, 2));
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}

			if(i == Material.EMERALD_ORE){
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.EMERALD, 2));
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}

			if(i == Material.GOLD_ORE){
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, 2));
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}

			if(i == Material.COAL_ORE){
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.COAL));
				e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.TORCH,2));
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
		}
	}
}
