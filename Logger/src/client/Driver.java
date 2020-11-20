package client;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import pages.Page;
import pages.PageController;
import pages.UserDatabase;

public class Driver 
{
	private static boolean keepRunning;
	private static String currUser;
	
	public static void main(String[] args) throws Exception
	{
		keepRunning = true;
		
		// make sure page controller gets the database from the RMI registry
		PageController.initializeUserDatabase();
		
		// get the name of the current user
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please Enter Name of Current User:");
		currUser = in.readLine();
		
		// check if user has a page, if not, create one for user
		if (!PageController.checkForUser(currUser))
		{
			PageController.createUserPage(currUser);
		}
		
		String options = "Menu:\n"
				+ "1. Set Background Color of User Page\n"
				+ "2. Set Log of User Page\n"
				+ "3. Exit Program";
		
		String input, user;
		int selection = -1;
		while(keepRunning)
		{
			// get action from user
			System.out.println(options);
			input = in.readLine();
			selection = Integer.parseInt(input);
			
			switch(selection)
			{
				// background color
				case 1:
					System.out.println(">[Set Background Color]");
					
					System.out.println("Enter Name of Other User:\n");
					input = in.readLine();
					
					// check for other user
					if (!PageController.checkForUser(input))
					{
						System.out.println("Other user not found");
						break;
					} else
					{
						user = input;
						System.out.println("Enter New Background Color (#XXXXXX):\n");
						input = in.readLine();
						PageController.updateBackgroundColor(user, input, currUser);
					}
					
					break;
				// log
				case 2:
					System.out.println(">[Set Log]");
					
					System.out.println("Enter Name of Other User:\n");
					input = in.readLine();
					
					// check for other user
					if (!PageController.checkForUser(input))
					{
						System.out.println("Other user not found");
						break;
					} else
					{
						user = input;
						System.out.println("Enter New User Log:\n");
						input = in.readLine();
						PageController.updateLog(user, input, currUser);
					}
					
					break;
				// exit
				case 3:
					System.out.println("Exiting program...");
					Thread.sleep(1000);
					keepRunning = false;
					break;
				default:
					System.out.println("Cannot process selection, please try again...");
					break;
			}
			
			if (selection != 3 && selection != -1)
			{
				System.out.println("Press [Enter] to Continue...");
				in.readLine();
//				System.out.print("\033[H\033[2J");
//				System.out.flush();
			}
		}
		
		
		
		System.setProperty("java.security.policy", "logger.policy");  
	}

}
