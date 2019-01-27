package socketDb;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import interfaces.RemoteInterface;

public class SocketDb extends UnicastRemoteObject
implements RemoteInterface {
	private static final String JDBC_DRIVER = "org.postgresql.Driver";  
	private static String DB_URL = "";
	private static String USER = "";
	private static String PASS = "";
	private static SocketDb socketDb;
	private static Connection conn;
	private static int nAttempts;
	private static PreparedStatement stmt;
	private static boolean isActive;
	private final static int max=30;
	private static int users;
	
	private SocketDb() throws ClassNotFoundException, SQLException, RemoteException{
		createSql();
	}
	
	private SocketDb(String host, String user, String password) throws ClassNotFoundException, SQLException, RemoteException{
		createSqlAdmin(host, user, password);
	}
	
	public static synchronized SocketDb getAdminInstanceDb(String host, String user, String password) throws ClassNotFoundException {
		users++;
		if(socketDb==null&&users<max) {
			try {
				socketDb=new SocketDb(host, user, password);
				nAttempts=0;
			} catch (SQLException e) {
				nAttempts++;
				if(nAttempts<=25)
					getInstanceDb();
				else
					return null;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return socketDb;
	}
	
	public static synchronized SocketDb getInstanceDb() throws ClassNotFoundException {
		users++;
		if(socketDb==null&&users<max) {
			try {
				socketDb=new SocketDb();
				nAttempts=0;
			} catch (SQLException e) {
				nAttempts++;
				if(nAttempts<=25)
					getInstanceDb();
				else
					return null;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return socketDb;
	}
	
	private synchronized void createSql() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName(JDBC_DRIVER);
		SocketDb.DB_URL="jdbc:postgresql://localhost:5432/sss";
		SocketDb.USER="postgres";
		SocketDb.PASS="makaay";
		if(users<max) conn = DriverManager.getConnection(DB_URL,USER,PASS);
		isActive=true;
	}
	
	private synchronized void createSqlAdmin(String host, String user, String password) throws ClassNotFoundException, SQLException, RemoteException {
		conn = null;
		Class.forName(JDBC_DRIVER);
		SocketDb.DB_URL="jdbc:postgresql://"+host+":5432/sss";
		SocketDb.USER=user;
		SocketDb.PASS=password;
		if(users<max) conn = DriverManager.getConnection(SocketDb.DB_URL,user,password);
		isActive=true;
	}
	
	public synchronized void destroySql() throws ClassNotFoundException, SQLException, RemoteException {
		stmt.close();
		conn.close();
		isActive=false;
	}
	
	private synchronized ArrayList<Map<String,Object>> executeSql(String sql) throws SQLException, ClassNotFoundException, RemoteException {
		ResultSet rs = null;
		if(sql.toUpperCase().startsWith("SELECT")) 
			rs = stmt.executeQuery();
		else stmt.execute();
		
		return rs==null ? null : getResults(rs);
	}
	
	public synchronized ArrayList<Map<String,Object>> query(String sql, Object[] params) throws ClassNotFoundException, SQLException, RemoteException {			
		users++;
		createSql();
		stmt=conn.prepareStatement(sql);
		bindParams(params);
		return executeSql(sql);
	}
	
	public synchronized ArrayList<Map<String,Object>> query(String sql) throws ClassNotFoundException, SQLException, RemoteException{
		return query(sql,new String[0]);
	}
	
	public synchronized ArrayList<Map<String,Object>> function(String funcName, Object[] params) throws ClassNotFoundException, SQLException, RemoteException {
		users++;
		createSql();
		String[] qmarks=new String[params.length];
		Arrays.fill(qmarks, "?");
		String sql= "SELECT * FROM "+funcName+"(" + String.join(", ", qmarks)+")";
		stmt=conn.prepareStatement(sql);
		bindParams(params);
		return executeSql(sql);
	}
	
	
	private synchronized ArrayList<Map<String,Object>> getResults(ResultSet objResults) throws SQLException, ClassNotFoundException, RemoteException {
		ArrayList<Map<String, Object>> hm= new ArrayList<Map<String ,Object>>();
		ResultSetMetaData rsmd= objResults.getMetaData();
		while(objResults.next()) {			
			Map<String, Object> m = new HashMap<String, Object>();
			for(int i=1; i<=rsmd.getColumnCount();i++) {
				m.put(rsmd.getColumnName(i).toLowerCase(), objResults.getObject(i));
			}
			hm.add(m);
		}
		objResults.close();
		destroySql();
		return hm;
	}
	
	private void bindParams(Object[] params) throws SQLException {
		for(int i=0; i<params.length;i++) {
			stmt.setObject(i+1, params[i]);
		}
	}

	@Override
	public boolean isActive() throws ClassNotFoundException, SQLException, RemoteException {
		return isActive;
	}
}