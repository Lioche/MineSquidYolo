package be.lioche.api.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R2.EntityArmorStand;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import be.lioche.api.main.Main;

public class Holo {

	public static List<EntityArmorStand> liste = new ArrayList<>();

	public static boolean showDamages = false;
	public static boolean showRegen = false;

	public EntityArmorStand stand;
	public String name;
	public Integer ID;

	public void create(Location loc, String texte){
		WorldServer w = ((CraftWorld) loc.getWorld()).getHandle();

		this.stand = new EntityArmorStand(w);
		this.name = texte;
		this.ID = this.stand.getId();

		this.stand.setLocation(loc.getX()+0.5, loc.getY()-1.5, loc.getZ()+0.5, 0, 0);
		this.stand.setInvisible(true);
		this.stand.setCustomName(texte);
		this.stand.setCustomNameVisible(true);
		this.stand.setGravity(false);

		liste.add(stand);
	}

	public void rename(String name){
		this.stand.setCustomName(name);
		this.name = name;
	}

	public void remove(){
		this.stand.die();
	}

	public void removeAll(){
		for(EntityArmorStand s : liste){

			s.die();
		}
	}

	public void tpToPlayer(Player player, double addx, double addy, double addz){
		Location location = player.getLocation();
		location.setX(location.getX()+addx);
		location.setY(location.getY()+addy);
		location.setZ(location.getZ()+addz);

		this.stand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
		for(Player all : Bukkit.getOnlinePlayers()){
			PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(this.stand);
			EntityPlayer nmsp = ((CraftPlayer)all).getHandle();
			nmsp.playerConnection.sendPacket(teleport);
		}
	}

	public static void followPlayer(final Player player, final Holo holo, long secx2){
		final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance,new Runnable() {
			public void run() {
				holo.tpToPlayer(player, 0, 0.1, 0);
			}
		}, 0, 2L);

		Bukkit.getScheduler().runTaskLater(Main.instance,new Runnable() {
			public void run() {
				holo.destroyToAll();
				Bukkit.getScheduler().cancelTask(id);
			}
		}, 10L*secx2);
	}

	public void destroy(Player player){
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(this.stand.getId());
		EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
		nmsp.playerConnection.sendPacket(destroy);
	}

	public void destroyAll(Player player){
		for(EntityArmorStand stand : liste){
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(stand.getId());
			EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
			nmsp.playerConnection.sendPacket(destroy);
		}
	}

	public void destroyToAll(){
		for(Player player : Bukkit.getOnlinePlayers()) {
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(stand.getId());
			EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
			nmsp.playerConnection.sendPacket(destroy);
		}
	}

	public void destroyAllToAll(){
		for(EntityArmorStand stand : liste){
			for(Player player : Bukkit.getOnlinePlayers()) {
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(stand.getId());
				EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
				nmsp.playerConnection.sendPacket(destroy);
			}
		}
	}

	public void show(Player player){
		PacketPlayOutSpawnEntityLiving standP = new PacketPlayOutSpawnEntityLiving(this.stand);

		EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
		nmsp.playerConnection.sendPacket(standP);
	}

	public void showToAll(){
		for(Player player : Bukkit.getOnlinePlayers()) {
			PacketPlayOutSpawnEntityLiving standP = new PacketPlayOutSpawnEntityLiving(this.stand);

			EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
			nmsp.playerConnection.sendPacket(standP);
		}
	}

	public void showAll(Player player){
		for(EntityArmorStand stand : liste){
			PacketPlayOutSpawnEntityLiving standP = new PacketPlayOutSpawnEntityLiving(stand);

			EntityPlayer nmsp = ((CraftPlayer)player).getHandle();
			nmsp.playerConnection.sendPacket(standP);
		}
	}

	public void showAllToAll(){
		for(EntityArmorStand stand : liste){
			for(Player pl : Bukkit.getOnlinePlayers()){
				PacketPlayOutSpawnEntityLiving standP = new PacketPlayOutSpawnEntityLiving(stand);

				EntityPlayer nmsp = ((CraftPlayer)pl).getHandle();
				nmsp.playerConnection.sendPacket(standP);
			}
		}
	}
}
