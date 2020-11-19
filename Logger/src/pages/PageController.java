package pages;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PageController 
{
	private static UserDatabase db;
	
	public static void initializeUserDatabase() throws Exception
	{
		db = (UserDatabase)Naming.lookup("rmi://localhost:1099/UserDatabase");
	}
	
	public static boolean checkForUser(String name) throws Exception
	{
		return db.get(name) != null;
	}
	
	public static void createUserPage(String name) throws Exception
	{
		Page page = new Page(name);
		db.add(page);
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
