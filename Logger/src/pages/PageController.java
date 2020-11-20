package pages;

import java.rmi.Naming;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.*;

import org.xml.sax.InputSource;

import java.io.*;

public class PageController 
{
	
	private static final boolean DEBUG = true;
	
	private static UserDatabase db;
	private static FileInputStream fis;
	private static ObjectInputStream ois;
	private static FileOutputStream fos;
	private static ObjectOutputStream oos;
	
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
			
			if (DEBUG && userPage != null) System.out.println("Loaded user page for " + userPage.getUser());
		}
	}
	
	public static boolean checkForUser(String name) throws Exception
	{
		return db.get(name) != null;
	}
	
	public static void createUserPage(String name) throws Exception
	{
		Page page = new Page(name);
		db.add(page);
		
		// write user page to directory with other user pages
		fos = new FileOutputStream(new File(".\\user_pages\\" + name).getCanonicalFile());
		oos = new ObjectOutputStream(fos);
		oos.writeObject(page);
		oos.close();
	}
	
	public static void updateBackgroundColor(String name, String color, String invoker) throws Exception
	{
		Page userPage = db.get(name);
		userPage.setBackgroundColor(color);
		db.add(userPage);
		
		if (DEBUG) System.out.println("New User XML:\n" + userPage.getXML());
		
		//TODO: invoker information?
	}
	
	public static void updateLog(String name, String log, String invoker) throws Exception
	{
		Page userPage = db.get(name);
		
		// update user page's log with new log and user changing the log
		userPage.setLog(log, "Logged By: " + invoker);
		db.add(userPage);
		
		if (DEBUG) System.out.println("New User XML:\n" + userPage.getXML());
	}
	
	public static void updateUserHtml(String name) throws Exception
	{
		// get xsl file and bytes
		RandomAccessFile raf = new RandomAccessFile(new File(".\\page_stylesheet.xsl").getCanonicalFile(), "r");
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
	 */
	private static byte[] transform(Source xmlSource, Source xslSource) throws Exception
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
