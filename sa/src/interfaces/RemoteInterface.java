package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface RemoteInterface extends Remote{
	ArrayList<Map<String,Object>> query(String sql, Object[] params)throws ClassNotFoundException, SQLException, RemoteException;
	ArrayList<Map<String,Object>> query(String sql) throws ClassNotFoundException, SQLException, RemoteException;
	ArrayList<Map<String,Object>> function(String funcName, Object[] params) throws ClassNotFoundException, SQLException, RemoteException;
	void destroySql() throws ClassNotFoundException, SQLException, RemoteException;
	boolean isActive() throws ClassNotFoundException, SQLException, RemoteException;
} 