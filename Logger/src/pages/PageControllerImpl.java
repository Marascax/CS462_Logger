package pages;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.*;
import java.io.*;
import java.net.Socket;

/**
 * Class controlling interactions with User pages.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class PageControllerImpl extends UnicastRemoteObject
								implements PageController
{
	
	private static final long serialVersionUID = 1L;

	private static final boolean DEBUG = true;
	
	private static FileInputStream fis;		// file input for user pages
	private static ObjectInputStream ois;	// object input to read in serialized user pages
	private static FileOutputStream fos;	// file output for user pages and htmls
	
	private UserDatabase db;				// user database for each user page
	private Socket socket;
	
	
	// object output to write serialized user pages and user htmls
	private static ObjectOutputStream oos;
	
	public PageControllerImpl(UserDatabase db) throws RemoteException
	{
		this.db = db;
		
	}
	
	@Override
	public boolean checkForUser(final String name) throws RemoteException
	{
		return db.get(name) != null;
	}
	
	@Override
	public void createUserPage(final String name) throws RemoteException
	{
		Page page = new Page(name);
		db.add(page);
		
		// write user page to directory with other user pages
		try 
		{
			fos = new FileOutputStream(new File("./user_pages/" + name).getCanonicalFile());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(page);
			oos.close();
		} catch (Exception e) 
		{
			throw new RemoteException();
		}
		
	}
	
	/**
	 * Update the background color of a specified user's page.
	 * @param name of user with page to change background color of
	 * @param color hex of new background color
	 * @param invoker user name changing background color
	 * @throws Exception
	 */
	public void updateBackgroundColor(final String name, 
			final String color, final String invoker) throws RemoteException
	{
		
		Page userPage = db.get(name);
		userPage.setBackgroundColor(color);
		db.add(userPage);
		
		if (DEBUG) 
			System.out.println("New Page with Background Color:\n" + userPage.getXML());
		
		//TODO: invoker information?
	}
	
	@Override
	public void updateLog(final String name, 
			final String log, final String invoker) throws RemoteException
	{
		Page userPage = db.get(name);
		
		// update user page's log with new log and user changing the log
		userPage.setLog(log, "Logged By: " + invoker);
		db.add(userPage);
		
		if (DEBUG) System.out.println("New Page with Log:\n" + userPage.getXML());
	}
	
	@Override
	public void updateUserHtml(final String name) throws RemoteException
	{
		// get xsl file and bytes
		RandomAccessFile raf;
		byte[] xslBytes, userHtmlBytes;
		StringReader xslReader, xmlReader;
		Source xslSource, xmlSource;
		Page userPage;
		String fileName;
		try
		{
			raf = new RandomAccessFile(
				new File("./page_stylesheet.xsl").getCanonicalFile(), "r");
			xslBytes = new byte[(int)raf.length()];
			raf.readFully(xslBytes);
			raf.close();
			
			// create xsl string reader to create stream source
			xslReader = new StringReader(new String(xslBytes));
			xslSource = new StreamSource(xslReader);
			
			// get user page and it's xml string to create string reader for stream source
			userPage = db.get(name);
			
			if (DEBUG) System.out.println("Got User XML:\n" + userPage.getXML());
			
			xmlReader = new StringReader(userPage.getXML());
			xmlSource = new StreamSource(xmlReader);
			
			// get bytes of html file from transforming xml with xsl file
			userHtmlBytes = transform(xmlSource, xslSource);
			
			if (DEBUG) System.out.println("New User HTML:\n" + new String(userHtmlBytes));
			
			// write bytes to file
			fileName = String.format("./user_pages/htmls/%s.html", name);
			fos = new FileOutputStream(new File(fileName).getCanonicalFile());
			fos.write(userHtmlBytes);
			fos.close();
		} catch (Exception e)
		{
			throw new RemoteException();
		}
		
	}
	
}
