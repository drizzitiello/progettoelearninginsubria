package gestioneContenutiCorso;

public class Risorse {
	String nome, descrizione, path;
	int codSezione;
	public int codRisorsa;
	public Boolean visibilita;
	public String tipo;
	public Risorse(String nome, String descr2, String path, int codSezione, int codRisorsa,
			Boolean visibilita, String tipo) {
		this.nome=nome;
		this.descrizione=descr2;
		this.path=path;
		this.codSezione=codSezione;
		this.codRisorsa=codRisorsa;
		this.visibilita=visibilita;
		this.tipo=tipo;
	}
}