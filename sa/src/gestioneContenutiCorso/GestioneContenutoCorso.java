package gestioneContenutiCorso;

import java.sql.SQLException;

import socketDb.SocketDb;

public class GestioneContenutoCorso {
	SocketDb socket;
	public void uploadMatCorso(Corso cor, Contenuto con) throws Exception {
		socket=SocketDb.getInstanceDb();
		cor.setContenuti(con);
	}
	public void createFolder(String name, String path, int codSezione, int codRisorsa, String descr, String tipo, boolean pubblica) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "INSERT INTO risorsa(codice_risorsa, nome, descrizione, percorso, tipo, "
				+ "codice_sezione, is_pubblica) "
				+ "VALUES ("+codRisorsa+", '"+name+"', '"+descr+"', '"+path+"','"+tipo+"', "
						+ " "+codSezione+", "+pubblica+");";
		socket.query(sql);
	}
	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM risorsa"
				+ " WHERE codice_risorsa = "+codRisorsa;
		socket.query(sql);
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
	public void visualizaAsStudent() {
		//gestire a livello GUI
	}
}
