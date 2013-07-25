package TamingSystem;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


public final class Configuration

{
	private final TamingSystem plugin;
	private static HashMap<String, ItemStack> baitMap = new HashMap<String, ItemStack>();
	private static HashMap<String, Integer> tamingXPMap = new HashMap<String, Integer>();
	public static boolean provokable;
	public static boolean ridable;
	public static boolean attackTame;
	public static int idleDistance;
	public static int attackDistance;
	public static int maxPetsPerPlayer = 2;
	public static boolean opsBypassPerms;
	public static boolean PetsAttackPlayers;
	public static boolean petsAttackPets;
	public static boolean invinciblePets;
	public static long mainLoop;
	public static boolean disablePermissions;
	public static String defaultPetMode;
	public static String commandPrefix;
	public static int maxSpawnCount;
	public static boolean overrideDefaultTaming;
	public static ArrayList<String> nameFiles = new ArrayList<String>();
	public static boolean randomizePetNames;
	public static boolean mcMMOSuport;
	public static boolean rememberPetLocation;
	public static boolean noDropOnKillCommand;
	public static String defaultPetAge;
	public static boolean lockSpawnedBabies;
	public static boolean customNamePlates;
	public static String namePlateColor;
	public static final String[] mobs = { "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Ghast", "Giant", "Golem", "MagmaCube", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Sheep", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie" };
	
	public Configuration(TamingSystem plugin)
	
	{
		this.plugin = plugin;
		load();
	}
	
	public ItemStack getMat(String s, String mobName)
	
	{
		ItemStack item = new ItemStack(0);
		Material mat = Material.AIR;
		if (s == null)
		{
			return item;
		}
		byte byteCode = 0;
		String matName;
		if (s.contains(":"))
		{
			String[] tmp = s.split(":", 2);
			matName = tmp[0];
			if (tmp[1].matches("\\d+"))
			{
				byteCode = (byte) Integer.parseInt(tmp[1]);
				
			}
		}
		else
		{
			matName = s;
		}
		if (matName.matches("\\d+"))
		{
			mat = Material.getMaterial(Integer.parseInt(matName));
		}
		else if (Material.matchMaterial(matName) != null)
		{
			mat = Material.matchMaterial(matName);
		}
		else
		{
			//this.plugin.PetHandler.logInfo("Invalid bait: " + matName);
		}
		if (mat != null)
		{
			item = new ItemStack(mat, 1, byteCode);
			//this.plugin.PetHandler.logInfo("[" + mobName + "] [" + s + "] [" + item.getType().toString() + "]");
		}
		return item;
	}
	
	public void load()
	{
		for (String s : mobs)
		{
			baitMap.put(s, getMat(this.plugin.getConfig().getString(s), s));
		}
		
		provokable = this.plugin.getConfig().getBoolean("Provokable", true);
		ridable = this.plugin.getConfig().getBoolean("Ridable", true);
		attackTame = this.plugin.getConfig().getBoolean("AttackTame", false);
		idleDistance = this.plugin.getConfig().getInt("IdleDistance", 5);
		attackDistance = this.plugin.getConfig().getInt("AttackDistance", 10);
		maxPetsPerPlayer = this.plugin.getConfig().getInt("MaxPetsPerPlayer", 1);
		opsBypassPerms = this.plugin.getConfig().getBoolean("OpsBypassPerms", false);
		PetsAttackPlayers = this.plugin.getConfig().getBoolean("PetsAttackPets", true);
		petsAttackPets = this.plugin.getConfig().getBoolean("PetsAttackPets", true);
		mainLoop = this.plugin.getConfig().getLong("MainLoop", 1000L);
		disablePermissions = this.plugin.getConfig().getBoolean("DisablePermissions", false);
		defaultPetMode = this.plugin.getConfig().getString("DefaultPetMode", "P").toUpperCase();
		invinciblePets = this.plugin.getConfig().getBoolean("InvinciblePets", true);
		maxSpawnCount = this.plugin.getConfig().getInt("MaxSpawnCount", 1);
		overrideDefaultTaming = this.plugin.getConfig().getBoolean("OverrideDefaultTaming", true);
		nameFiles = (ArrayList<String>) this.plugin.getConfig().getStringList("NameFiles");
		randomizePetNames = this.plugin.getConfig().getBoolean("RandomizePetNames", true);
		mcMMOSuport = this.plugin.getConfig().getBoolean("mcMMOSuport", true);
		rememberPetLocation = this.plugin.getConfig().getBoolean("RememberPetLocation", false);
		noDropOnKillCommand = this.plugin.getConfig().getBoolean("NoDropOnKillCommand", false);
		defaultPetAge = this.plugin.getConfig().getString("DefaultPetAge", "adult");
		lockSpawnedBabies = this.plugin.getConfig().getBoolean("LockSpawnedBabies", false);
		customNamePlates = this.plugin.getConfig().getBoolean("CustomNamePlates", true);
		namePlateColor = this.plugin.getConfig().getString("NamePlateColor", "GREEN");
		
		commandPrefix = this.plugin.getConfig().getString("CommandPrefix", "pet");
	}
	
	public static ItemStack getBait(EntityType type)
	{
		if (baitMap.containsKey(type.getName()))
		{
			return baitMap.get(type.getName());
		}
		return new ItemStack(0);
	}
	
	public static Integer getTamingXP(EntityType type)
	{
		if (tamingXPMap.containsKey(type.getName()))
		{
			return tamingXPMap.get(type.getName());
		}
		return Integer.valueOf(0);
	}
	
}