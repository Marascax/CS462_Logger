package pages;

import java.rmi.*;

public interface UserDatabase extends Remote
{
	public void add(Page page) throws RemoteException;
	
	public Page get(String user) throws RemoteException;
	
	public void remove(String user) throws RemoteException;
}
