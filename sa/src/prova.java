import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import analytics.CourseAnalytics;
import analytics.GlobalAnalytics;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;
import session.Session;
import socketDb.SocketDb;
import user.User;
import user.User.UserInfo;
import userManager.UserManager;

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
		
		FindCourse fc = new FindCourse();
		Resource r = new Resource(null, null, "", nTentativi, nTentativi, false, null);
		fc.download(r);
		
		/*Session ses = Session.getInstance();
		ses.create(2);
		
		System.out.println(ses.getUser().getInfo().name);
		System.out.println(ses.getUser().getInfo().surname);
		System.out.println(ses.getUser().getInfo().corsoLaurea);
		System.out.println(ses.getUser().getInfo().email);
		System.out.println(ses.getUser().getInfo().careerStatus);
		System.out.println(ses.getUser().getInfo().strutturaRiferimento);
		System.out.println(ses.getUser().getInfo().userType);
		System.out.println(ses.getUser().getInfo().registrationYear);
		System.out.println(ses.getUser().getInfo().student_number);
		
		UserManager um = new UserManager();
		User user = new User();*/
		
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
		
		/*user.createFromStudentNumber(34);
		System.out.println(user.getInfo().name);
		System.out.println(user.getInfo().surname);
		System.out.println(user.getInfo().corsoLaurea);
		System.out.println(user.getInfo().email);
		System.out.println(user.getInfo().careerStatus);
		System.out.println(user.getInfo().strutturaRiferimento);
		System.out.println(user.getInfo().userType);
		System.out.println(user.getInfo().registrationYear);
		System.out.println(user.getInfo().student_number);
		user.createFromStudentNumber(13);
		System.out.println(user.getInfo().name);
		System.out.println(user.getInfo().surname);
		System.out.println(user.getInfo().corsoLaurea);
		System.out.println(user.getInfo().email);
		System.out.println(user.getInfo().careerStatus);
		System.out.println(user.getInfo().strutturaRiferimento);
		System.out.println(user.getInfo().userType);
		System.out.println(user.getInfo().registrationYear);
		System.out.println(user.getInfo().student_number);*/
		//System.out.println(um.modificaDatiUtente(user));
		
		/*GlobalAnalytics cs = new GlobalAnalytics();
		Map<Integer, Integer> hh = cs.downloadsPerCorso();// downloadsIntervallo("2019-01-20 16:20", "2019-01-20 16:35");
		
		for(Integer m : hh.keySet()) {
			
			System.out.print(m);
			System.out.print("  ");
			System.out.println(hh.get(m));
		}*/
		
		//ses.destroy();
	}
}
