package TamingSystem.Handlers.Players;

import org.bukkit.entity.Player;

import TamingSystem.TamingSystem;

public class PlayerData
{
	public PlayerData()
	{
	}

	public Faction getPlayerFaction(Player Player)
	{
		if (TamingSystem.permission.playerInGroup(Player, "Templar"))
		{
			return Faction.Templar;
		}
		else if (TamingSystem.permission.playerInGroup(Player, "Pagan"))
		{
			return Faction.Pagan;
		}
		else if (TamingSystem.permission.playerInGroup(Player, "Pirate"))
		{
			return Faction.Pirate;
		}
		else if (TamingSystem.permission.has(Player, "Dragonkin"))
		{
			return Faction.Dragonkin;
		}
		return Faction.None;
	}
/*
	public String getPlayerFactionPerm(Player Player)
	{
		if (TamingSystem..permission.has(Player, "Templar"))
		{
			return "TamingSystem..Templar";
		}
		else if (TamingSystem..permission.has(Player, "Pagan"))
		{
			return "TamingSystem..Pagan";
		}
		else if (TamingSystem..permission.has(Player, "TamingSystem..Pirate"))
		{
			return "TamingSystem..Pirate";
		}
		else if (TamingSystem..permission.has(Player, "TamingSystem..Dragonkin"))
		{
			return "TamingSystem..Dragonkin";
		}
		return null;
	}
*/
	public boolean isSameFaction(Player PlayerOne, Player PlayerTwo)
	{
		return getPlayerFaction(PlayerOne) == getPlayerFaction(PlayerTwo);
	}

	/*
	 * public boolean isSameFactionPerm(Player PlayerOne, Player PlayerTwo) {
	 * return }
	 */

	public Faction getFaction(String Name)
	{
		if (Name.equalsIgnoreCase("pagan"))
		{
			return Faction.Pagan;
		}
		if (Name.equalsIgnoreCase("templar"))
		{
			return Faction.Templar;
		}
		if (Name.equalsIgnoreCase("pirate"))
		{
			return Faction.Pirate;
		}
		if (Name.equalsIgnoreCase("dragonkin"))
		{
			return Faction.Dragonkin;
		}
		return null;
	}

	public static enum Faction
	{
		Templar, Pagan, Pirate, Dragonkin, None;
	}
}