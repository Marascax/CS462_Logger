package server;


import java.nio.file.Paths;
import java.rmi.Naming;

import pages.UserDatabaseImpl;

/**
 * Program for the server.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class LoggerServer 
{
	private static final String SERVERNAME = "//localhost:1099/";
	
	public static void main(final String[] args) throws Exception
	{
		UserDatabaseImpl db;
		String policyPath = Paths.get(
				LoggerServer.class.getResource("logger.policy").toURI()).toString();
		System.setProperty("java.security.policy", policyPath);
		System.setSecurityManager(new SecurityManager());
		// create the RMI dispenser component and add it to the registry
		db = new UserDatabaseImpl();
		Naming.rebind(SERVERNAME + "UserDatabase", db);
	}

}
