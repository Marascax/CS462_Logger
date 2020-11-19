package server;


import java.rmi.Naming;

import pages.UserDatabaseImpl;

public class LoggerServer 
{
	private static final String SERVERNAME = "//localhost:1109/";
	
	public static void main(String[] args)
	{
		UserDatabaseImpl db;
		
		System.setProperty("java.security.policy", "logger.policy");        
	    System.setSecurityManager(new SecurityManager());
	    
	    try
		{
			
			// create the RMI dispenser component and add it to the registry
			db = new UserDatabaseImpl();
			Naming.rebind(SERVERNAME + "UserDatabase", db);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
