package pages;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
/**
 * Implementation of user database remote object.
 * @author Alex Marasco, Joseph Stefanik
 *
 */
public class UserDatabaseImpl extends UnicastRemoteObject
							  implements UserDatabase 
{

	private static final long serialVersionUID = 1L;
	
	private Hashtable<String, Page> db;

	public UserDatabaseImpl() throws RemoteException 
	{
		db = new Hashtable<>();
	}

	@Override
	public void add(final Page page) throws RemoteException 
	{
		db.put(page.getUser(), page);
	}

	@Override
	public Page get(final String user) throws RemoteException 
	{
		return (Page)db.get(user);
	}

	@Override
	public void remove(final String user) throws RemoteException 
	{
		db.remove(user);
	}

}
