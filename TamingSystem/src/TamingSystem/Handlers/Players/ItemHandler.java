package TamingSystem.Handlers.Players;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemHandler
{
	public ArrayList<String> getItemLore(ItemStack Item)
	{
		ArrayList<String> ReturnLore = new ArrayList<String>();
		if (Item.hasItemMeta())
		{
			for (String S : Item.getItemMeta().getLore())
			{
				ReturnLore.add(S);
			}
			return ReturnLore;
		}
		
		return null;
	}
	
	public String getItemLore(ItemStack Item, int Line)
	{
		if (Item.hasItemMeta())
		{
			return (String) Item.getItemMeta().getLore().get(Line);
		}
		
		return null;
	}
	
	public boolean itemLoreContains(ItemStack Item, String Text)
	{
		if (Item.hasItemMeta())
		{
			ArrayList<String> Lore = getItemLore(Item);
			for (String s : Lore)
			{
				if (s.toLowerCase().contains(Text.toLowerCase()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void setItemName(ItemStack Item, String Text)
	{
		if (Item.hasItemMeta())
		{
			ItemMeta iMeta = Item.getItemMeta();
			iMeta.setDisplayName(Text);
			Item.setItemMeta(iMeta);
		}
	}
	
	public String getItemName(ItemStack Item)
	{
		return Item.getItemMeta().getDisplayName();
	}
	
	public ItemStack RemoveFromStack(ItemStack Item, int Amount)
	{
		if (Item.getAmount() > Amount)
		{
			ItemMeta StackMeta = Item.getItemMeta();
			ItemStack Return = new ItemStack(Item.getType(), Item.getAmount() - Amount);
			Return.setItemMeta(StackMeta);
			Return.setDurability(Item.getDurability());
			return Return;
		}
		return null;
	}
}