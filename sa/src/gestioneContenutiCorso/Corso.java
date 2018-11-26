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
		ArrayList<Integer> codiciSezione=new ArrayList<Integer>();
		ArrayList<String> titoli=new ArrayList<String>();
		ArrayList<String> descrizioni=new ArrayList<String>();
		ArrayList<Boolean> visibilita=new ArrayList<Boolean>();
		ArrayList<Integer> codiciCorso=new ArrayList<Integer>();
		ArrayList<Integer> PadreDi=new ArrayList<Integer>();
		ArrayList<Integer> matricole=new ArrayList<Integer>();
		
		ArrayList<Risorse> risorse=new ArrayList<Risorse>();
		ArrayList<Integer> codiciRisorse=new ArrayList<Integer>();
		ArrayList<String> nomi=new ArrayList<String>();
		ArrayList<String> descrizioni2=new ArrayList<String>();
		ArrayList<String> paths=new ArrayList<String>();
		ArrayList<String> tipi=new ArrayList<String>();
		ArrayList<Integer> codiciSezione2=new ArrayList<Integer>();
		ArrayList<Boolean> visibilita2=new ArrayList<Boolean>();
		while(con.hasMoreSections()) {
			Sezione sezione=con.nextSection();
			sezioni.add(sezione);
			codiciSezione.add(sezione.codSezione);
			titoli.add(sezione.titolo);
			descrizioni.add(sezione.descrizione);
			visibilita.add(sezione.visibilita);
			codiciCorso.add(sezione.codCorso);
			PadreDi.add(sezione.PadreDi);
			matricole.add(sezione.matricola);
			while(sezione.hasMoreResources()) {
				Risorse risorsa=sezione.nextResource();
				risorse.add(risorsa);
				codiciRisorse.add(risorsa.codRisorsa);
				nomi.add(risorsa.titolo);
				descrizioni2.add(risorsa.descrizione);
				paths.add(risorsa.path);
				nomi.add(risorsa.titolo);
				codiciSezione2.add(risorsa.codSezione);
				visibilita2.add(risorsa.visibilita);
			}
		}
		String sql = "INSERT INTO \"Sezione\"(\"codiceSezione\", Titolo, Descrizione, \"isPubblica\", "
				+ "\"codiceCorso\", \"figlioDi\", matricola) "
				+ "VALUES( "+codiciSezione+", "+titoli+", "+descrizioni+", "+visibilita+", "
				+codiciCorso+", "+PadreDi+", "+matricole+")";
		String sql2 = "INSERT INTO \"Risorsa\"(\"codiceRisorsa\", nome, descrizione, percorso, tipo, "
				+ "\"codiceSezione\", \"isPubblica\") "
				+ "VALUES ("+codiciRisorse+", "+nomi+", "+descrizioni2+", "+paths+","+tipi+", "
						+ " "+codiciSezione2+""+visibilita2+", )";
		socket.query(sql);
		socket.query(sql2);
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
