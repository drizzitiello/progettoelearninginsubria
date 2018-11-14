package socketDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SocketDb {
	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	static final String DB_URL = "jdbc:postgresql://localhost/postgres";
	static final String USER = "username";
	static final String PASS = "password";
	public static SocketDb socketDb;
	private Connection conn;
	private ResultSet rs;
	private static int nTentativi;
	private CallableStatement cs;
	
	private SocketDb() throws ClassNotFoundException, SQLException{
		createSql();
	}
	
	public static SocketDb getInstanceDb() throws ClassNotFoundException {
		if(socketDb==null) {
			try {
				socketDb=new SocketDb();
				nTentativi=0;
			} catch (SQLException e) {
				nTentativi++;
				if(nTentativi<=25)
				getInstanceDb();
				else
				return null;
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
		rs.close();
		cs.close();
		conn.close();
	}
	
	public ArrayList<Map<String,Object>> query(String sql, String[] params) throws ClassNotFoundException, SQLException {		
		createSql();
		params=checkParams(params);
		cs=conn.prepareCall(sql);
		for(int i=0; i<params.length;i++) {
			cs.setObject(i, params[i]);
		}
		cs.registerOutParameter(1, Types.OTHER);
		if(cs.execute()) {rs = (ResultSet) cs.getObject(1);}
		return getResults();
	}
	
	public ArrayList<Map<String,Object>> query(String sql) throws ClassNotFoundException, SQLException{
		return query(sql,null);
	}
	
	public ArrayList<Map<String,Object>> function(String funcName, String[] params) throws ClassNotFoundException, SQLException {
		String[] qmarks=new String[params.length];
		Arrays.fill(qmarks, '?');
		String sql= "{ ? = call "+funcName+"(" + String.join(", ", qmarks)+")}";
		return this.query(sql, params);
	}
	
	private ArrayList<Map<String,Object>> getResults() throws SQLException, ClassNotFoundException {
		ArrayList<Map<String, Object>> hm= new ArrayList<Map<String ,Object>>();
		ResultSetMetaData rsmd= rs.getMetaData();
		while(rs.next()) {
			Map<String, Object> m = new HashMap<String, Object>();
			for(int i=0; i<rsmd.getColumnCount();i++) {
				m.put(rsmd.getColumnName(i), rs.getObject(i));
				hm.add(m);
			}
		}
		destroySql();
		return hm;
	}
	
	public String[] checkParams(String[] params) {
		String[] paramsCorrected=new String[params.length];
		for(String p : params) {
			if(p.contains("drop")||p.contains("DROP")||(p.contains("delete")||p.contains("DELETE"))) {
				return null;
			}
		}
		return paramsCorrected;
	}
}