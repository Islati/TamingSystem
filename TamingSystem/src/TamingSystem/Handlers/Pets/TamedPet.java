package TamingSystem.Handlers.Pets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.material.MaterialData;

import TamingSystem.Handlers.Tag;
import TamingSystem.Handlers.TextData;

public class TamedPet
{
	private EntityType Type = EntityType.UNKNOWN;
	private Tag EntityTypeTag = new Tag("Type");
	private Villager.Profession Profession = Villager.Profession.FARMER;
	private Tag ProfessionTag = new Tag("Profession");
	private String CatType = "BLACK_CAT";
	private Tag CatTypeTag = new Tag("CatType");
	private int EntityID = -1;
	private Tag EntityIDTag = new Tag("EntityID");
	private double CurrentHP = 0;
	private Tag CurrentHPTag = new Tag("CurrentHP");
	private double MaximumHP = 0;
	private Tag MaximumHPTag = new Tag("MaximumHP");
	private int Size = 0;
	private Tag SizeTag = new Tag("Size");
	private boolean isSheared = false;
	private Tag ShearedTag = new Tag("IsSheared");
	private String Color = "";
	private Tag ColorTag = new Tag("Color");
	private boolean isSaddled = false;
	private Tag SaddledTag = new Tag("IsSaddled");
	private String PetName = "";
	private Tag PetNameTag = new Tag("PetName");
	private boolean isFollowed = true;
	private Tag FollowTag = new Tag("IsFollowing");
	private int EntityAge = 0;
	private Tag EntityAgeTag = new Tag("Age");
	private boolean isPowered = false;
	private Tag PoweredTag = new Tag("Powered");
	private MaterialData CarriedMaterial = new MaterialData(0);
	private Tag CarriedMaterialTag = new Tag("CarriedMaterial");
	// public modes EntityMode = modes.DEFENSIVE; TODO ENTITIES ONLY ATTACK SHIT
	// NEAR OWNER
	private int EntityLevel = 1; // TODO Possible Entity Level
	private Tag LevelTag = new Tag("Level");
	private int EntityExp = 0; // TODO Possible Entity EXP
	private Tag ExpTag = new Tag("Exp");
	private int _SkeletonType = 0;
	private Tag SkeletonTypeTag = new Tag("SkeletonType");
	private boolean isAgeLocked = false;
	private Tag AgeLockedTag = new Tag("AgeLocked");
	private boolean isZombieVillager = false;
	private Tag ZombieVillagerTag = new Tag("IsZombieVillager");
	private double x;
	private Tag XTag = new Tag("X-Coord");
	private double y;
	private Tag YTag = new Tag("Y-Coord");
	private double z;
	private Tag ZTag = new Tag("Z-Coord");
	private String world = "PaganTemplar";
	private Tag WorldTag = new Tag("World");
	private boolean isSitting = false;
	private Tag SittingTag = new Tag("IsSitting");
	private boolean isBaby = false;
	private Tag BabyTag = new Tag("IsBaby");
	private String Owner = "";
	private Tag OwnerTag = new Tag("Owner");
	private String FileLocation = "";
	private Tag FileTag = new Tag("FileLocation");
	private boolean IsKilled = false;
	private Tag KTag = new Tag("Killed");
	
	public TamedPet() { }
	
	public TamedPet(LivingEntity TamedEntity, String Owner)
	{
		this.Owner = Owner;
		this.getPetFromEntity(TamedEntity);
	}
	
	public boolean LoadPetFromFile(String FileLocation)
	{
		try
		{
			File PetFile = new File(FileLocation);
			if (PetFile.exists())
			{
				TextData Data = new TextData(FileLocation);
				this.setType(EntityType.valueOf(Data.getBetween(this.EntityTypeTag)));
				this.setProfession(Villager.Profession.valueOf(Data.getBetween(this.ProfessionTag)));
				this.setCatType(Data.getBetween(this.CatTypeTag));
				//this.setEntityID(Integer.parseInt(Data.getBetween(Tag)))
				//this.setHP(Integer.parseInt(Data.getBetween(this.CurrentHPTag)));
				this.setHP(Double.parseDouble(Data.getBetween(this.CurrentHPTag)));
				this.setMaxHP(Double.parseDouble(Data.getBetween(this.MaximumHPTag)));
				this.setSize(Integer.parseInt(Data.getBetween(this.SizeTag)));
				this.setSheared(Boolean.parseBoolean(Data.getBetween(this.ShearedTag)));
				this.setColor(Data.getBetween(this.ColorTag));
				this.setSaddled(Boolean.parseBoolean(Data.getBetween(this.SaddledTag)));
				this.setPetName(Data.getBetween(this.PetNameTag));
				this.setFollowing(Boolean.parseBoolean(Data.getBetween(this.FollowTag)));
				this.setAge(Integer.parseInt(Data.getBetween(this.EntityAgeTag)));
				this.setPowered(Boolean.parseBoolean(Data.getBetween(this.PoweredTag)));
				this.setCarriedMaterial(new MaterialData(Material.getMaterial(Data.getBetween(this.CarriedMaterialTag))));
				this.setSkeletonType(Integer.parseInt(Data.getBetween(this.SkeletonTypeTag)));
				this.setAgeLocked(Boolean.parseBoolean(Data.getBetween(this.AgeLockedTag)));
				this.setZombieVillager(Boolean.parseBoolean(Data.getBetween(this.ZombieVillagerTag)));
				this.setLocation(new Location(Bukkit.getWorld(Data.getBetween(this.WorldTag)),Double.parseDouble(Data.getBetween(this.XTag)),Double.parseDouble(Data.getBetween(this.YTag)),Double.parseDouble(Data.getBetween(this.ZTag))));
				this.setSitting(Boolean.parseBoolean(Data.getBetween(this.SittingTag)));
				this.setBaby(Boolean.parseBoolean(Data.getBetween(this.BabyTag)));
				this.setOwner(Data.getBetween(this.OwnerTag));
				this.FileLocation = FileLocation;
				//this.setKilled(Boolean.parseBoolean(Data.getBetween(this.KTag)));
				return true;
			}
			return false;
		}
		catch (Exception Ex)
		{
			Bukkit.getLogger().info(Ex.getMessage());
			return false;
		}
	}
	
	private String Text(Tag Tag, String String)
	{
		return Tag.getOpen() + String + Tag.getClose();
	}
	
	public boolean SavePetToFile(String FileLocation)
	{
		List<String> LinesOfData = new ArrayList<String>();
		LinesOfData.add(Text(this.EntityTypeTag,this.getType().name()));
		LinesOfData.add(Text(this.ProfessionTag,this.getProfession().name()));
		LinesOfData.add(Text(this.CatTypeTag,this.getCatType().name()));
		LinesOfData.add(Text(this.CurrentHPTag,String.valueOf(this.getHP())));
		LinesOfData.add(Text(this.MaximumHPTag,String.valueOf(this.getMaxHP())));
		LinesOfData.add(Text(this.SizeTag,String.valueOf(this.getSize())));
		LinesOfData.add(Text(this.ShearedTag,String.valueOf(this.isSheared())));
		LinesOfData.add(Text(this.ColorTag,this.getColor()));
		LinesOfData.add(Text(this.SaddledTag,String.valueOf(this.isSaddled())));
		LinesOfData.add(Text(this.PetNameTag,this.getPetName()));
		LinesOfData.add(Text(this.FollowTag,String.valueOf(this.isFollowing())));
		LinesOfData.add(Text(this.EntityAgeTag,String.valueOf(this.getAge())));
		LinesOfData.add(Text(this.PoweredTag,String.valueOf(this.isPowered())));
		LinesOfData.add(Text(this.CarriedMaterialTag,String.valueOf(this.getCarriedMaterial().getItemTypeId())));
		LinesOfData.add(Text(this.SkeletonTypeTag,String.valueOf(this.getSkeltonType())));
		LinesOfData.add(Text(this.AgeLockedTag,String.valueOf(this.isAgeLocked())));
		LinesOfData.add(Text(this.ZombieVillagerTag,String.valueOf(this.isZombieVillager())));
		LinesOfData.add(Text(this.XTag,String.valueOf(this.getLocation().getX())));
		LinesOfData.add(Text(this.YTag,String.valueOf(this.getLocation().getY())));
		LinesOfData.add(Text(this.ZTag,String.valueOf(this.getLocation().getZ())));
		LinesOfData.add(Text(this.WorldTag,this.getLocation().getWorld().getName()));
		LinesOfData.add(Text(this.SittingTag,String.valueOf(this.isSitting())));
		LinesOfData.add(Text(this.BabyTag,String.valueOf(this.isBaby())));
		LinesOfData.add(Text(this.OwnerTag,this.getOwner()));
		LinesOfData.add(Text(this.FileTag,this.FileLocation));
		LinesOfData.add(Text(this.KTag,String.valueOf(this.getKilled())));
		
		String WriteData = "";
		for(String S : LinesOfData)
		{
			WriteData += S + "\r\n";
		}
		
		try
		{
			FileUtils.writeStringToFile(new File(FileLocation), WriteData, false);
			return true;
		}
		catch (IOException e)
		{
			Bukkit.getLogger().info(e.getMessage());
			return false;
		}
	}
	
	public void setKilled(boolean Value)
	{
		this.IsKilled = Value;
	}
	
	public boolean getKilled()
	{
		return this.IsKilled;
	}
	
	public void getPetFromEntity(Damageable TamedEntity)
	{
		this.setHP(TamedEntity.getMaxHealth());
		this.setMaxHP(TamedEntity.getMaxHealth());
		this.setType(TamedEntity.getType());
		this.setEntityID(TamedEntity.getEntityId());
		this.setPetName(((LivingEntity)TamedEntity).getCustomName());
		this.setLocation(TamedEntity.getLocation());
		
		switch (TamedEntity.getType())
		{
			case MAGMA_CUBE:
				this.setSize(((MagmaCube) TamedEntity).getSize());
				break;
			case PIG:
				this.setSaddled(((Pig) TamedEntity).hasSaddle());
				break;
			case SHEEP:
				this.setSheared(((Sheep) TamedEntity).isSheared());
				this.setColor(((Sheep) TamedEntity).getColor().toString());
				break;
			case SKELETON:
				this.setSkeletonType(((Skeleton) TamedEntity).getType().getTypeId());
				break;
			case SLIME:
				this.setSize(((Slime) TamedEntity).getSize());
				break;
			case VILLAGER:
				this.setProfession(((Villager) TamedEntity).getProfession());
				break;
			case WOLF:
				this.setColor(((Wolf) TamedEntity).getCollarColor().name());
				break;
			case ZOMBIE:
				this.setZombieVillager(((Zombie) TamedEntity).isVillager());
				break;
			case OCELOT:
				this.setCatType(((Ocelot) TamedEntity).getCatType().name());
				break;
			default:
				break;
		}
		
		if (TamedEntity instanceof Ageable)
		{
			this.setAge(((Ageable) TamedEntity).getAge());
			this.setAgeLocked(((Ageable) TamedEntity).getAgeLock());
		}
	}
	
	public void setEntityAsPet(LivingEntity Entity)
	{
		Entity.setMaxHealth(this.getMaxHP());
		Entity.setHealth(this.getHP());
		if (Entity instanceof Ageable)
		{
			((Ageable) Entity).setAge(this.getAge());
			((Ageable) Entity).setAgeLock(this.isAgeLocked());
		}
		
		if (Entity instanceof Tameable)
		{
			((Tameable) Entity).setOwner(Bukkit.getPlayer(this.getOwner()));
		}
		
		//this.setEntityID(Entity.getEntityId());
		
		switch (this.getType())
		{
			case MAGMA_CUBE:
				((MagmaCube) Entity).setSize(this.getSize());
				break;
			case PIG:
				((Pig) Entity).setSaddle(this.isSaddled());
				break;
			case SHEEP:
				((Sheep) Entity).setSheared(this.isSheared());
				((Sheep) Entity).setColor(DyeColor.valueOf(this.getColor()));
				break;
			case SKELETON:
				((Skeleton) Entity).setSkeletonType(SkeletonType.getType(this.getSkeltonType()));
				break;
			case SLIME:
				((Slime) Entity).setSize(this.getSize());
				break;
			case VILLAGER:
				((Villager) Entity).setProfession(this.getProfession());
				break;
			case WOLF:
				if (!this.getColor().isEmpty())
				{
					((Wolf) Entity).setCollarColor(DyeColor.valueOf(this.getColor()));
				}
				break;
			case ZOMBIE:
				((Zombie) Entity).setVillager(this.isZombieVillager());
				((Zombie) Entity).setBaby(isBaby());
				break;
			case OCELOT:
				((Ocelot) Entity).setCatType(this.getCatType());
				break;
			default:
				break;
		}
	}
	
	public void setType(EntityType SetType)
	{
		this.Type = SetType;
	}
	
	public EntityType getType()
	{
		return this.Type;
	}
	
	public Villager.Profession getProfession()
	{
		return this.Profession;
	}
	
	public void setProfession(Villager.Profession Profession)
	{
		this.Profession = Profession;
	}
	
	public Ocelot.Type getCatType()
	{
		return Ocelot.Type.valueOf(this.CatType);
	}
	
	public void setCatType(String CatType)
	{
		this.CatType = CatType;
	}
	
	public void setCatType(Ocelot.Type CatType)
	{
		this.CatType = CatType.name();
	}
	
	public int getEntityID()
	{
		return this.EntityID;
	}
	
	public void setEntityID(int ID)
	{
		this.EntityID = ID;
	}
	
	public double getHP()
	{
		return this.CurrentHP;
	}
	
	public void setHP(double HP)
	{
		this.CurrentHP = HP;
	}
	
	public double getMaxHP()
	{
		return this.MaximumHP;
	}
	
	public void setMaxHP(double HP)
	{
		this.MaximumHP = HP;
	}
	
	public int getSize()
	{
		return this.Size;
	}
	
	public void setSize(int Size)
	{
		this.Size = Size;
	}
	
	public boolean isSheared()
	{
		return this.isSheared;
	}
	
	public void setSheared(boolean Sheared)
	{
		this.isSheared = Sheared;
	}
	
	public String getColor()
	{
		return this.Color;
	}
	
	public void setColor(String Color)
	{
		this.Color = Color;
	}
	
	public boolean isSaddled()
	{
		return this.isSaddled;
	}
	
	public void setSaddled(boolean Saddled)
	{
		this.isSaddled = Saddled;
	}
	
	public String getPetName()
	{
		return this.PetName;
	}
	
	public void setPetName(String Name)
	{
		this.PetName = Name;
	}
	
	public boolean isFollowing()
	{
		return this.isFollowed;
	}
	
	public void setFollowing(boolean Follow)
	{
		this.isFollowed = Follow;
	}
	
	public int getAge()
	{
		return this.EntityAge;
	}
	
	public void setAge(int Age)
	{
		this.EntityAge = Age;
	}
	
	public boolean isPowered()
	{
		return this.isPowered;
	}
	
	public void setPowered(boolean Powered)
	{
		this.isPowered = Powered;
	}
	
	public MaterialData getCarriedMaterial()
	{
		return this.CarriedMaterial;
	}
	
	public void setCarriedMaterial(MaterialData Data)
	{
		this.CarriedMaterial = Data;
	}
	
	public int getSkeltonType()
	{
		return this._SkeletonType;
	}
	
	public void setSkeletonType(int Type)
	{
		this._SkeletonType = Type;
	}
	
	public boolean isAgeLocked()
	{
		return this.isAgeLocked;
	}
	
	public void setAgeLocked(boolean Locked)
	{
		this.isAgeLocked = Locked;
	}
	
	public boolean isZombieVillager()
	{
		return this.isZombieVillager;
	}
	
	public void setZombieVillager(boolean Value)
	{
		this.isZombieVillager = Value;
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
	}
	
	public void setLocation(Location Loc)
	{
		this.world = Loc.getWorld().getName();
		this.x = Loc.getX();
		this.y = Loc.getY();
		this.z = Loc.getZ();
	}
	
	public void setWorld(String World)
	{
		this.world = World;
	}
	
	public void setLocation(Player Player)
	{
		this.setLocation(Player.getLocation());
	}
	
	public boolean isSitting()
	{
		return this.isSitting;
	}
	
	public void setSitting(boolean Value)
	{
		this.isSitting = Value;
	}
	
	public boolean isBaby()
	{
		return this.isBaby;
	}
	
	public void setBaby(boolean Value)
	{
		this.isBaby = Value;
	}
	
	public void setOwner(String Name)
	{
		this.Owner = Name;
	}
	
	public void setOwner(Player Player)
	{
		this.setOwner(Player.getName());
	}
	
	public String getOwner()
	{
		return this.Owner;
	}
	
	public void setFileLocation(String Location)
	{
		this.FileLocation = Location;
	}
	
	public String getFileLocation()
	{
		return this.FileLocation;
	}
	
}
