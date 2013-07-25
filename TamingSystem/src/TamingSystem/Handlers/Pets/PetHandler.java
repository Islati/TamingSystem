package TamingSystem.Handlers.Pets;

import java.util.List;

import net.minecraft.server.v1_6_R2.EntityInsentient;
import net.minecraft.server.v1_6_R2.Navigation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;

import SkillSystem.Handlers.SkillHandlers;
import TamingSystem.Configuration;
import TamingSystem.TamingSystem;
import TamingSystem.Handlers.HorseModifier;
import TamingSystem.Handlers.Pets.TamedPet;
import TamingSystem.Handlers.Players.ItemHandler;
import TamingSystem.Handlers.Players.PlayerData;
import TamingSystem.Handlers.Players.PlayerHandler;


public class PetHandler
{
	
	public PetHandler()
	{
		
	}
	
	public TamedPet getPetFromID(Integer EntityID)
	{
		for(PlayerHandler Handler : TamingSystem.PlayerHandlers.values())
		{
			if (Handler.hasPets())
			{
				for(TamedPet Pet : Handler.getPets())
				{
					if (Pet.getEntityID() == EntityID)
					{
						return Pet;
					}
				}
			}
		}
		return null;
	}
	
	public Entity getPetEntity(Integer EntityID)
	{
		return TamingSystem.PetEntities.get(EntityID);
	}
	
	public void PetDied(Entity Entity)
	{
		TamedPet Pet = getPetFromID(Entity.getEntityId());
		if (Pet != null)
		{
			String Owner = Pet.getOwner();
			Bukkit.getLogger().info("[TamingSystem] Deleted pet file @ Location " + Pet.getFileLocation());
			TamingSystem.getPlayerHandler(Owner).deletePet(Pet);
		}
	}
	
	public void PetDied(TamedPet Pet)
	{
		if (Pet != null)
		{
			String Owner = Pet.getOwner();
			Bukkit.getLogger().info("[TamingSystem] Deleted pet file @ Location " + Pet.getFileLocation());
			TamingSystem.getPlayerHandler(Owner).deletePet(Pet);
		}
	}
	
	public String getOwner(Entity Entity)
	{
		TamedPet Pet = getPetFromID(Entity.getEntityId());
		if (Pet != null)
		{
			return Pet.getOwner();
		}
		return null;
	}
	
	public boolean TamePet(Player Player, Entity Taming)
	{
		if (Taming instanceof LivingEntity)
		{
			LivingEntity Entity = (LivingEntity)Taming;
			if (!(Entity instanceof Player) && !(Entity.hasMetadata("NPC")))
			{
				EntityType EntType = Entity.getType();
				if (Player.getItemInHand() != null && Player.getItemInHand().getAmount() > 0)
				{
					ItemStack TamingItem = Player.getItemInHand();
					if (TamingItem.getType() == Configuration.getBait(EntType).getType())
					{
						if (SkillHandlers.canTame(EntType, Player))
						{
							TamedPet TamedEntity = new TamedPet(Entity,Player.getName());
							Entity.setFireTicks(0);
							TamingSystem.getPlayerHandler(Player.getName()).addPetAndSaveData(TamedEntity);
							TamingSystem.addPetEntity(Entity.getEntityId(), Entity);
							
							if (Entity instanceof Tameable)
							{
								((Tameable)Entity).setOwner(Player);
							}
							
							if (Entity instanceof Creature)
							{
								((Creature)Entity).setTarget(null); // This sets the hostile-mobs target to null when tamed
							}
							
							Player.setItemInHand(new ItemHandler().RemoveFromStack(Player.getItemInHand(), 1));
							walkToPlayer(Entity,Player);
							SkillHandlers.HandleMobTaming(Player, Entity);
							Bukkit.getLogger().info("[TotalWar Taming] " + Player.getName() + " has just tamed a " + Entity.getType().toString());
							return true;
						}
						else
						{
							Player.sendMessage(ChatColor.RED + "You need a taming level of " + SkillHandlers.getTameRequirement(Entity.getType()) + " to tame this mob!");
							return false;
						}
					}
					else
					{
						if (!HorseModifier.isHorse(Entity))
						{
							Player.sendMessage(ChatColor.RED + "You need to have a taming level of " + SkillHandlers.getTameRequirement(Entity.getType()) + ", and use " + Configuration.getBait(Entity.getType()).getType().toString().toLowerCase().replace("_", "") + " to tame this mob!");
						}
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public void UntamePet(Player Player, Entity Entity)
	{
		PlayerHandler Handler = TamingSystem.getPlayerHandler(Player.getName());
		Handler.deletePet(TamingSystem.nPetHandler.getPetFromID(Entity.getEntityId()));
	}
	
	public void walkToPlayer(Entity e, Player p)
	{
		if ((e instanceof Tameable) || (e.getPassenger() instanceof Player) || (e instanceof EnderDragon) || (e instanceof Bat))
		{
			return;
		}
		
		if (e.getLocation().distance(p.getLocation()) > 14.0D)
		{
			e.teleport(p);
		}
		else
		{
			if (e instanceof LivingEntity)
			{
				CraftLivingEntity cLivEnt = (CraftLivingEntity)((LivingEntity)e);
				if (cLivEnt.getHandle() instanceof EntityInsentient)
				{
					Navigation n = ((EntityInsentient) cLivEnt.getHandle()).getNavigation();
					//Navigation Nav = ((CraftEntity) e).getHandle().move(p.getLocation().getX() + 2.0D, p.getLocation().getY(), p.getLocation().getZ() + 2.0D);
					n.a(p.getLocation().getX() + 2.0D, p.getLocation().getY(), p.getLocation().getZ() + 2.0D, 0.5F);
				}
			}
		}
	}
	
	public void AttackNearbyEntities(Entity Entity, Player Player)
	{
		List<Entity> NearbyEntities = Player.getNearbyEntities(10, 10, 10);
		
		if (Entity instanceof Creature)
		{
			if (NearbyEntities.isEmpty())
			{
				((Creature)Entity).setTarget(null);
				return;
			}
			else
			{
				for(Entity Targets : NearbyEntities)
				{
					if (Targets != Player)
					{
						if (!Targets.hasMetadata("NPC"))
						{
							if (TamingSystem.nPetHandler.isPet(Targets) && TamingSystem.nPetHandler.getOwner(Targets).equalsIgnoreCase(Player.getName()))
							{
								return;
							}
							else
							{
								if (Targets instanceof Player)
								{
									Player PlayerTarget = (Player)Targets;
									if (new PlayerData().isSameFaction(Player, PlayerTarget) == false)
									{
										((Creature) Entity).setTarget((LivingEntity)Targets);
									}
								}
								else
								{
									if (TamingSystem.nPetHandler.isPet(Targets))
									{
										OfflinePlayer TargetOwner = Bukkit.getOfflinePlayer((TamingSystem.nPetHandler.getOwner(Targets)));
										if (TargetOwner.isOnline())
										{
											Player TargetOwnerP = TargetOwner.getPlayer();
											if (new PlayerData().isSameFaction(Player, TargetOwnerP) == false)
											{
												((Creature) Entity).setTarget((LivingEntity)Targets);
											}
										}
									}
									else
									{
										if (Targets instanceof LivingEntity)
										{
											CraftLivingEntity crEnt = (CraftLivingEntity)((LivingEntity)Entity);
											CraftCreature crCreature = (CraftCreature)crEnt;
											crCreature.setTarget((LivingEntity)Targets);
											//CraftCreature crCreature = (CraftCreature)
											//((Creature) Entity).setTarget((LivingEntity)Targets);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean isPet(Entity Entity)
	{
		try
		{
			if (getPetFromID(Entity.getEntityId()) != null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception Ex)
		{
			return false;
		}
	}
	
}
