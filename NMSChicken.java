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
import org.bukkit.material.MonsterEggs;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import be.lioche.api.enums.Particles;
import be.lioche.api.enums.Symboles;
import be.lioche.api.main.Main;
import be.lioche.api.utils.Others;

public class NMSChicken extends EntityChicken implements Listener{

	public static List<ItemStack> items = new ArrayList<>();

	public static List<ItemStack> armors = new ArrayList<>();
	public static List<ItemStack> weapons = new ArrayList<>();
	public static List<ItemStack> potions = new ArrayList<>();
	public static List<ItemStack> noDouble = new ArrayList<>();

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

	@SuppressWarnings("deprecation")
	public void setupItems(){
		this.chickenInv = Bukkit.getServer().createInventory(owner, 27, Symboles.ARROW_LITTLE_RIGHT.get()+" Sponsor de "+this.owner.getName()+ " "+Symboles.ARROW_LITTLE_LEFT.get());

		Random r = new Random();
		List<Material> inInv = new ArrayList<>();

		int nbr = r.nextInt(5)+8;
		int pp = r.nextInt(2)+1;
		int slot;

		int isenchant;
		int whatenchant;
		int lvlenchant;
		int selected;

		ItemStack ih = new ItemStack(Material.IRON_HELMET);
		ItemStack ic = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack il = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack ib = new ItemStack(Material.IRON_BOOTS);

		armors.add(ih);
		armors.add(ic);
		armors.add(il);
		armors.add(ib);

		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack is = new ItemStack(Material.IRON_SWORD); 

		weapons.add(bow);
		weapons.add(is);

		Potion h2 = new Potion(PotionType.INSTANT_HEAL, 2);
		Potion po2 = new Potion(PotionType.POISON, 2, true);
		Potion f2 = new Potion(PotionType.STRENGTH, 2, true);
		Potion w2 = new Potion(PotionType.WEAKNESS, 2, true, true);
		Potion r1 = new Potion(PotionType.REGEN, 2, true);

		ItemStack p2 = h2.toItemStack(pp);
		ItemStack p5 = po2.toItemStack(pp);
		ItemStack p8 = f2.toItemStack(pp);
		ItemStack p6 = w2.toItemStack(pp);
		ItemStack p9 = r1.toItemStack(pp);

		potions.add(p2);
		potions.add(p5);
		potions.add(p6);
		potions.add(p8);
		potions.add(p9);
		
		MonsterEggs me = new MonsterEggs(383, (byte)50);
		ItemStack bread = new ItemStack(Material.BREAD, (nbr*(nbr/2)+((nbr*2)/3)/(pp*2))/pp);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, (nbr*(nbr/2)+((nbr*2)/3)/(pp*2))/pp);
		ItemStack iron = new ItemStack(Material.IRON_ORE, nbr+(nbr*2-pp*4)/4);
		ItemStack gold = new ItemStack(Material.GOLD_ORE, (nbr+(nbr*2-pp*4))/4);
		ItemStack coal = new ItemStack(Material.COAL, nbr+(nbr*2+2-pp*4+2)/2);
		ItemStack obsi = new ItemStack(Material.OBSIDIAN, (pp*3));
		ItemStack creep = me.toItemStack(pp*pp+3);
		ItemStack log = new ItemStack(Material.LOG, nbr*(nbr/2)+((nbr*2)/3)/(pp*2)/4);
		ItemStack exp = new ItemStack(Material.EXP_BOTTLE, nbr*(pp/2)+((nbr-pp*2)/3));
		ItemStack arrow = new ItemStack(Material.ARROW, nbr+(nbr/2)+5);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, pp-1);
		ItemStack ender = new ItemStack(Material.ENDER_PEARL, (pp-1)*(pp-1));
		
		items.add(bread);
		items.add(log);
		items.add(exp);
		items.add(steak);
		items.add(iron);
		items.add(creep);
		items.add(gold);
		items.add(coal);
		items.add(obsi);
		items.add(gapple);
		items.add(ender);
		
		noDouble.add(bread);
		noDouble.add(log);
		noDouble.add(exp);
		noDouble.add(steak);
		noDouble.add(iron);
		noDouble.add(gold);
		noDouble.add(coal);
		noDouble.add(gapple);
		noDouble.add(ender);

		items.addAll(weapons);
		items.addAll(armors);
		items.addAll(potions);

		for(int i = 0; i < nbr; i++){

			isenchant = r.nextInt(armors.size());
			whatenchant = r.nextInt(4)+1;
			lvlenchant = r.nextInt(5);

			selected = r.nextInt(items.size());
			slot = r.nextInt(27);

			ItemStack item = items.get(selected);

			if(!inInv.contains(item.getType()) && !this.chickenInv.contains(item)){
				if(armors.contains(item)){
					inInv.add(item.getType());
					if(isenchant > armors.size()/2 && isenchant < armors.size()){
						if(whatenchant == 1){
							Others.addEnchant(item, Enchantment.DURABILITY, lvlenchant);
						}else if(whatenchant == 2){
							Others.addEnchant(item, Enchantment.THORNS, lvlenchant);
						}else{
							Others.addEnchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, lvlenchant);
						}
					}
				}

				if(weapons.contains(item)){
					inInv.add(item.getType());
					if(isenchant > armors.size()/2 && isenchant < armors.size()){
						if(item.getType().equals(Material.BOW)){
							this.chickenInv.setItem(slot-1, arrow);
							inInv.add(arrow.getType());
							if(whatenchant == 1){
								Others.addEnchant(item, Enchantment.ARROW_FIRE, lvlenchant);
							}else if(whatenchant == 2){
								Others.addEnchant(item, Enchantment.ARROW_KNOCKBACK, lvlenchant);
							}else{
								Others.addEnchant(item, Enchantment.ARROW_DAMAGE, lvlenchant);
							}
						}else{
							if(whatenchant == 1){
								Others.addEnchant(item, Enchantment.FIRE_ASPECT, lvlenchant);
							}else if(whatenchant == 2){
								Others.addEnchant(item, Enchantment.KNOCKBACK, lvlenchant);
							}else{
								Others.addEnchant(item, Enchantment.DAMAGE_ALL, lvlenchant);
							}
						}
					}
				}
				
				if(noDouble.contains(item)){
					inInv.add(item.getType());
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
