package client;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import pages.PageController;

/**
 * Driver for program when used by user client.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class ClientDriver
{
	private static final boolean DEBUG = true;
	private static final String INVALID_USER_ERROR = 
			"Invalid User: Other user not found or current user\n";
	
	private static boolean keepRunning;
	private static String currUser;
	private static PageController pageController;
	
	public static void main(String[] args)
	{
		if (DEBUG) System.out.println("User Client Started...");
		System.setProperty("java.security.policy", "logger.policy");
		try
		{
			// make sure page controller gets the database from the RMI registry
			Registry reg = LocateRegistry.getRegistry(5025);
			pageController = (PageController)reg.lookup("PageController");
			if (DEBUG) System.out.println("User got page controller...");
			// get the name of the current user
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please Enter Name of Current User:\n");
			currUser = in.readLine();
			
			// check if user has a page, if not, create one for user
			if (!pageController.checkForUser(currUser))
			{
				pageController.createUserPage(currUser);
			}
			
			String options = "Menu:\n"
					+ "1. Set Background Color of User Page\n"
					+ "2. Set Log of User Page\n"
					+ "3. Exit Program\n";
			
			String input, user;
			int selection = -1;
			keepRunning = true;
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
						System.out.println(">[Set Background Color]\n");
						
						System.out.println("User For Background Color Change:\n");
						input = in.readLine();
						
						// check for other user
						if (!pageController.checkForUser(input) 
								|| input.equals(currUser))
						{
							System.out.println(INVALID_USER_ERROR);
							break;
						} else
						{
							user = input;
							System.out.println(
									"Enter New Background Color"
									+ " (#XXXXXX):\n");
							input = in.readLine();
							pageController.updateBackgroundColor(user, 
									input, currUser);
							pageController.updateUserHtml(user);
						}
						
						break;
					// log
					case 2:
						System.out.println(">[Set Log]\n");
						
						System.out.println("User For Log Change:\n");
						input = in.readLine();
						
						// check for other user
						if (!pageController.checkForUser(input) 
								|| input.equals(currUser))
						{
							System.out.println(INVALID_USER_ERROR);
							break;
						} else
						{
							user = input;
							System.out.println("Enter New User Log:\n");
							input = in.readLine();
							pageController.updateLog(user, input, currUser);
							pageController.updateUserHtml(user);
						}
						
						break;
					// exit
					case 3:
						System.out.println("Exiting program...\n");
						Thread.sleep(1000);
						keepRunning = false;
						break;
					default:
						System.out.println(
								"Cannot process selection,"
								+ " please try again...\n");
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
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
