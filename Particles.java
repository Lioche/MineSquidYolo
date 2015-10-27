package be.lioche.api.enums;

import java.util.Random;

import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Particles {

	public static void sendParticle(EnumParticle particle, Location loc, double x, double y, double z, int quantity, int speed){
		float xX = (float) (loc.getX()+x);
		float xY = (float) (loc.getY()+y);
		float xZ = (float) (loc.getZ()+z);

		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, xX, xY, xZ, 0, 0, 0, 0, quantity, speed);
		for(Player pl : Bukkit.getOnlinePlayers()){
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void createCircle(Location location, EnumParticle effect, int particle, float circleradius){
		Location location1 = location;
		Location location2 = location;
		Location location3 = location;
		int particles = particle;
		float radius = circleradius;
		for (int i = 0; i < particles; i++)
		{
			double angle = 6.283185307179586D * i / particles;
			double x = Math.cos(angle) * radius;
			double z = Math.sin(angle) * radius;
			location1.add(x, 0.0D, z);
			location2.add(x, -0.66D, z);
			location3.add(x, -1.33D, z);
			sendParticle(effect, location1, 0, 0, 0, particles, 50);
			sendParticle(effect, location2, 0, 0, 0, particles, 50);
			sendParticle(effect, location3, 0, 0, 0, particles, 50);
			location1.subtract(x, 0.0D, z);
			location2.subtract(x, -0.66D, z);
			location3.subtract(x, -1.33D, z);
		}
	}

	public static void createLine(Player player, int Longueur , EnumParticle effect){
		Location start = player.getEyeLocation();
		Location increase = start.getDirection().toLocation(start.getWorld());
		for (int counter = 0; counter < Longueur; counter++)
		{
			Location point = start.add(increase);
			sendParticle(effect, point, 0, 0, 0, 5, 0);
		}
	}

	public static void createHelix(Location location, double hauteur, int radius,double sepa, EnumParticle effect){
		Location loc = location;
		for (double y = 0.0D; y <= hauteur; y += sepa)
		{
			double x = radius * Math.cos(y);
			double z = radius * Math.sin(y);

			Location l = new Location(loc.getWorld(), (float)(loc.getX() + x), (float)(loc.getY() + y), (float)(loc.getZ() + z));
			sendParticle(effect, l, x, y, z, 1, 1);
		}
	}

	public static void createShield(Location location, EnumParticle effect, float radius, boolean sphere){
		for (int i = 0; i < 50; i++)
		{ 
			Random random = new Random(System.nanoTime());

			double x = random.nextDouble() * 2.0D - 1.0D;
			double y = random.nextDouble() * 2.0D - 1.0D;
			double z = random.nextDouble() * 2.0D - 1.0D;
			Vector vector = new Vector(x, y, z).normalize().multiply(radius);
			if (!sphere) {
				vector.setY(Math.abs(vector.getY()));
			}
			location.add(vector);
			sendParticle(effect, location, 0, 0, 0, 1, 5);
			location.subtract(vector);
		}
	}
}
