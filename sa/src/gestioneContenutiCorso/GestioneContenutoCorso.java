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
		String sql = "INSERT INTO \"Risorsa\"(\"codiceRisorsa\", nome, descrizione, percorso, tipo, "
				+ "\"codiceSezione\", \"isPubblica\") "
				+ "VALUES ("+codRisorsa+", '"+name+"', '"+descr+"', '"+path+"','"+tipo+"', "
						+ " "+codSezione+", "+pubblica+");";
		socket.query(sql);
	}
	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM \"Risorsa\""
				+ " WHERE \"codiceRisorsa\" = "+codRisorsa;
		socket.query(sql);
	}
	public void modificaTitolo(int codSezione, String titolo) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE \"Sezione\""+ " SET titolo = '"+titolo+
				"' WHERE \"codiceSezione\" = "+codSezione;
		socket.query(sql);
	}
	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE \"Sezione\""+ " SET \"isPubblica\" = true"+ " WHERE \"codiceSezione\" = "+codSezione;
		socket.query(sql);
	}
	public void visualizaAsStudent() {
		//gestire a livello GUI
	}
}
