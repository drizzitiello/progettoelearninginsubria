import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import interfaccia.RemoteInterface;
import socketDb.SocketDb;

public class MultiServer {
	public static void main(String[] args)
	 {
	  try
	  {
		  RemoteInterface server = SocketDb.getInstanceDb();
	   Naming.rebind("rmi://localhost/SocketDb",server);
	  }
	  catch (RemoteException e){e.printStackTrace( );}
	  catch (MalformedURLException e) {e.printStackTrace( );}
	  catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	 }
	 
}
