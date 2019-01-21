import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import Sessione.Sessione;
import UserManager.UserManager;
import Utente.Utente;
import Utente.Utente.InfoUtente;
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
		ses.create(2);
		
		System.out.println(ses.getUtente().getInfo().nome);
		System.out.println(ses.getUtente().getInfo().cognome);
		System.out.println(ses.getUtente().getInfo().corsoLaurea);
		System.out.println(ses.getUtente().getInfo().email);
		System.out.println(ses.getUtente().getInfo().statoCarriera);
		System.out.println(ses.getUtente().getInfo().strutturaRiferimento);
		System.out.println(ses.getUtente().getInfo().tipoUtente);
		System.out.println(ses.getUtente().getInfo().annoImmatricolazione);
		System.out.println(ses.getUtente().getInfo().matricola);
		
		UserManager um = new UserManager();
		Utente user = new Utente();
		
		/*String path="C:/Users/macri/Desktop/csvimportutenti.txt";
		um.csvImportUtente(path);*/
		
		/*InfoUtente info = new InfoUtente();
		info.annoImmatricolazione=2001;
		info.nome="mak";
		info.cognome="mac";
		info.corsoLaurea="Informatica";
		info.email="asda";
		info.matricola=19;
		info.statoCarriera="2 anno";
		info.strutturaRiferimento=null;
		info.tipoUtente=1;
		um.creaUtente(info);*/
		
		user.createFromMatricola(34);
		System.out.println(user.getInfo().nome);
		System.out.println(user.getInfo().cognome);
		System.out.println(user.getInfo().corsoLaurea);
		System.out.println(user.getInfo().email);
		System.out.println(user.getInfo().statoCarriera);
		System.out.println(user.getInfo().strutturaRiferimento);
		System.out.println(user.getInfo().tipoUtente);
		System.out.println(user.getInfo().annoImmatricolazione);
		System.out.println(user.getInfo().matricola);
		user.createFromMatricola(13);
		System.out.println(user.getInfo().nome);
		System.out.println(user.getInfo().cognome);
		System.out.println(user.getInfo().corsoLaurea);
		System.out.println(user.getInfo().email);
		System.out.println(user.getInfo().statoCarriera);
		System.out.println(user.getInfo().strutturaRiferimento);
		System.out.println(user.getInfo().tipoUtente);
		System.out.println(user.getInfo().annoImmatricolazione);
		System.out.println(user.getInfo().matricola);
		//System.out.println(um.modificaDatiUtente(user));
		
		/*GlobalAnalytics cs = new GlobalAnalytics();
		Map<Integer, Integer> hh = cs.downloadsPerCorso();// downloadsIntervallo("2019-01-20 16:20", "2019-01-20 16:35");
		
		for(Integer m : hh.keySet()) {
			
			System.out.print(m);
			System.out.print("  ");
			System.out.println(hh.get(m));
		}*/
		
		ses.destroy();
	}
}
