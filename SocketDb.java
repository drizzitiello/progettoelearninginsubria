package socketDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import gestioneContenutiCorso.Contenuto;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;

public class SocketDb {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/EMP";
	static final String USER = "username";
	static final String PASS = "password";
	public static SocketDb socketDb;
	private Connection conn;
	private Statement stmt;
	
	private SocketDb() {
		
	}
	
	public static SocketDb getInstanceDb() throws ClassNotFoundException, SQLException {
		if(socketDb==null) {
			socketDb=new SocketDb();
		}
		return socketDb;
	}
	
	private void createSql() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		stmt = conn.createStatement();
	}
	
	public List<String> getEmailUtenti(int codCorso) throws Exception {
		List<String> l=new ArrayList<String>();
		createSql();
		String sql;
		sql = "SELECT getEmailUtenti("+codCorso+")";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			String email=rs.getString("email");
			l.add(email);
		   }
		rs.close();
		stmt.close();
		conn.close();
		return l;
	}
	
	public List<String> getCorsi() throws ClassNotFoundException, SQLException {
		List<String> l=new ArrayList<String>();
		createSql();
		String sql;
		sql = "SELECT getCorsi()";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			String corso=rs.getString("email");
			l.add(corso);
		   }
		rs.close();
		stmt.close();
		conn.close();
		return l;
	}

	public void setContenuti(Contenuto cont, Corso c) throws ClassNotFoundException, SQLException {
		createSql();
		String sql,sql2;
		List<Sezione> sezioni=new ArrayList<Sezione>();
		List<String> titoli=new ArrayList<String>();
		List<String> descrizioni=new ArrayList<String>();
		List<Boolean> visibilita=new ArrayList<Boolean>();
		List<Integer> codiciCorso=new ArrayList<Integer>();
		List<Integer> PadreDi=new ArrayList<Integer>();
		List<Risorse> risorse=new ArrayList<Risorse>();
		List<String> nomi=new ArrayList<String>();
		List<String> descrizioni2=new ArrayList<String>();
		List<Boolean> paths=new ArrayList<Boolean>();
		List<Integer> codiciSezione=new ArrayList<Integer>();
		while(cont.hasMoreSections()) {
			Sezione sezione=cont.nextSection();
			sezioni.add(sezione);
			titoli.add(sezione.titolo);
			descrizioni.add(sezione.descrizione);
			visibilita.add(sezione.visibilita);
			codiciCorso.add(sezione.codCorso);
			PadreDi.add(sezione.PadreDi);
			while(sezione.hasMoreResources()) {
				Risorse risorsa=sezione.nextResource();
				risorse.add(risorsa);
				nomi.add(sezione.titolo);
				descrizioni.add(sezione.descrizione);
				paths.add(sezione.visibilita);
				codiciSezione.add(sezione.codCorso);
			}
		}
			sql = "INSERT INTO Sezione(Titolo, Descrizione, isPubblica, CodCorso, PadreDi) "
					+ "VALUES( "+titoli+", "+descrizioni+", "+visibilita+", "+codiciCorso+", "+PadreDi+")";
			ResultSet rs = stmt.executeQuery(sql);
			sql2 = "INSERT INTO Risorsa(Nome, Descrizione, Path, CodSezione) "
					+ "VALUES( "+nomi+", "+descrizioni2+", "+paths+", "+codiciSezione+")";
			rs = stmt.executeQuery(sql2);
			rs.close();
		    stmt.close();
		    conn.close();
	}

	public Contenuto getContenutoCorso(Corso c) throws ClassNotFoundException, SQLException {
		Contenuto cont=new Contenuto();
		createSql();
		String sql,sql2;
		sql = "SELECT codSezione, descrizione"
				+ " FROM Sezione JOIN Corso ON codCorso"
				+ " WHERE codCorso="+c.codCorso;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int codSezione=rs.getInt("codSezione");
			String descr=rs.getString("descrizione");
			String titolo=rs.getString("titolo");
			Boolean visibilita=rs.getBoolean("visibilita");
			sql2 = "SELECT descrizione,path"
					+ " FROM Risorsa JOIN Sezione ON codSezione"
					+ " WHERE codSezione="+codSezione;
			ResultSet rs2 = stmt.executeQuery(sql2);
			Sezione sez=cont.addSection(titolo, descr, visibilita, codSezione);
			while(rs2.next()) {
				String nome=rs2.getString("descrizione");
				String descr2=rs2.getString("descrizione");
				String path=rs2.getString("path");
				sez.addResource(nome, descr2, path, codSezione);
			}
			rs2.close();
		}
		rs.close();
		stmt.close();
		conn.close();
		return cont;
	}

	public void modificaTitolo(int codSezione, String titolo) throws ClassNotFoundException, SQLException {
		createSql();
		String sql;
		sql = "UPDATE Sezione"
				+ " SET titolo="+titolo
				+ " WHERE codSezione="+codSezione;
		ResultSet rs = stmt.executeQuery(sql);
		rs.close();
		stmt.close();
		conn.close();
	}

	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		createSql();
		String sql;
		sql = "UPDATE Sezione"
				+ "SET isPubblica=true"
				+ "WHERE codSezione="+codSezione;
		ResultSet rs = stmt.executeQuery(sql);
		rs.close();
		stmt.close();
		conn.close();
	}

	public void createFolder(String name, String path, int codSezione) throws ClassNotFoundException, SQLException {
		createSql();
		String sql;
		sql = "INSERT INTO Risorsa(CodRisorsa, Nome, Path, CodSezione) "
				+ "VALUES ("+name+", "+path+", "+codSezione+")";
		ResultSet rs = stmt.executeQuery(sql);
		rs.close();
		stmt.close();
		conn.close();
	}

	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		createSql();
		String sql;
		sql = "DELETE FROM Risorsa"
				+ "WHERE codRisorsa="+codRisorsa;
		ResultSet rs = stmt.executeQuery(sql);
		rs.close();
		stmt.close();
		conn.close();
	}

	public Corso getCorso(String cor) throws ClassNotFoundException, SQLException {
		createSql();
		Corso result=null;
		String sql;
		sql = "SELECT getCorso("+cor+")";
		ResultSet rs = stmt.executeQuery(sql);
		result.setNome(rs.getString(0));
		result.setAnno(rs.getString(1));
		result.setLaurea(rs.getString(2));
		result.setDescrizione(rs.getString(3));
		result.setContenuti(rs.getString(4));
		rs.close();
		stmt.close();
		conn.close();
		return result;
	}
}
