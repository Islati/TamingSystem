package TamingSystem;

import java.util.HashMap;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import TamingSystem.Handlers.Pets.PetHandler;
import TamingSystem.Handlers.Pets.TamedPet;
import TamingSystem.Handlers.Players.PlayerHandler;
import TamingSystem.Listeners.EntityListener;
import TamingSystem.Listeners.PlayerListener;


public class TamingSystem extends JavaPlugin
{
	int taskID;
	private static Configuration config;
	
	public static HashMap<String, PlayerHandler> PlayerHandlers = new HashMap<String, PlayerHandler>();
	public static HashMap<Integer, Entity> PetEntities = new HashMap<Integer, Entity>();
	public static PetHandler nPetHandler;
	public static Permission permission = null;
	@Override
	public void onEnable()
	{
		setupPermissions();
		nPetHandler = new PetHandler();
		config = new Configuration(this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		petFollowTask();
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
	}
	
	public static PlayerHandler getPlayerHandler(String PlayerName)
	{
		return TamingSystem.PlayerHandlers.get(PlayerName);
	}
	
	public static void addPlayerHandler(String PlayerName)
	{
		if (!TamingSystem.PlayerHandlers.containsKey(PlayerName))
		{
			TamingSystem.PlayerHandlers.put(PlayerName, new PlayerHandler(PlayerName));
		}
		TamingSystem.PlayerHandlers.get(PlayerName).LoadPets();
	}
	
	public static void addPetEntity(Integer EntityID,Entity Entity)
	{
		if (!PetEntities.containsKey(EntityID))
		{
			PetEntities.put(EntityID, Entity);
		}
	}
	
	public static void RemovePetEntity(Integer EntityID)
	{
		if (PetEntities.containsKey(EntityID))
		{
			PetEntities.remove(EntityID);
		}
	}
	
	public static void RemovePetEntity(Entity Entity)
	{
		RemovePetEntity(Entity.getEntityId());
	}
	
	private void petFollowTask()
	{
		this.taskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				for (Player Player : Bukkit.getOnlinePlayers())
				{
					PlayerHandler Handler = TamingSystem.getPlayerHandler(Player.getName());
					
					if (Handler != null && Handler.hasPets())
					{
						for(TamedPet Pet : Handler.getPets())
						{
							Entity PetEntity = TamingSystem.nPetHandler.getPetEntity(Pet.getEntityID());
							if (PetEntity != null)
							{
								if (Player.getWorld() == PetEntity.getWorld())
								{
									if ((Player.getLocation().distance(PetEntity.getLocation()) > 5.0D) && (Pet.isFollowing()))
									{
										nPetHandler.walkToPlayer(PetEntity, Player);
									}
									else
									{
										nPetHandler.AttackNearbyEntities(PetEntity, Player);
									}
								}
							}
						}
					}
				}
			}
		}, 20L, 20L);
	}
	
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}