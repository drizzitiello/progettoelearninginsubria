package gestioneContenutiCorso;

import java.sql.SQLException;

import socketDb.SocketDb;

public class Corso {
	public int codCorso;
	public String nome,anno,laurea,descrizione,contenuti;
	SocketDb socket;
	public void setContenuti(Contenuto con, Corso cor) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		socket.setContenuti(con,cor);
	}
	public void setCodCorso(String cC) {
		this.codCorso=Integer.parseInt(cC);
	}
	public void setNome(String nome) {
		this.nome=nome;
	}
	public void setAnno(String anno) {
		this.anno=anno;
	}
	public void setLaurea(String laurea) {
		this.laurea=laurea;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	public void setContenuti(String contenuti) {
		this.contenuti=contenuti;
	}
}
