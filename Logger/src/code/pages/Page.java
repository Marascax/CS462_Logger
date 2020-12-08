package pages;

import java.io.Serializable;

/**
 * Class representing a user page.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class Page implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	// properties of user page
	private String user, backgroundColor, log, logInfo, xmlString;
	
	/**
	 * Constructor for new page for specified user.
	 * @param user name
	 */
	public Page(final String user)
	{
		this.user = user;
		backgroundColor = "#FFFFFF";
		log = "";
		logInfo = "";
		updateXML();
	}
	
	/**
	 * Get the user for this page.
	 * @return user name
	 */
	public String getUser()
	{
		return user;
	}
	
	/**
	 * Get the hex background color of this page.
	 * @return background color
	 */
	public String getBackgroundColor()
	{
		return backgroundColor;
	}
	
	/**
	 * Get the current log of this page.
	 * @return log
	 */
	public String getLog()
	{
		return log;
	}
	
	/**
	 * Get the current log info of this page.
	 * @return logInfo
	 */
	public String getLogInfo()
	{
		return logInfo;
	}
	
	/**
	 * Get the current XML representation of this page.
	 * @return XMLString
	 */
	public String getXML()
	{
		return xmlString;
	}
	
	/**
	 * Set the background color of this page.
	 * @param hexColor string of new color
	 */
	public void setBackgroundColor(final String hexColor)
	{
		backgroundColor = hexColor;
		updateXML();
	}
	
	/**
	 * Set the log of this page.
	 * @param newLog updated log
	 * @param newLogInfo updated log info
	 */
	public void setLog(final String newLog, final String newLogInfo)
	{
		log = newLog;
		logInfo = newLogInfo;
		updateXML();
	}
	
	/**
	 * Update the XML string representation of this page.
	 */
	private void updateXML()
	{
		xmlString = "<?xml version=\"1.0\"?>"
				+ "<?xml-stylesheet type=\"text/xsl\""
				+ " href=\"page_stylesheet.xsl\"?>"
				+ "<page>"
				+ "<name>" + user + "</name>"
				+ "<backgroundColor>" + backgroundColor + "</backgroundColor>"
				+ "<log>" + log + "</log>"
				+ "<logInfo>" + logInfo + "</logInfo>"
				+ "</page>";
		
	}

}
