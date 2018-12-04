package gestioneContenutiCorso;

import java.util.*;

public class Contenuto {
	List<Sezione> sezioni;
	public Contenuto() {
		sezioni=new ArrayList<Sezione>();
	}
	public Sezione addSection(String titolo, String descr, boolean visibilita, Integer codSezione, int matricola, int codCorso, Integer figlioDi) {
		Sezione sez=new Sezione(titolo, descr, visibilita, codSezione, matricola, codCorso, figlioDi);
		sezioni.add(sez);
		return sez;
	}
}