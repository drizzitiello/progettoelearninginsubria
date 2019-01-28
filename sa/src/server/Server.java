package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import socketDb.SocketDb;

public class Server extends UnicastRemoteObject
implements AnotherInterface{
	private RemoteInterface server;
	public SocketDb adminInstanceDb;
	
	private static Server singleton;
	public static int connessioni;
	public static int registry;
	
	private Server(SocketDb adminInstanceDb) throws RemoteException{
		this.adminInstanceDb=adminInstanceDb;
		server = adminInstanceDb;
		connessioni = 0;
		registry = 2000;
		createStub();
	}

	public static Server getInstance(SocketDb adminInstanceDb) throws RemoteException {
		if(singleton==null) {
			singleton = new Server(adminInstanceDb);
		}
		return singleton;
	}

	public void connectionIncremented() {
		connessioni++;
	}
	
	private void createStub() {
		try {
			if(registry>2100) {
				registry=2000;
			}
			Registry reg = LocateRegistry.createRegistry(registry);
			reg.rebind("SocketDb", server); 
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void close() throws ClassNotFoundException, RemoteException, SQLException {
		SocketDb.getInstanceDb().destroySql();
	}

	public boolean isActive() throws ClassNotFoundException, RemoteException, SQLException {
		return adminInstanceDb.isActive();
	}

	public void bind() {
		if(connessioni>20) {
			System.out.println("+");
			connessioni=0;
			registry++;
			createStub();
		}
	}

	@Override
	public int getRegistry() throws RemoteException {
		return registry;
	}
	
	public void sta() {
		try {
			connessioni++;
			System.out.println(connessioni);
			bind();
			AnotherInterface ai = singleton;
			Naming.rebind("rmi://localhost/Server",ai);
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void starting() throws RemoteException{
		connessioni++;
		System.out.println(connessioni);
		bind();
	}
}
