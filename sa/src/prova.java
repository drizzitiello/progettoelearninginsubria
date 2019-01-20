import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import Sessione.Sessione;
import analytics.CorsoAnalytics;
import analytics.GlobalAnalytics;
import socketDb.SocketDb;

public class prova {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:postgresql://localhost/dbdb";
	static final String USER = "postgres";
	static final String PASS = "makaay";
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	private static int nTentativi;
	private PreparedStatement ps;
	private static CallableStatement cs;
	public static void main(String[] args) throws Exception {
		SocketDb s = SocketDb.getInstanceDb();
		
		if(s == null) {
			System.err.println("ERRORE");
			return;
		}
		
		Sessione ses = Sessione.getInstance();
		ses.create(13);
		
		GlobalAnalytics cs = new GlobalAnalytics();
		Map<Integer, Integer> hh = cs.downloadsPerCorso();// downloadsIntervallo("2019-01-20 16:20", "2019-01-20 16:35");
		
		for(Integer m : hh.keySet()) {
			
			System.out.print(m);
			System.out.print("  ");
			System.out.println(hh.get(m));
		}
		
		ses.destroy();
	}
}
