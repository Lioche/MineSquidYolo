package be.lioche.api.nms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import be.lioche.api.enums.Particles;
import be.lioche.api.enums.Symboles;
import be.lioche.api.main.Main;
import be.lioche.api.utils.Others;

public class NMSChicken extends EntityChicken implements Listener{

	public static List<ItemStack> items = new ArrayList<>();

	public static List<ItemStack> armors = new ArrayList<>();
	public static List<ItemStack> weapons = new ArrayList<>();

	Player owner;
	Location loc;

	NMSChicken chick;
	Inventory chickenInv;

	public NMSChicken(World world, Player player) {
		super(((CraftWorld) world).getHandle());

		List<?> goalB = (List<?>)EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		List<?> goalC = (List<?>)EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		List<?> targetB = (List<?>)EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		List<?> targetC = (List<?>)EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();


		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this,EntityHuman.class, 6.0F));

		this.owner = player;
		this.chick = this;
		this.uniqueID = this.owner.getUniqueId();
		this.loc = this.owner.getLocation().add(0, 20, 0);
	}

	@Override
	public void makeSound(String s, float f, float f1) {
		if (!this.R()) {
			this.world.makeSound(this, "random.levelup", 1, 1);
			Particles.sendParticle(EnumParticle.HEART, new Location(this.world.getWorld(), this.locX, this.locY+1, this.locZ), 0, 0, 0, 1, 10);
		}
	}

	public void setup(){
		this.setCustomName("§c"+Symboles.ARROW_LITTLE_RIGHT.get()+"§f Sponsor de "+this.owner.getName()+ "§c "+Symboles.ARROW_LITTLE_LEFT.get());
		this.uniqueID = this.owner.getUniqueId();
		this.expToDrop = 0;
		this.fireProof = true;
		this.noclip = true;
		this.setCustomNameVisible(true);
		this.setAbsorptionHearts(999);
		this.dropDeathLoot(false, 0);
		this.dropEquipment(false, 0);
	}

	public void setupItems(){
		this.chickenInv = Bukkit.getServer().createInventory(owner, 27, Symboles.ARROW_LITTLE_RIGHT.get()+" Sponsor de "+this.owner.getName()+ " "+Symboles.ARROW_LITTLE_LEFT.get());

		Random r = new Random();
		int nbr = r.nextInt(3)+5;
		int slot;

		int isenchant;
		int whatenchant;
		int lvlenchant;
		int selected;

		ItemStack dh = new ItemStack(Material.DIAMOND_HELMET);
		ItemStack dc = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack dl = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack db = new ItemStack(Material.DIAMOND_BOOTS);
		
		armors.add(dh);
		armors.add(dc);
		armors.add(dl);
		armors.add(db);
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack ds = new ItemStack(Material.DIAMOND_SWORD); 
		
		weapons.add(bow);
		weapons.add(ds);
		
		ItemStack bread = new ItemStack(Material.BREAD, nbr*(nbr/2)+((nbr*2)/3));
		ItemStack log = new ItemStack(Material.LOG, nbr*(nbr/2)+((nbr*2)/3));
		ItemStack exp = new ItemStack(Material.EXP_BOTTLE, nbr*(nbr/2)+((nbr*2)/3));
		ItemStack arrow = new ItemStack(Material.ARROW, nbr+(nbr/2)+5);

		items.add(bread);
		items.add(log);
		items.add(exp);
		items.add(arrow);
		
		items.addAll(weapons);
		items.addAll(armors);
		
		for(int i = 0; i < nbr; i++){

			isenchant = r.nextInt(armors.size());
			whatenchant = r.nextInt(4)+1;
			lvlenchant = r.nextInt(3)+1;

			selected = r.nextInt(items.size());
			slot = r.nextInt(27);

			ItemStack item = items.get(selected);

			if(!this.chickenInv.contains(item)){
				if(armors.contains(item)){
					if(isenchant == armors.size()/2){
						Others.addEnchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}

				if(weapons.contains(item)){
					if(isenchant == weapons.size()/2){
						if(item.getType().equals(Material.BOW)){
							if(whatenchant == 1){
								Others.addEnchant(item, Enchantment.ARROW_FIRE, lvlenchant);
							}else if(whatenchant == 2){
								Others.addEnchant(item, Enchantment.ARROW_KNOCKBACK, lvlenchant);
							}else{
								Others.addEnchant(item, Enchantment.ARROW_DAMAGE, lvlenchant);
							}
						}else{
							Others.addEnchant(item, Enchantment.DAMAGE_ALL, lvlenchant);
						}
					}
				}

				this.chickenInv.setItem(slot, item);
			}


		}
	}

	public static void spawnEntity(final Entity entity, Location loc)	{
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), -loc.getYaw(), -loc.getPitch());
		((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);

		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			public void run() {
				entity.die();
			}
		}, 20*60L);
	}
	public Inventory getChickInventory(){
		return this.chickenInv;
	}
	public Player getOwner(){
		return this.owner;
	}
	public Location getLocation(){
		return this.loc;
	}
	public void setInventory(Inventory inv){
		this.chickenInv = inv;
	}

	public void setNoClip(final boolean bool, long time){
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			public void run() {
				noclip = bool;
			}
		}, 10L*time);
	}
}
