package be.lioche.compact.obj;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityChicken;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R2.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import be.lioche.api.enums.Particles;
import be.lioche.api.enums.Symboles;

public class NMSChicken extends EntityChicken implements Listener{

	public static List<ItemStack> items = new ArrayList<>();

	Player owner;
	Location loc;

	NMSChicken chick;
	Inventory chickenInv;
	UUID chickUUID;

	public NMSChicken(World world, Player player) {
		super(((CraftWorld) world).getHandle());

		List<?> goalB = (List<?>)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		List<?> goalC = (List<?>)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		List<?> targetB = (List<?>)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		List<?> targetC = (List<?>)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();


		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this,EntityHuman.class, 6.0F));

		this.owner = player;
		setup();
	}

	public void setup(){
		this.chickenInv = Bukkit.getServer().createInventory(owner, 27, Symboles.ARROW_LITTLE_RIGHT.get()+" Sponsor de "+this.owner.getName()+ " "+Symboles.ARROW_LITTLE_LEFT.get());
		this.chickUUID = this.owner.getUniqueId();
		this.chick = this;
		this.loc = this.owner.getLocation().add(1, 20, 0);

		this.setCustomName(Symboles.ARROW_LITTLE_RIGHT.get()+" Sponsor de "+this.owner.getName()+ " "+Symboles.ARROW_LITTLE_LEFT.get());
		this.uniqueID = this.owner.getUniqueId();
		this.setCustomNameVisible(true);
		setupItems();
	}

	private void setupItems(){
		Random r = new Random();
		int nbr = r.nextInt(5)+3;
		int ri;
		int po;
		
		ItemStack i1 = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack i2 = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack i3 = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack i4 = new ItemStack(Material.DIAMOND_BOOTS);
		
		ItemStack i5 = new ItemStack(Material.BOW);
		ItemStack i6 = new ItemStack(Material.BREAD, nbr*nbr);
		ItemStack i7 = new ItemStack(Material.LOG, nbr*nbr);
		ItemStack i8 = new ItemStack(Material.EXP_BOTTLE, nbr*nbr);
		
		
		items.add(i1);
		items.add(i2);
		items.add(i3);
		items.add(i4);
		items.add(i5);
		items.add(i6);
		items.add(i7);
		items.add(i8);

		for(int i = 0; i < nbr; i++){
			ri = r.nextInt(items.size());
			po = r.nextInt(27);
			if(!this.chickenInv.contains(items.get(ri))){
				this.chickenInv.setItem(po, items.get(ri));
			}
		}
	}

	public static void spawnEntity(Entity entity, Location loc)
	{
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
		for(double t = 0; t < 10 * Math.PI; t += 0.39){
			Particles.sendParticle(EnumParticle.FIREWORKS_SPARK, loc, Math.sin(t), t, Math.cos(t), 1, 20);
		}
	}

	public String getUUID(){
		return this.chickUUID.toString();
	}
	public Inventory getInventory(){
		return this.chickenInv;
	}
	public Player getOwner(){
		return this.owner;
	}
	public Location getLocation(){
		return this.loc;
	}

	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object)
	{
		Field field;
		Object o = null;
		try
		{
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			o = field.get(object);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return o;
	}
}
