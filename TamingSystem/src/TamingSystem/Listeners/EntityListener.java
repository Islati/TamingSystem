package TamingSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;


import SkillSystem.Skills;
import SkillSystem.Handlers.SkillHandlers;
import TamingSystem.TamingSystem;
import TamingSystem.Handlers.HorseModifier;
import TamingSystem.Handlers.Players.PlayerData;

public class EntityListener implements Listener
{
	private final TamingSystem plugin;
	
	public EntityListener(TamingSystem instance)
	{
		this.plugin = instance;
	}
	
	private boolean attackCheck(Entity Attacker, Entity Target)
	{
		if (Target == null)
		{
			return true;
		}
		
		if (TamingSystem.nPetHandler.isPet(Attacker))
		{
			Player Player = Bukkit.getPlayer(TamingSystem.nPetHandler.getOwner(Attacker));
			if ((Attacker instanceof Creeper) && Target != Player)
			{
				if (Target instanceof Player)
				{
					Player TargetPlayer = (Player)Target;
					if (new PlayerData().isSameFaction(Player, TargetPlayer))
					{
						return true;
					}
					return false;
				}
				return false; //Stop creeper from esploding
			}
			if (Player == Target)
			{
				if (Player.getWorld() == Attacker.getWorld())
				{
					if ((TamingSystem.nPetHandler.getPetFromID(Attacker.getEntityId()).isFollowing() == false) || (Attacker.getPassenger() != null) || (Attacker.getLocation().distance(Player.getLocation()) < 5.0D))
					{
						return true;
					}
					return false;
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTargetEvent(EntityTargetEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		Entity Entity = event.getEntity();
		if (TamingSystem.nPetHandler.isPet(Entity))
		{
			if ((Entity instanceof Monster))
			{
				event.setCancelled(true);
				return;
			}
		}
		Entity Target = event.getTarget();
		if (attackCheck(Entity, Target))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		Entity e = event.getEntity();
		Entity t = event.getTarget();
		if (attackCheck(e, t) || HorseModifier.isHorse((LivingEntity) e))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTameEvent(EntityTameEvent event)
	{
		LivingEntity Entity = event.getEntity();
		Player Player = (Player) event.getOwner();
		if (HorseModifier.isHorse(event.getEntity()))
		{
			SkillHandlers.HandleMobTaming(Player, Entity);
		}
		else
		{
			if (TamingSystem.nPetHandler.isPet(Entity))
			{
				Player.sendMessage(ChatColor.RED + "This " + Entity.getType().toString().toLowerCase() + " is already tamed!");
				event.setCancelled(true);
				return;
			}
			TamingSystem.nPetHandler.TamePet(Player, Entity);
		}
	}
	
	//@EventHandler(priority = EventPriority.NORMAL)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
		Entity e = event.getEntity();
		if ((e instanceof Creeper))
		{
			if (TamingSystem.nPetHandler.isPet(e))
			{
				event.setCancelled(true);
			}
		}
	}
	
	//@EventHandler
	public void stopDragonDamage(EntityExplodeEvent event)
	{
		//event.
		Entity e = event.getEntity();
		if (e != null)
		{
			if (TamingSystem.nPetHandler.isPet(e))
			{
				event.setCancelled(true);
			}
		}
	}
	
	public void onProjectileHitEvent(ProjectileHitEvent event)
	{
		Entity e = event.getEntity().getShooter();
		if ((e instanceof Entity))
		{
			if (TamingSystem.nPetHandler.isPet(e))
			{
				event.getEntity().remove();
			}
		}
	}
	
	public void onEntityCombustEvent(EntityCombustEvent event)
	{
		Entity e = event.getEntity();
		if ((event.getEntity() instanceof Creature))
		{
			if (!event.getEntity().getType().equals(EntityType.PLAYER))
			{
				if (TamingSystem.nPetHandler.isPet(e))
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityTeleportEvent(EntityTeleportEvent event)
	{
		Entity e = event.getEntity();
		if (event.getEntityType().isAlive())
		{
			if ((TamingSystem.nPetHandler.isPet(e)) && (e.getType() == EntityType.ENDERMAN))
			{
				Creature c = (Creature) e;
				if ((c.getPassenger() instanceof Entity))
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		Entity Entity = event.getEntity();
		Entity Damager = event.getDamager();
		
		if (Entity instanceof Player)
		{
			Player Player = (Player)Entity;
			if (Damager instanceof LivingEntity)
			{
				if (TamingSystem.nPetHandler.isPet(Damager))
				{
					String PetOwner = TamingSystem.nPetHandler.getPetFromID(Damager.getEntityId()).getOwner();
					if (PetOwner.equalsIgnoreCase(Player.getName()))
					{
						event.setCancelled(true);
						return;
					}
				}
				/*
				if (PetCreeper.getPlayerHandler(Player.getName()).hasPets())
				{
					for(TamedPet Pet : PetCreeper.getPlayerHandler(Player.getName()).getPets())
					{
						Entity PetEntity = PetCreeper.nPetHandler.getPetEntity(Pet.getEntityID());
						if (PetEntity != null && PetEntity.isValid())
						{
							((Creature)PetEntity).setTarget((LivingEntity)Damager);
						}
					}
				}
				*/
			}
			else if (Damager instanceof Arrow)
			{
				Arrow Arrow = (Arrow)Damager;
				if (TamingSystem.nPetHandler.isPet(Arrow.getShooter()))
				{
					String PetOwner = TamingSystem.nPetHandler.getPetFromID(((Arrow) Damager).getShooter().getEntityId()).getOwner();
					if (PetOwner.equalsIgnoreCase(Player.getName()))
					{
						Arrow.remove();
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		//
		else if (Entity.getType().isAlive())
		{
			if (TamingSystem.nPetHandler.isPet(Entity))
			{
				if ((Damager instanceof Player))
				{
					String PetOwner = TamingSystem.nPetHandler.getPetFromID(Entity.getEntityId()).getOwner();
					if (PetOwner.equalsIgnoreCase(((Player)Damager).getName()))
					{
						((Player)Damager).sendMessage(ChatColor.RED + "You've angered this mob, causing it to disband you");
						TamingSystem.nPetHandler.UntamePet(((Player)Damager), Entity);
						event.setCancelled(true);
						Entity.remove();
						return;
					}
				}
			}
			else if ((TamingSystem.nPetHandler.isPet(Damager)) && ((Entity instanceof LivingEntity)))
			{
				try
				{
					String PetOwner = TamingSystem.nPetHandler.getPetFromID(Damager.getEntityId()).getOwner();
					Skills.getPlayerHandler(PetOwner).getSkill("Taming").addExp(event.getDamage() * 2);
				}
				catch (Exception localException)
				{
				}
			}
		}
	}
	
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event)
	{
		Projectile p = event.getEntity();
		Entity e = event.getEntity();
		if (((e instanceof Fireball)) || ((e instanceof ThrownPotion)))
		{
			Entity sh = p.getShooter();
			if (TamingSystem.nPetHandler.isPet(sh))
			{
				event.setCancelled(true);
				p.remove();
			}
		}
		else if (TamingSystem.nPetHandler.isPet(e))
		{
			event.setCancelled(true);
			p.remove();
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity e = event.getEntity();
		if (TamingSystem.nPetHandler.isPet(e))
		{
			//PetCreeper.nPetHandler.UntamePet(Bukkit.getPlayer(PetCreeper.nPetHandler.getOwner(e)), e); //TODO BUGTEST
			TamingSystem.nPetHandler.getPetFromID(e.getEntityId()).setKilled(true);
			Bukkit.getLogger().info("[Taming System] Market an entity for deletion");
			//PetCreeper.nPetHandler.PetDied(event.getEntity());
			event.getDrops().clear();
		}
	}
	
	//@EventHandler(priority = EventPriority.NORMAL)
	/*
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player Player = event.getEntity();
		EntityDamageEvent DamagedEvent = Player.getLastDamageCause();
		if ((DamagedEvent instanceof EntityDamageByEntityEvent))
		{
			Entity Killer = ((EntityDamageByEntityEvent) DamagedEvent).getDamager();
			if (PetCreeper.PetHandler.isPet(Killer))
			{
				Player p = PetCreeper.PetHandler.getMasterOf(Killer);
				SkillHandlers.HandleMobEXP(p, 30);
			}
		}
	}
	*/
}