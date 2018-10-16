package gestioneContenutiCorso;

import java.sql.SQLException;

import socketDb.SocketDb;

public class GestioneContenutoCorso {
	SocketDb socket;
	public void uploadMatCorso(Corso cor, Contenuto ... con) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		for(Contenuto cont : con) {
			socket.setContenuti(cont,cor);
		}
	}
	public void createFolder(String name, String path, int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		socket.createFolder(name,path,codSezione);
	}
	public void cancelFolder(int codRisorsa) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		socket.cancelFolder(codRisorsa);
	}
	public void modificaTitolo(int codSezione, String titolo) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		socket.modificaTitolo(codSezione,titolo);
	}
	public void modificaVisibilita(int codSezione) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		socket.modificaVisibilita(codSezione);
	}
	public void visualizaAsStudent() {
		Session s=new Session("Studente");
	}
}
