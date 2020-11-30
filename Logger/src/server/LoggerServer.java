package server;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import pages.UserDatabaseImpl;

/**
 * Program for the server.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class LoggerServer implements Runnable
{
	private static final boolean DEBUG = false;
	private static final String SERVERNAME = "//localhost:1099/";
	private static LoggerServer instance;
	
	private Thread thread;
	
	private LoggerServer()
	{
		String policyPath;
		try 
		{
			policyPath = Paths.get(
					LoggerServer.class.getResource("logger.policy").toURI()).toString();
			System.setProperty("java.security.policy", policyPath);
			
		} catch (URISyntaxException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.setSecurityManager(new SecurityManager());
		
	}
	
	public static LoggerServer getServer()
	{
		if (instance == null) instance = new LoggerServer();
		return instance;
	}
	
	public void start()
	{
		if (thread == null)
		{
			if (DEBUG) System.out.println("Server creating thread...");
			
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() 
	{
		UserDatabaseImpl db;
		// create the RMI dispenser component and add it to the registry
		try 
		{
			db = new UserDatabaseImpl();
			
			if (DEBUG) System.out.println("Getting Registry...");
			
			Registry reg = LocateRegistry.getRegistry(1099);
			
			if (DEBUG) System.out.println("Rebinding DB...");
			
			reg.rebind(SERVERNAME + "UserDatabase", db);
			
			if (DEBUG) System.out.println("Bound...");
//			Naming.rebind(SERVERNAME + "UserDatabase", db);
		} catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void stop()
	{
		thread.interrupt();
		thread = null;
	}

}
