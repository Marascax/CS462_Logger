package server;


import java.nio.file.Paths;
import java.rmi.Naming;

import pages.UserDatabaseImpl;

public class LoggerServer 
{
	private static final String SERVERNAME = "//localhost:1099/";
	
	public static void main(String[] args) throws Exception
	{
		UserDatabaseImpl db;
		String policyPath = Paths.get(LoggerServer.class.getResource("logger.policy").toURI()).toString();
		System.setProperty("java.security.policy", policyPath);        
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
