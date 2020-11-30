package main;

import client.*;
import server.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainDriver 
{
	private static final boolean DEBUG = false;
	
	public static void main(String[] args)
	{
		LoggerServer server = LoggerServer.getServer();
		ClientDriver client = ClientDriver.getClient();
		Registry registry;
		try 
		{
			registry = LocateRegistry.createRegistry(1099);
			
			server.start();
			Thread.sleep(5000);
			Thread clientThread = client.start();
			clientThread.join();
			server.stop();
			UnicastRemoteObject.unexportObject(registry, true);
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
