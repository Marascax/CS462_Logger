package pages;

import java.rmi.Naming;
import java.io.*;

public class PageController 
{
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
		for (File userFile : pagesDir.listFiles()) db.add(new Page(userFile.getName()));
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
		
		//TODO: invoker information?
	}
	
	public static void updateLog(String name, String log, String invoker) throws Exception
	{
		Page userPage = db.get(name);
		
		// update user page's log with new log and user changing the log
		userPage.setLog(log, invoker);
	}
}
