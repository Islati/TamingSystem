package TamingSystem.Handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class TextData {
	
	private String Location = "";
	
	public TextData(String FileLocation)
	{
		this.Location = FileLocation;
	}
	
	public String getText()
	{
		try
		{
			return FileUtils.readFileToString(new File(this.Location));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String getBetween(String Start, String End)
	{
		return getBetween(getText(), Start, End);
	}
	
	public static String getBetween(String Data, String Start, String End)
	{
		return StringUtils.substringBetween(Data,Start,End);
	}
	
	public static String getBetween(String Data, Tag Tag)
	{
		return getBetween(Data,Tag.Open(),Tag.Close());
	}
	
	public String getBetween(Tag Tag)
	{
		return getBetween(getText(),Tag.Open(),Tag.Close());
	}
	
	public static List<String> getAllBetween(String Data, String Start, String End)
	{
		String[] getData = StringUtils.substringsBetween(Data, Start, End);
		ArrayList<String> Return = new ArrayList<String>();
		for(String S : getData)
		{
			Return.add(S);
		}
		return Return;
	}
	
	public List<String> getAllBetween(String Start, String End)
	{
		return getAllBetween(getText(),Start,End);
	}
	
	public List<String> getAllBetween(Tag Tag)
	{
		return getAllBetween(getText(),Tag.getOpen(),Tag.getClose());
	}
	
	public static List<String> getAllBetween(String Data, Tag Tag)
	{
		return getAllBetween(Data,Tag.getOpen(),Tag.getClose());
	}

}
