package be.lioche.compact.obj;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import net.minecraft.server.v1_8_R2.Entity;

public enum EntityTypes
{
	SPONSOR_CHICKEN("Chicken", 63, NMSChicken.class);
	// Changer chiffre = changer apparence

	private EntityTypes(String name, int id, Class<? extends Entity> custom)
	{
		addToMaps(custom, name, id);
	}

	public static void spawnEntity(Entity entity, Location loc)
	{
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
	}

	@SuppressWarnings("unchecked")
	private static void addToMaps(Class<? extends Entity> clazz, String name, int id)
	{
		((Map<String, Class<? extends Entity>>)NMSChicken.getPrivateField("c", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(name, clazz);
		((Map<Class<? extends Entity>, String>)NMSChicken.getPrivateField("d", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, name);
		((Map<Class<? extends Entity>, Integer>)NMSChicken.getPrivateField("f", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
	}
}
