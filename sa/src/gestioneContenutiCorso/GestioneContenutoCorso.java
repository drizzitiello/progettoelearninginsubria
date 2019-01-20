package gestioneContenutiCorso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import graphics.PaginaCorso;
import socketDb.SocketDb;
import Sessione.Sessione;

public class GestioneContenutoCorso {
	SocketDb socket;
	public void uploadMatCorso(Corso cor, Contenuto con) throws Exception {
		socket=SocketDb.getInstanceDb();
		cor.setContenuti(con);
	}
	public void createSezione(Sezione s) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "INSERT INTO sezione(codice_sezione, titolo, descrizione, is_pubblica, codice_corso, "
				+ "figlio_di, matricola) "
				+ "VALUES ("+s.codSezione+", '"+s.titolo+"', '"+s.descrizione+"', '"+s.visibilita+"','"+s.codCorso+"', "
						+ " "+s.figlioDi+", "+s.matricola+");";
		socket.query(sql);
	}
	public void cancellaSezione(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM sezione"
				+ " WHERE codice_sezione = "+codSezione;
		socket.query(sql);
	}
	public Sezione getSezione(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "SELECT * FROM sezione"
				+ " WHERE codice_sezione = "+codSezione;
		ArrayList<Map<String,Object>> sez = socket.query(sql);
		Sezione sezione=null;
		for(Map<String,Object> m : sez) {
			if(m.get("figlio_di")!=null)
			sezione = new Sezione((String) m.get("titolo"),(String)  m.get("descrizione"),
					(boolean) m.get("is_pubblica"), (int)  m.get("codice_sezione"), 
					(int) m.get("matricola"),(int)  m.get("codice_corso"),(int)  m.get("figlio_di"));
			else
				sezione = new Sezione((String) m.get("titolo"),(String)  m.get("descrizione"),
						(boolean) m.get("is_pubblica"), (int)  m.get("codice_sezione"), 
						(int) m.get("matricola"),(int)  m.get("codice_corso"), null);
		}
		return sezione;
	}
	public void modificaSezione(Sezione s) throws Exception {
		socket=SocketDb.getInstanceDb();
		Object[] params = {s.codSezione, s.titolo , s.visibilita , s.descrizione,  s.codCorso,
				(Integer) s.figlioDi, s.matricola};
		socket.function("modificasezione", params);
	}
	public void createRisorsa(Risorse r) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "INSERT INTO risorsa(codice_risorsa, nome, descrizione, percorso, tipo, "
				+ "codice_sezione, is_pubblica) "
				+ "VALUES ("+r.codRisorsa+", '"+r.nome+"', '"+r.descrizione+"', '"+r.path+"','"+r.tipo+"', "
						+ " "+r.codSezione+", "+r.visibilita+");";
		socket.query(sql);
	}
	public void cancellaRisorsa(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM risorsa"
				+ " WHERE codice_risorsa = "+codRisorsa;
		socket.query(sql);
	}
	public void modificaRisorsa(Risorse r) throws Exception {
		socket=SocketDb.getInstanceDb();
		Object[] params = {(short) r.codRisorsa, r.nome,  r.descrizione,
				 r.path, r.tipo, (short) r.codSezione,r.visibilita};
		socket.function("modificarisorsa", params);
	}
	public Risorse getRisorsa(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "SELECT * FROM risorsa"
				+ " WHERE codice_risorsa = "+codRisorsa;
		ArrayList<Map<String,Object>> sez = socket.query(sql);
		Risorse risorsa=null;
		for(Map<String,Object> m : sez) {
			risorsa = new Risorse((String) m.get("nome"),(String)  m.get("descrizione"),
					(String) m.get("percorso"),
					(int)  m.get("codice_sezione"), (int)  m.get("codice_risorsa"), 
					(boolean) m.get("is_pubblica"), (String)  m.get("tipo"));
		}
		return risorsa;
	}
	public void modificaTitolo(int codSezione, String titolo) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE sezione"+ " SET titolo = '"+titolo+
				"' WHERE codice_sezione = "+codSezione;
		socket.query(sql);
	}
	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE sezione"+ " SET is_pubblica = true"+ " WHERE codice_sezione = "+codSezione;
		socket.query(sql);
	}
	public static void visualizaAsStudent(String corso) {
		try {
			PaginaCorso mc = new PaginaCorso(Sessione.getInstance(), corso, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
