package pages;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class UserPageHandler extends DefaultHandler 
{
	
	private String root, waitingFor, user, backgroundColor, log, logInfo;
	
	public UserPageHandler(String root)
	{
		this.root = root;
	}
	
	public void startDocument()
	{
		waitingFor = "";
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{ 
		if (qName.equals(root))
		{
			waitingFor = "user_characters";
		} else if (qName.equals("backgroundColor"))
		{
			waitingFor = "backgroundColor_characters";
		} else if (qName.equals("log"))
		{
			waitingFor = "log_characters";
		} else if (qName.equals("logInfo"))
		{
			waitingFor = "logInfo_characters";
		}
	}
	
	public void endElement(String uri, String localName, String qName)
	{
		if (qName.equals(root)) waitingFor = "";
	}
	
	public void characters(char[] ch, int start, int length)
	{
		if (waitingFor.equals("user_characters"))
		{
			user = new String(ch, start, length).trim();
		} else if (waitingFor.equals("backgroundColor_characters"))
		{
			backgroundColor = new String(ch, start, length).trim();
		} else if (waitingFor.equals("log_characters"))
		{
			log = new String(ch, start, length).trim();
		} else if (waitingFor.equals("logInfo_characters"))
		{
			logInfo = new String(ch, start, length).trim();
		}
	}
	
	public Page getUserPage()
	{
		// check if no user or background color
		// log and loginfo could be null if user has no logs
		if (user == null || backgroundColor == null)
		{
			return null;
		}
		Page page = new Page(user);
		page.setBackgroundColor(backgroundColor);
		if (log != null && logInfo != null) page.setLog(log, logInfo);
		return page;
	}

}
