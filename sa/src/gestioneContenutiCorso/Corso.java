package gestioneContenutiCorso;

import java.sql.SQLException;
import java.util.ArrayList;

import socketDb.SocketDb;

public class Corso {
	
	public int codCorso, peso, creatore, anno_attivazione;
	public String nome, laurea, descrizione, contenuti, facolta;
	SocketDb socket;
	
	public Corso() {
		
	}

	public Corso(int codCorso, String nome, int anno_attivazione, String facolta, String descrizione,
			int peso, int creatore) {
		this.codCorso=codCorso;
		this.nome=nome;
		this.anno_attivazione=anno_attivazione;
		this.facolta=facolta;
		this.descrizione=descrizione;
		this.peso=peso;
		this.creatore=creatore;
	}
	/**
	 * 
	 * @param con
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
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
	public void setCodCorso(int i) {
		this.codCorso = i;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setAnno(int anno) {
		this.anno_attivazione = anno;
	}
	public void setLaurea(String laurea) {
		this.laurea = laurea;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public void setPeso(int i) {
		this.peso = i;
	}
	public void setCreatore(int i) {
		this.creatore = i;
	}
}
