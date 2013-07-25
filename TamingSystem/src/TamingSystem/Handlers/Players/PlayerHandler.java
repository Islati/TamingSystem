package TamingSystem.Handlers.Players;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import TamingSystem.TamingSystem;
import TamingSystem.Handlers.Pets.TamedPet;



public class PlayerHandler
{
	private String Name = "";
	private String UserFolder = "";
	private String TamingDirectory = "plugins/TamingSystem/";
	private List<TamedPet> Pets = new ArrayList<TamedPet>();
	
	public PlayerHandler(String Name)
	{
		this.Name = Name;
		File PlayerFolder = new File(this.TamingDirectory + Name);
		if (!PlayerFolder.exists())
		{
			PlayerFolder.mkdir();
		}
		this.UserFolder = this.TamingDirectory + Name + "/";
	}
	
	public PlayerHandler() { }
	
	public void setName(String Name)
	{
		this.Name = Name;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public TamedPet getPetFromFile(String File)
	{
		TamedPet Pet = new TamedPet();
		Pet.LoadPetFromFile(File);
		Pet.setFileLocation(File);
		return Pet;
	}
	
	public List<TamedPet> getPets()
	{
		return this.Pets;
	}
	
	public boolean deletePet(TamedPet Pet)
	{
		File PetFile = new File(Pet.getFileLocation());
		try
		{
			FileUtils.forceDelete(PetFile);
			//PetFile.delete();
			TamingSystem.RemovePetEntity(Pet.getEntityID());
			this.Pets.remove(Pet);
			return true;
		}
		catch (Exception Ex)
		{
			//Bukkit.getLogger().info(e.getMessage());
			return false;
		}
	}
	
	public boolean addPet(TamedPet Pet)
	{
		if (!this.Pets.contains(Pet))
		{
			this.Pets.add(Pet);
			return true;
		}
		return false;
	}
	
	public boolean addPetAndSaveData(TamedPet Pet)
	{
		if (!this.Pets.contains(Pet))
		{
			this.Pets.add(Pet);
			this.SavePet(Pet);
			return true;
		}
		return false;
	}
	
	public void SavePet(TamedPet Pet)
	{
		if (Pet.getKilled() == false)
		{
			if (Pet.getFileLocation().isEmpty() == false)
			{
				Pet.SavePetToFile(Pet.getFileLocation());
			}
			else
			{
				Pet.setFileLocation(this.getUserFolder() + String.valueOf(System.currentTimeMillis()) + new Random().nextInt(900) + 1 + ".txt");
				Pet.SavePetToFile(Pet.getFileLocation());
			}
		}
		else
		{
			if (Pet.getFileLocation().isEmpty() == false)
			{
				if (FileUtils.deleteQuietly(new File(Pet.getFileLocation())) == true)
				{
					Bukkit.getLogger().info("[Pet deleted @ " + Pet.getFileLocation());
				}
			}
		}
	}
	
	public List<TamedPet> getPets(EntityType SearchType)
	{
		List<TamedPet> Returning = new ArrayList<TamedPet>();
		for(TamedPet Pet : this.getPets())
		{
			if (Pet.getType() == SearchType)
			{
				Returning.add(Pet);
			}
		}
		return Returning;
	}
	
	public TamedPet getPet(int EntityID)
	{
		for(TamedPet PlayerPet : this.getPets())
		{
			if (PlayerPet.getEntityID() == EntityID)
			{
				return PlayerPet;
			}
		}
		return null;
	}
	
	public boolean PlayerOwnsPet(int EntityID)
	{
		if (getPet(EntityID) != null)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasPets()
	{
		return this.Pets.size() > 0;
	}
	
	public void LoadPets()
	{
		for(String S : this.getPetFiles())
		{
			TamedPet Pet = this.getPetFromFile(S);
			Pet.setFileLocation(S);
			if (Pet.getKilled() == false)
			{
				if (this.addPet(Pet) == true)
				{
					Bukkit.getLogger().info("[TamingSystem] Loaded a tamed entity for player " + this.getName());
				}
				else
				{
					Bukkit.getLogger().info("[TamingSystem] Failed to load a tamed entity for player " + this.getName());
				}
			}
			else
			{
				try
				{
					FileUtils.forceDelete(new File(S));
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void SpawnPets()
	{
		try
		{
			for(TamedPet Pet : this.getPets())
			{
				if (Pet.getKilled() == false)
				{
					Location SpawnLocation = Pet.getLocation().clone();
					SpawnLocation.setY(SpawnLocation.getY() + 1.0D);
					LivingEntity Entity = (LivingEntity)SpawnLocation.getWorld().spawnEntity(SpawnLocation, Pet.getType());
					Pet.setEntityAsPet(Entity);
					Pet.setEntityID(Entity.getEntityId());
					Pet.setOwner(this.getName());
					TamingSystem.addPetEntity(Pet.getEntityID(), Entity);
					Bukkit.getLogger().info("[TamingSystem] Spawned a tamed entity for player " + this.getName());
				}
			}
		}
		catch (Exception Ex)
		{
			Bukkit.getLogger().info("[TamingSystem] " + Ex.getMessage());
		}
	}
	
	public void DespawnPets()
	{
		try
		{
			for(TamedPet Pet : this.getPets())
			{
				if (Pet.getKilled() != true)
				{
					Entity Entity = TamingSystem.nPetHandler.getPetEntity(Pet.getEntityID());
					TamingSystem.RemovePetEntity(Pet.getEntityID());
					Bukkit.getLogger().info("[TamingSystem] Despawned a tamed entity [" + Entity.getType() + "] for player " + this.getName());
					Entity.remove();
				}
				else
				{
					Entity Entity = TamingSystem.nPetHandler.getPetEntity(Pet.getEntityID());
					TamingSystem.RemovePetEntity(Pet.getEntityID());
					Bukkit.getLogger().info("[TamingSystem] Despawned a tamed entity in prep for delition [" + Entity.getType() + "] for player " + this.getName());
					Entity.remove();
					FileUtils.forceDelete(new File(Pet.getFileLocation()));
				}
			}
			this.Pets.clear();
		}
		catch (Exception Ex)
		{
			Bukkit.getLogger().info("[TamingSystem] " + Ex.getMessage());
		}
	}
	
	public List<String> getPetFiles()
	{
		List<String> PetFiles = new ArrayList<String>();
		for(File Pet : FileUtils.listFiles(new File(this.UserFolder), new String[] { "txt" }, false))
		{
			PetFiles.add(this.UserFolder + Pet.getName());
		}
		return PetFiles;
	}
	
	public void SavePets()
	{
		for(TamedPet Pet : this.getPets())
		{
			if (Pet.getKilled() == true)
			{
				
			}
			else
			{
				if (Pet.getFileLocation().isEmpty() == false)
				{
					Pet.SavePetToFile(Pet.getFileLocation());
				}
				else
				{
					Pet.setFileLocation(this.getUserFolder() + String.valueOf(System.currentTimeMillis()) + new Random().nextInt(900) + 1 + ".txt");
					Pet.SavePetToFile(Pet.getFileLocation());
				}
			}
		}
	}
	
	public String getUserFolder()
	{
		return this.UserFolder;
	}
	
	
}
