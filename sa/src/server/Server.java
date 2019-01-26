package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;

import interfaces.RemoteInterface;
import socketDb.SocketDb;

public class Server {
	private RemoteInterface server;
	private SocketDb adminInstanceDb;
	public Server(SocketDb adminInstanceDb) {
		try{
			this.adminInstanceDb=adminInstanceDb;
			  server = adminInstanceDb;
		   Naming.rebind("rmi://localhost/SocketDb",server);
		  }
		  catch (RemoteException e){e.printStackTrace( );}
		  catch (MalformedURLException e) {e.printStackTrace( );}
	}

	public void close() throws ClassNotFoundException, RemoteException, SQLException {
		SocketDb.getInstanceDb().destroySql();
	}

	public boolean isActive() throws ClassNotFoundException, RemoteException, SQLException {
		return adminInstanceDb.isActive();
	}
}
