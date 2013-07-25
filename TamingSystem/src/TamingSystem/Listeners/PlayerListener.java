package TamingSystem.Listeners;

import net.minecraft.server.v1_6_R2.EntityInsentient;
import net.minecraft.server.v1_6_R2.Navigation;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import TamingSystem.TamingSystem;
import TamingSystem.Handlers.Cooldown;
import TamingSystem.Handlers.Pets.TamedPet;
import TamingSystem.Handlers.Players.PlayerHandler;




public class PlayerListener implements Listener
{
	private final TamingSystem plugin;
	private Cooldown ChatCooldown = new Cooldown(2);
	
	public PlayerListener(TamingSystem instance)
	{
		this.plugin = instance;
	}
	
	public void delayedPetSpawnTask(final String Player)
	{
		plugin.getServer().getScheduler().runTaskLater(this.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				TamingSystem.getPlayerHandler(Player).SpawnPets();
			}
		}, 20L);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		TamingSystem.addPlayerHandler(event.getPlayer().getName());
		delayedPetSpawnTask(event.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent Event)
	{
		TamingSystem.getPlayerHandler(Event.getPlayer().getName()).SpawnPets();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent Event)
	{
		TamingSystem.getPlayerHandler(Event.getEntity().getName()).DespawnPets();
		if (TamingSystem.getPlayerHandler(Event.getEntity().getName()).hasPets())
		{
			TamingSystem.getPlayerHandler(Event.getEntity().getName()).DespawnPets();
			/*
			for(TamedPet Ent : TamingSystem.getPlayerHandler(Event.getEntity().getName()).getPets())
			{
				Ent.setFollowing(false);
			}
			*/
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent Event)
	{
		TamingSystem.getPlayerHandler(Event.getPlayer().getName()).DespawnPets();
		TamingSystem.getPlayerHandler(Event.getPlayer().getName()).SavePets();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent Event)
	{
		TamingSystem.getPlayerHandler(Event.getPlayer().getName()).DespawnPets();
		TamingSystem.getPlayerHandler(Event.getPlayer().getName()).SavePets();
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if ((event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) && (event.getEntity().isInsideVehicle()))
		{
			Entity vehicle = event.getEntity().getVehicle();
			if (TamingSystem.nPetHandler.isPet(vehicle))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE && !event.getPlayer().isOp())
		{
			event.setCancelled(true);
			return;
		}
		Entity Entity = event.getRightClicked();
		Player Player = event.getPlayer();
		EntityType EntType = Entity.getType();
		if ((!(Entity instanceof Player)) && (!Entity.hasMetadata("NPC")))
		{
			if (EntType.isAlive())
			{
				if (TamingSystem.nPetHandler.isPet(Entity))
				{
					//Bukkit.getLogger().info("Entity is pet");
					if (EntType == EntityType.PIG)
					{
						if (((Pig) Entity).hasSaddle())
						{
							if (Player.isSneaking())
							{
								event.setCancelled(true);
							}
							else
							{
								return;
							}
						}
					}
					String Owner = TamingSystem.nPetHandler.getOwner(Entity); //TODO See why pets say "this entity belongs to blank
					//Bukkit.getLogger().info("Owner is gotten");
					if (Owner.equalsIgnoreCase(Player.getName()))
					{
						Entity Passenger = Entity.getPassenger();
						if (Passenger != null)
						{
							if (EntType != EntityType.PIG && (Passenger == Player))
							{
								Entity.eject();
								return;
							}
						}
						else if ((Player.getItemInHand().getType() == Material.SADDLE) && Passenger == null)
						{
							if (EntType != EntityType.PIG)
							{
								Entity.setPassenger(Player);
							}
							else
							{
								return;
							}
						}
						else if (Entity instanceof Wolf)
						{
							((Wolf)Entity).setSitting(!((Wolf)Entity).isSitting());
						}
						else if (Entity instanceof Ocelot)
						{
							((Ocelot)Entity).setSitting(!((Ocelot)Entity).isSitting());
						}
						if (!this.ChatCooldown.IsOnCooldown(Player.getName()))
						{
							TamedPet Pet = TamingSystem.nPetHandler.getPetFromID(Entity.getEntityId());
							Pet.setFollowing(!Pet.isFollowing());
							Player.sendMessage(ChatColor.YELLOW + "Your " + EntType.name().toLowerCase() + " is " + ((Pet.isFollowing() == true) ? "now following you!" : "no longer following you!"));
							ChatCooldown.SetOnCooldown(Player.getName());
						}
					}
					else
					{
						Player.sendMessage(ChatColor.YELLOW + "That pet belongs to " + Owner);//p, ChatColor.YELLOW + "That " + e.getType().getName() + " belongs to " + master.getDisplayName() + ".");
					}
				}
				else
				{
					//Bukkit.getLogger().info("Tame pet call via interect entity");
					TamingSystem.nPetHandler.TamePet(Player, Entity);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (event.getPlayer().isInsideVehicle() || event.getPlayer().hasMetadata("NPC"))
		{
			return;
		}
		//PetCreeper.PetHandler.teleportPetsOf(event.getPlayer(), event.getTo(), false, true);
		PlayerHandler Handler = TamingSystem.getPlayerHandler(event.getPlayer().getName());
		if (Handler != null)
		{
			if (Handler.hasPets())
			{
				for(TamedPet Pet : Handler.getPets())
				{
					if (Pet.isFollowing())
					{
						try
						{
							TamingSystem.nPetHandler.getPetEntity(Pet.getEntityID()).teleport(event.getTo());
						}
						catch (NullPointerException Ex)
						{
							//
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player p = event.getPlayer();
		Action action = event.getAction();
		if ((action == Action.LEFT_CLICK_AIR) && (p.isInsideVehicle()))
		{
			Entity e = p.getVehicle();
			
			if (e.getType().isAlive() && TamingSystem.nPetHandler.isPet(e))
			{
				Block targetBlock = p.getTargetBlock(null, 100);
				Location blockLoc = targetBlock.getLocation();
				if (e instanceof LivingEntity)
				{
					CraftLivingEntity cLivEnt = (CraftLivingEntity)((LivingEntity)e);
					if (cLivEnt.getHandle() instanceof EntityInsentient)
					{
						Navigation n = ((EntityInsentient) cLivEnt.getHandle()).getNavigation();
						//Navigation Nav = ((CraftEntity) e).getHandle().move(p.getLocation().getX() + 2.0D, p.getLocation().getY(), p.getLocation().getZ() + 2.0D);
						n.a(blockLoc.getX(), blockLoc.getY(), blockLoc.getZ(), 1.1F);
					}
				}
			}
		}
	}
}

/* Location:           C:\Users\Brandon\Desktop\PetCreeper.jar
 * Qualified Name:     com.cnaude.petcreeper.Listeners.PetPlayerListener
 * JD-Core Version:    0.6.2
 */