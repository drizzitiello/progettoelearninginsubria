package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AnotherInterface extends Remote{
	int getRegistry() throws RemoteException;
	void starting() throws RemoteException;
}
