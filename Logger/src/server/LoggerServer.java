package server;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import pages.Page;
import pages.PageControllerImpl;
import pages.UserDatabase;

/**
 * Program for the server.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class LoggerServer
{
	private static final boolean DEBUG = false;
	private static FileInputStream fis;		// file input for user pages
	private static ObjectInputStream ois;	// object input to read in serialized user pages
	private static FileOutputStream fos;	// file output for user pages and htmls
	
	public static void main(String[] args)
	{
		String policyPath;
		Registry registry;
		try 
		{
			policyPath = Paths.get(
					LoggerServer.class.getResource("logger.policy").toURI()).toString();
			System.setProperty("java.security.policy", policyPath);
//			System.setProperty("java.rmi.server.hostname", "stu.cs.jmu.edu");
			System.setSecurityManager(new SecurityManager());
			
			registry = LocateRegistry.createRegistry(5025);
			
			UserDatabase db = new UserDatabase();
			
			// get the directory with the already made user pages, add them to user database
			File pagesDir = new File("./user_pages").getCanonicalFile();
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
			
			PageControllerImpl pageController = new PageControllerImpl(db);
			registry.rebind("PageController", pageController);
//			Naming.rebind("//stu.cs.jmu.edu:5025/PageController", pageController);
			
			System.out.println(InetAddress.getLocalHost().getHostName());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Press [Enter] to stop the server...");

			// Block until the user presses [Enter]
			in.readLine();
			
			UnicastRemoteObject.unexportObject(registry, true);
			
		} catch (URISyntaxException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
		
	}
	
	
}
