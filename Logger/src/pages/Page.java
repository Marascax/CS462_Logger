package pages;

import java.io.Serializable;

public class Page implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user, backgroundColor, log, logInfo;
	
	public Page(String user)
	{
		this.user = user;
		backgroundColor = "#FFFFFF";
		log = "";
		logInfo = "";
	}
	
	public String getUser()
	{
		return user;
	}
	
	public String getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public String getLog()
	{
		return log;
	}
	
	public String getLogInfo()
	{
		return logInfo;
	}
	
	public void setBackgroundColor(String hexColor)
	{
		backgroundColor = hexColor;
	}
	
	public void setLog(String newLog, String newLogInfo)
	{
		log = newLog;
		logInfo = newLogInfo;
	}
	
	public String toXML()
	{
		String s = "<?xml version=\"1.0\"?>"
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"page_stylesheet.xsl\"?>"
				+ "<page>"
				+ "<name>" + user + "</name>"
				+ "<backgroundColor>" + backgroundColor + "<backgroundColor>"
				+ "<log>" + log + "</log>"
				+ "<logInfo>" + logInfo + "</logFile>"
				+ "</page>";
		
		return s;
	}

}
