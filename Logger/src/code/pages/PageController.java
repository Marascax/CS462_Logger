package pages;

import java.io.ByteArrayOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public interface PageController extends Remote
{
	/**
	 * Check user database for user with specified name.
	 * @param name of user to check for
	 * @return boolean indicating if user with name is in database
	 * @throws Exception
	 */
	public boolean checkForUser(final String name) throws RemoteException;
	
	/**
	 * Create and add user page for user with specified name.
	 * Write new user page to file.
	 * @param name of new user
	 * @throws Exception
	 */
	public void createUserPage(final String name) throws RemoteException;
	
	/**
	 * Update the background color of a specified user's page.
	 * @param name of user with page to change background color of
	 * @param color hex of new background color
	 * @param invoker user name changing background color
	 * @throws Exception
	 */
	public void updateBackgroundColor(final String name, 
			final String color, final String invoker) throws RemoteException;
	
	/**
	 * Change the log of a specified user's page.
	 * @param name of user with page to change log of
	 * @param log to put on user's page
	 * @param invoker user name changing the log and to note in log info
	 * @throws Exception
	 */
	public void updateLog(final String name, 
			final String log, final String invoker) throws RemoteException;
	
	/**
	 * Create or Update the HTML file of a user's page.
	 * Called after any page creation or modification.
	 * @param name of user to update HTML page for
	 * @throws Exception
	 */
	public void updateUserHtml(final String name) throws RemoteException;
	
	/**
	 * Transform xml with xsl stylesheet and return bytes of transformation.
	 * @param xmlSource of xml file
	 * @param xslSource of xsl file
	 * @return byte[] of transformation bytes
	 */
	default byte[] transform(final Source xmlSource, 
			final Source xslSource) throws RemoteException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(bos);
		
		TransformerFactory fac = TransformerFactory.newInstance();
		try
		{
			Transformer trans = fac.newTransformer(xslSource);
		
			trans.transform(xmlSource, result);
		} catch (Exception e)
		{
			throw new RemoteException();
		}
		
		byte[] resultBytes = bos.toByteArray();
		return resultBytes;
	}

}
