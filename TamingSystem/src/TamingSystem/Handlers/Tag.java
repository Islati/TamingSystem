package TamingSystem.Handlers;

public class Tag
{
	private String OpenTag = "";
	private String CloseTag = "";
	
	public Tag(String Tag)
	{
		this.OpenTag = "<" + Tag + ">";
		this.CloseTag = "</" + Tag + ">";
	}
	
	public String getOpen()
	{
		return this.OpenTag;
	}
	
	public String getClose()
	{
		return this.CloseTag;
	}
	
	public String Open()
	{
		return this.getOpen();
	}
	
	public String Close()
	{
		return this.getClose();
	}
}
