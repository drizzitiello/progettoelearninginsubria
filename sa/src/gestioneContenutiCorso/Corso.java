package gestioneContenutiCorso;

import java.sql.SQLException;
import java.util.ArrayList;

import socketDb.SocketDb;

public class Corso {
	public int codCorso;
	public String nome,anno,laurea,descrizione,contenuti;
	SocketDb socket;
	public void setContenuti(Contenuto con, Corso cor) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		ArrayList<Sezione> sezioni=new ArrayList<Sezione>();
		ArrayList<String> titoli=new ArrayList<String>();
		ArrayList<String> descrizioni=new ArrayList<String>();
		ArrayList<Boolean> visibilita=new ArrayList<Boolean>();
		ArrayList<Integer> codiciCorso=new ArrayList<Integer>();
		ArrayList<Integer> PadreDi=new ArrayList<Integer>();
		ArrayList<Risorse> risorse=new ArrayList<Risorse>();
		ArrayList<String> nomi=new ArrayList<String>();
		ArrayList<String> descrizioni2=new ArrayList<String>();
		ArrayList<Boolean> paths=new ArrayList<Boolean>();
		ArrayList<Integer> codiciSezione=new ArrayList<Integer>();
		while(con.hasMoreSections()) {
			Sezione sezione=con.nextSection();
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
		String sql = "INSERT INTO Sezione(Titolo, Descrizione, isPubblica, CodCorso, PadreDi) "
				+ "VALUES( "+titoli+", "+descrizioni+", "+visibilita+", "+codiciCorso+", "+PadreDi+")";
		String sql2 = "INSERT INTO Risorsa(Nome, Descrizione, Path, CodSezione) "
				+ "VALUES( "+nomi+", "+descrizioni2+", "+paths+", "+codiciSezione+")";
		socket.dmlQuery(sql);
		socket.dmlQuery(sql2);
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
