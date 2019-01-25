package socketDb;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import interfaccia.RemoteInterface;

public class SocketDb extends UnicastRemoteObject
implements RemoteInterface {
	private static final String JDBC_DRIVER = "org.postgresql.Driver";  
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/sss";
	private static final String USER = "postgres";
	private static final String PASS = "makaay";
	public static SocketDb socketDb;
	private Connection conn;
	private static int nAttempts;
	private PreparedStatement stmt;
	
	private SocketDb() throws ClassNotFoundException, SQLException, RemoteException{
		createSql();
	}
	
	public static SocketDb getInstanceDb() throws ClassNotFoundException {
		if(socketDb==null) {
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
	
	private void createSql() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
	}
	
	private void destroySql() throws ClassNotFoundException, SQLException {
		stmt.close();
		conn.close();
	}
	
	private ArrayList<Map<String,Object>> executeSql(String sql) throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
		if(sql.toUpperCase().startsWith("SELECT")) 
			rs = stmt.executeQuery();
		else stmt.execute();
		
		return rs==null ? null : getResults(rs);
	}
	
	public ArrayList<Map<String,Object>> query(String sql, Object[] params) throws ClassNotFoundException, SQLException {			
		createSql();
		stmt=conn.prepareStatement(sql);
		bindParams(params);
		return executeSql(sql);
	}
	
	public ArrayList<Map<String,Object>> query(String sql) throws ClassNotFoundException, SQLException{
		return query(sql,new String[0]);
	}
	
	public ArrayList<Map<String,Object>> function(String funcName, Object[] params) throws ClassNotFoundException, SQLException {
		createSql();
		String[] qmarks=new String[params.length];
		Arrays.fill(qmarks, "?");
		String sql= "SELECT * FROM "+funcName+"(" + String.join(", ", qmarks)+")";
		stmt=conn.prepareStatement(sql);
		bindParams(params);
		return executeSql(sql);
	}
	
	
	private ArrayList<Map<String,Object>> getResults(ResultSet objResults) throws SQLException, ClassNotFoundException {
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
}