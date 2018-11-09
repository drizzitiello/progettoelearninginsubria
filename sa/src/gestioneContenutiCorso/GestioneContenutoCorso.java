package gestioneContenutiCorso;

import java.sql.SQLException;

import socketDb.SocketDb;

public class GestioneContenutoCorso {
	SocketDb socket;
	public void uploadMatCorso(Corso cor, Contenuto ... con) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		for(Contenuto cont : con) {
			cor.setContenuti(cont,cor);
		}
	}
	public void createFolder(String name, String path, int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "INSERT INTO Risorsa(CodRisorsa, Nome, Path, CodSezione) "
				+ "VALUES ("+name+", "+path+", "+codSezione+")";
		socket.dmlQuery(sql);
	}
	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "DELETE FROM Risorsa"
				+ "WHERE codRisorsa="+codRisorsa;
		socket.dmlQuery(sql);
	}
	public void modificaTitolo(int codSezione, String titolo) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE Sezione"+ " SET titolo="+titolo+
				" WHERE codSezione="+codSezione;
		socket.dmlQuery(sql);
	}
	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "UPDATE Sezione"+ "SET isPubblica=true"+ "WHERE codSezione="+codSezione;
		socket.dmlQuery(sql);
	}
	public void visualizaAsStudent() {
		//gestire a livello GUI
	}
}
