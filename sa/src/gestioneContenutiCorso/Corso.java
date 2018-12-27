package gestioneContenutiCorso;

import java.sql.SQLException;
import java.util.ArrayList;

import socketDb.SocketDb;

public class Corso {
	
	public int codCorso, peso, creatore, anno;
	public String nome, laurea, descrizione, contenuti;
	SocketDb socket;
	
	public Corso(String string, String string2, String string3, String string4, String string5, String string6, String attributi) {
		
	}
	
	public Corso() {
		// TODO Auto-generated constructor stub
	}

	public void setContenuti(Contenuto con) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		ArrayList<Sezione> sezioni = new ArrayList<Sezione>();
		ArrayList<Risorse> risorse = new ArrayList<Risorse>();
		for(Sezione sezione : con.sezioni) {
			sezioni.add(sezione);
			String sql = "INSERT INTO sezione "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
			Object[] p = {sezione.codSezione, sezione.titolo, sezione.descrizione,
					sezione.visibilita, sezione.codCorso, sezione.figlioDi,
					sezione.matricola};
			socket.query(sql,p);
			for(Risorse risorsa : sezione.risorse) {
				risorse.add(risorsa);
				String sql2 = "INSERT INTO risorsa " + 
						"VALUES (?, ?, ?, ?, ?, ?, ?)";
				Object[] p2 = {risorsa.codRisorsa, risorsa.nome, risorsa.descrizione, 
						risorsa.path, risorsa.tipo, risorsa.codSezione, risorsa.visibilita};
				socket.query(sql2,p2);
			}
		}
	}
	public void setCodCorso(String cC) {
		this.codCorso = Integer.parseInt(cC);
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setAnno(String anno) {
		this.anno = Integer.parseInt(anno);
	}
	public void setLaurea(String laurea) {
		this.laurea = laurea;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public void setPeso(String pesoCFU) {
		this.peso = Integer.parseInt(pesoCFU);
	}
	public void setCreatore(String creator) {
		this.creatore = Integer.parseInt(creator);
	}
}
