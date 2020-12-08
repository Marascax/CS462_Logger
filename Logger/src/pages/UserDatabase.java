package pages;

import java.rmi.RemoteException;
import java.util.Hashtable;
/**
 * Implementation of user database remote object.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class UserDatabase
{
	
	private Hashtable<String, Page> db;

	
	public UserDatabase()
	{
		db = new Hashtable<>();
	}

	/**
	 * Add user page to database.
	 * @param page to add
	 * @throws RemoteException
	 */
	public void add(final Page page)
	{
		db.put(page.getUser(), page);
	}

	/**
	 * Get user page of user with specified name.
	 * @param user name
	 * @return Page for user or null
	 * @throws RemoteException
	 */
	public Page get(final String user)
	{
		return (Page)db.get(user);
	}

	/**
	 * Remove user with specified name from user database.
	 * @param user name
	 * @throws RemoteException
	 */
	public void remove(final String user)
	{
		db.remove(user);
	}

}
