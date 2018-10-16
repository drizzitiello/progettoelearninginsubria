package gestioneContenutiCorso;

import java.util.ArrayList;
import java.util.List;

public class Sezione {

	public Integer PadreDi;
	public String titolo;
	public String descrizione;
	public Boolean visibilita;
	public Integer codCorso;
	List<Risorse> risorse=new ArrayList<Risorse>();
	int indice=0;
	public Sezione(String titolo, String descr, boolean visibilita, Integer codCorso) {
		this.titolo=titolo;
		this.descrizione=descr;
		this.visibilita=visibilita;
		this.codCorso=codCorso;
	}

	public boolean hasMoreResources() {
		if(risorse.get(indice)!=null) {
			return true;
		}
		else {
			indice=0;
			return false;
		}
	}

	public Risorse nextResource() {
		indice++;
		return risorse.get(indice);
	}

	public void addResource(String nome, String descr2, String path, int codSezione) {
		Risorse ris=new Risorse(nome, descr2, path, codSezione);
		risorse.add(ris);
	}

}
