package pages;

import java.rmi.Naming;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.*;
import java.io.*;

/**
 * Class controlling interactions with User pages.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class PageController 
{
	
	private static final boolean DEBUG = true;
	
	private static UserDatabase db;			// user database for each user page
	private static FileInputStream fis;		// file input for user pages
	private static ObjectInputStream ois;	// object input to read in serialized user pages
	private static FileOutputStream fos;	// file output for user pages and htmls
	
	// object output to write serialized user pages and user htmls
	private static ObjectOutputStream oos;	
	
	/**
	 * Get the user database from the RMI registry.
	 * Add in any saved, serialized user pages.
	 * Should be done before any other method calls.
	 * @throws Exception
	 */
	public static void initializeUserDatabase() throws Exception
	{
		db = (UserDatabase)Naming.lookup("rmi://localhost:1099/UserDatabase");
		
		// get the directory with the already made user pages, add them to user database
		File pagesDir = new File(".\\user_pages").getCanonicalFile();
		for (File userFile : pagesDir.listFiles()) 
		{
			if (userFile.isDirectory()) continue;
			fis = new FileInputStream(userFile);
			ois = new ObjectInputStream(fis);
			Page userPage = (Page)ois.readObject();
			if (userPage != null) db.add(userPage);
			ois.close();
			
			if (DEBUG && userPage != null) 
				System.out.println("Loaded user page for " + userPage.getUser());
		}
	}
	
	/**
	 * Check user database for user with specified name.
	 * @param name of user to check for
	 * @return boolean indicating if user with name is in database
	 * @throws Exception
	 */
	public static boolean checkForUser(final String name) throws Exception
	{
		return db.get(name) != null;
	}
	
	/**
	 * Create and add user page for user with specified name.
	 * Write new user page to file.
	 * @param name of new user
	 * @throws Exception
	 */
	public static void createUserPage(final String name) throws Exception
	{
		Page page = new Page(name);
		db.add(page);
		
		// write user page to directory with other user pages
		fos = new FileOutputStream(new File(".\\user_pages\\" + name).getCanonicalFile());
		oos = new ObjectOutputStream(fos);
		oos.writeObject(page);
		oos.close();
	}
	
	/**
	 * Update the background color of a specified user's page.
	 * @param name of user with page to change background color of
	 * @param color hex of new background color
	 * @param invoker user name changing background color
	 * @throws Exception
	 */
	public static void updateBackgroundColor(final String name, 
			final String color, final String invoker) throws Exception
	{
		Page userPage = db.get(name);
		userPage.setBackgroundColor(color);
		db.add(userPage);
		
		if (DEBUG) 
			System.out.println("New Page with Background Color:\n" + userPage.getXML());
		
		//TODO: invoker information?
	}
	
	/**
	 * Change the log of a specified user's page.
	 * @param name of user with page to change log of
	 * @param log to put on user's page
	 * @param invoker user name changing the log and to note in log info
	 * @throws Exception
	 */
	public static void updateLog(final String name, 
			final String log, final String invoker) throws Exception
	{
		Page userPage = db.get(name);
		
		// update user page's log with new log and user changing the log
		userPage.setLog(log, "Logged By: " + invoker);
		db.add(userPage);
		
		if (DEBUG) System.out.println("New Page with Log:\n" + userPage.getXML());
	}
	
	/**
	 * Create or Update the HTML file of a user's page.
	 * Called after any page creation or modification.
	 * @param name of user to update HTML page for
	 * @throws Exception
	 */
	public static void updateUserHtml(final String name) throws Exception
	{
		// get xsl file and bytes
		RandomAccessFile raf;
		
		raf = new RandomAccessFile(
				new File(".\\page_stylesheet.xsl").getCanonicalFile(), "r");
		byte[] xslBytes = new byte[(int)raf.length()];
		raf.readFully(xslBytes);
		raf.close();
		
		// create xsl string reader to create stream source
		StringReader xslReader = new StringReader(new String(xslBytes));
		Source xslSource = new StreamSource(xslReader);
		
		// get user page and it's xml string to create string reader for stream source
		Page userPage = db.get(name);
		
		if (DEBUG) System.out.println("Got User XML:\n" + userPage.getXML());
		
		StringReader xmlReader = new StringReader(userPage.getXML());
		Source xmlSource = new StreamSource(xmlReader);
		
		// get bytes of html file from transforming xml with xsl file
		byte[] userHtmlBytes = transform(xmlSource, xslSource);
		
		if (DEBUG) System.out.println("New User HTML:\n" + new String(userHtmlBytes));
		
		// write bytes to file
		String fileName = String.format(".\\user_pages\\htmls\\%s.html", name);
		fos = new FileOutputStream(new File(fileName).getCanonicalFile());
		fos.write(userHtmlBytes);
		fos.close();
	}
	
	/**
	 * Transform xml with xsl stylesheet and return bytes of transformation.
	 * @param xmlSource of xml file
	 * @param xslSource of xsl file
	 * @return byte[] of transformation bytes
	 */
	private static byte[] transform(final Source xmlSource, 
			final Source xslSource) throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(bos);
		
		TransformerFactory fac = TransformerFactory.newInstance();
		Transformer trans = fac.newTransformer(xslSource);
		
		trans.transform(xmlSource, result);
		
		byte[] resultBytes = bos.toByteArray();
		return resultBytes;
	}
}
