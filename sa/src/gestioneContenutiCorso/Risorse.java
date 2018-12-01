package gestioneContenutiCorso;

public class Risorse {
	String nome, descrizione, path;
	int codSezione;
	public int codRisorsa;
	public Boolean visibilita;
	public Risorse(String nome, String descr2, String path, int codSezione) {
		this.nome=nome;
		this.descrizione=descr2;
		this.path=path;
		this.codSezione=codSezione;
	}
}