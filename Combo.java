@EventHandler(priority=EventPriority.LOW)
	public void comboCheck(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){

			Player p = (Player)e.getEntity();
			MPlayer mp = MPlayer.get(p);

			Player k = (Player)e.getDamager();
			MPlayer mk = MPlayer.get(k);

			Faction fp = mp.getFaction();
			Faction fk = mk.getFaction();
			Faction lfp = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation())); 
			Faction lfk = BoardColl.get().getFactionAt(PS.valueOf(k.getLocation())); 

			boolean hasFacP = true;
			boolean hasFacK = true;

			if(fp.isNone())hasFacP = false;
			if(fk.isNone())hasFacK = false;

			Rel rpk = fp.getRelationWish(fk);

			/* Intégration du plugin Faction, pour vérifier si un combo doit être appliqué ou non. 14-11-15 

			* Si p et k pas dans la zone du spawn. -> NEXT
			* Si p et k n'ont pas de fac -> OK

			* Si p et k ont fac -> NEXT
			* Si p et k pas même fac -> NEXT 
			* Si p et k pas ally -> NEXT
			* Si k est pas dans zone fac p et p est pas dans sa zone -> OK
			
			* Si k est zone fac p et p dans sa zone -> NEXT
			* Si k et p enemy -> OK
			*/

			if(!Main.isOnSpawn(p) && !Main.isOnSpawn(k)){
//				Spawn.noEntryCombat(p);
//				Spawn.noEntryCombat(k);
				if(!hasFacP && !hasFacK){
					comboLaunch(p, k);
				}else{
					if(fp != fk){
						if(rpk != Rel.ALLY){
							if(lfk != fp && lfp != fp){
								comboLaunch(p, k);
							}else{
								if(rpk == Rel.ENEMY){
									comboLaunch(p, k);
								}
							}
						}
					}
				}
			}
		}
	}

	private void comboLaunch(Player p, Player k){
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
