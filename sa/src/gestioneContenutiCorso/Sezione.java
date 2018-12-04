package gestioneContenutiCorso;

import java.util.ArrayList;
import java.util.List;

public class Sezione {

	public Integer figlioDi;
	public String titolo;
	public String descrizione;
	public Boolean visibilita;
	public Integer codCorso;
	List<Risorse> risorse=new ArrayList<Risorse>();
	public int codSezione;
	public int matricola;
	public Sezione(String titolo, String descr, boolean visibilita, Integer codSezione,
			int matricola, int codCorso, Integer figlioDi) {
		this.titolo=titolo;
		this.codSezione=codSezione;
		this.descrizione=descr;
		this.visibilita=visibilita;
		this.codCorso=codCorso;
		this.matricola=matricola;
		this.figlioDi=figlioDi;
	}

	public void addResource(String nome, String descr2, String path, int codSezione, int codRisorsa,
			boolean visibilita, String tipo) {
		Risorse ris=new Risorse(nome, descr2, path, codSezione, codRisorsa, visibilita, tipo);
		risorse.add(ris);
	}

}
