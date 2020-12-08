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
			
//			ServerRunner runner = new ServerRunner();
//			runner.start();
			
			System.out.println("Press [Enter] to stop the server...");

			// Block until the user presses [Enter]
			in.readLine();
			
//			runner.stop();
			
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
	
	static class ServerRunner implements Runnable
	{
		private ServerSocket serverSocket;
		private Socket socket;
		private String host;
		private int port;
		
		private BufferedReader br;		// read file path strings
		private FileInputStream fis;	// read file bytes
		private InputStream is;			// read from sockets
		
		private FileOutputStream fos;	// write to files
		
		private Thread thread;
		private volatile boolean keepRunning;
		
		ServerRunner()
		{
			host = "stu.cs.jmu.edu";
			port = 5026;
			try 
			{
				serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(5000);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		public void start()
		{
			if (thread == null)
			{
				thread = new Thread(this);
				keepRunning = true;
				thread.start();
			}
		}

		@Override
		public void run() 
		{
			while(keepRunning)
			{
				try
				{
					// accept connection
					socket = serverSocket.accept();
					
					// get socket input stream
					is = socket.getInputStream();
					
					// create input buffered reader for file path
					br = new BufferedReader(
							new InputStreamReader(is));
					
					// read in file path string
					String filePath = br.readLine();
					
					// create out stream to file path 
					fos = new FileOutputStream(filePath);
					
					// read all the bytes of file
					byte[] fileBytes = is.readAllBytes();
					
					// write bytes to file
					fos.write(fileBytes);
					fos.close();
					
					// close the connection
					socket.close();
					
					
				} catch (SocketTimeoutException stoe)
				{
					// no client waiting to connect, keep running in case someone does
				} catch (IOException ioe)
				{
					// socket forgot how to socket, try again
				} 
			}
			
		}
		
		public void stop()
		{
			keepRunning = false;
			thread.interrupt();
			thread = null;
		}
		
	}
	
}
