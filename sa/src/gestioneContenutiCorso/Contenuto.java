package gestioneContenutiCorso;

import java.util.*;

public class Contenuto {
	List<Sezione> sezioni;
	int indice=0;
	public Contenuto() {
		sezioni=new ArrayList<Sezione>();
	}
	public Sezione addSection(String titolo, String descr, boolean visibilita, Integer codSezione) {
		Sezione sez=new Sezione(titolo, descr, visibilita, codSezione);
		sezioni.add(sez);
		return sez;
	}

	public boolean hasMoreSections() {
		if(sezioni.get(indice)!=null) {
			return true;
		}
		else {
			indice=0;
			return false;
		}
	}

	public Sezione nextSection() {
		indice++;
		return sezioni.get(indice);
	}

}
