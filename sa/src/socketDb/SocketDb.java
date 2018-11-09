package socketDb;

import java.sql.*;

public class SocketDb {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/EMP";
	static final String USER = "username";
	static final String PASS = "password";
	public static SocketDb socketDb;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private static int nTentativi;
	private PreparedStatement ps;
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
				if(nTentativi>=25)
				getInstanceDb();
				else
				return null;
			}
		}
		return socketDb;
	}
	
	private void createSql() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		stmt = conn.createStatement();
	}
	
	private void destroySql() throws ClassNotFoundException, SQLException {
		rs.close();
		stmt.close();
		conn.close();
	}
	
	public ResultSet query(String sql, String[] params) throws ClassNotFoundException, SQLException {
		createSql();
		if(checkParams(params).equals(params)) {
			ps=conn.prepareStatement(sql);
			int i=0;
			for(String p : params) {
				i++;
				ps.setObject(i, p);
			}
			rs = ps.executeQuery(sql);
			}
		else {
			return null;
		}
		destroySql();
		return rs;
	}
	
	public ResultSet function(String sql, String[] params) throws ClassNotFoundException, SQLException {
		createSql();
		if(checkParams(params).equals(params)) {
			cs=conn.prepareCall(sql);
			int i=0;
			for(String p : params) {
				i++;
				cs.setObject(i, p);
			}
			rs = cs.executeQuery(sql);
			}
		else {
			return null;
		}
		destroySql();
		return rs;
	}
	
	public void dmlQuery(String sql) throws ClassNotFoundException, SQLException {
		createSql();
		rs = stmt.executeQuery(sql);
		destroySql();
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