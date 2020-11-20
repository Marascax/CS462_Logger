package pages;

import java.rmi.*;

/**
 * Remote interface of user database to hold user pages.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public interface UserDatabase extends Remote
{
	/**
	 * Add user page to database.
	 * @param page to add
	 * @throws RemoteException
	 */
	public void add(final Page page) throws RemoteException;
	
	/**
	 * Get user page of user with specified name.
	 * @param user name
	 * @return Page for user or null
	 * @throws RemoteException
	 */
	public Page get(final String user) throws RemoteException;
	
	/**
	 * Remove user with specified name from user database.
	 * @param user name
	 * @throws RemoteException
	 */
	public void remove(final String user) throws RemoteException;
}
