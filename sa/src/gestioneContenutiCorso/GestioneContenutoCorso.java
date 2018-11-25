package gestioneContenutiCorso;

import java.sql.SQLException;

import socketDb.SocketDb;

public class GestioneContenutoCorso {
	SocketDb socket;
	public void uploadMatCorso(Corso cor, Contenuto ... con) throws Exception {
		socket=SocketDb.getInstanceDb();
		for(Contenuto cont : con) {
			cor.setContenuti(cont,cor);
		}
	}
	public void createFolder(String name, String path, int codSezione, String pubblica, String codRisorsa, String descr, String tipo) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "INSERT INTO Risorsa(codiceRisorsa, nome, descrizione, percorso, tipo, "
				+ "codiceSezione, isPubblica) "
				+ "VALUES ("+codRisorsa+", "+name+", "+descr+", "+path+","+tipo+", "
						+ " "+codSezione+""+pubblica+", )";
		socket.query(sql);
	}
	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM Risorsa"
				+ " WHERE codRisorsa = "+codRisorsa;
		socket.query(sql);
	}
	public void modificaTitolo(int codSezione, String titolo) throws Exception {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE Sezione"+ " SET titolo="+titolo+
				" WHERE codSezione = "+codSezione;
		socket.query(sql);
	}
	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE Sezione"+ " SET isPubblica=true"+ " WHERE codSezione = "+codSezione;
		socket.query(sql);
	}
	public void visualizaAsStudent() {
		//gestire a livello GUI
	}
}
