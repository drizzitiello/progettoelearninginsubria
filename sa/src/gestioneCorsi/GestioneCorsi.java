package gestioneCorsi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

import Utente.Utente;
import gestioneContenutiCorso.Corso;
import notifier.Notifier;
import socketDb.SocketDb; 

/** Gestione dei corsi.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.3	*/

public class GestioneCorsi {
	
	//CAMPI
	private SocketDb socket;
	protected List<Corso> corsi = new ArrayList<Corso>();
	
	/** Costruttore: assegnamento del socket */
	public GestioneCorsi () throws ClassNotFoundException {
		socket = SocketDb.getInstanceDb();
	}
	
	/** Importazione dei dati dei corsi da file CSV e salvataggio 
	 * 	degli stessi all'interno del database  */
	public void dataInput (String CSV_fileName) throws ClassNotFoundException, SQLException {
        Path pathToFile = Paths.get(CSV_fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {		// creiamo un'istanza di BufferedReader
            String line = br.readLine();			// leggiamo la prima riga del file
            while (line != null) {					// fino a quando non abbiamo letto tutte le righe
                String[] attributi = line.split(",");
                Corso corso = new Corso();
                corso.setCodCorso(Integer.parseInt(attributi[0]));
                corso.setNome(attributi[1]);
                corso.setAnno(Integer.parseInt(attributi[2]));
                corso.setLaurea(attributi[3]);
                corso.setDescrizione(attributi[4]);
                corso.setPeso(Integer.parseInt(attributi[5]));
                corso.setCreatore(Integer.parseInt(attributi[6]));
                corsi.add(corso);					// aggiungiamo il corso all'array list
                line = br.readLine();				// leggiamo la prossima riga
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        for (Corso a: corsi) {						// memorizziamo ogni corso nel database
        	Object[] params = {a.codCorso, a.nome, (Integer) a.anno_attivazione, a.laurea, a.descrizione, a.peso, a.creatore};
        	socket.function("import_dati_corsi", params);
        }
	}
	
	/** Creazione di un nuovo corso nel database	*/
	public void creazioneCorso (Corso c) throws ClassNotFoundException, SQLException {				
        Object[] params = {c.codCorso, c.nome, c.anno_attivazione, c.laurea, c.descrizione, c.peso, c.creatore};
        socket.function("import_dati_corsi", params);
	} 
	
	/** Eliminazione dei dati relativi ad un corso presente nel database	*/
	protected void cancellaCorso (Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.codCorso};
		socket.function("elimina_dati_corsi", params);
	} 
	
	/** Modifica dei dati relativi ad un corso presente nel database	*/
	public void modificaCorso (Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.codCorso, c.nome, c.anno_attivazione, c.laurea, c.descrizione, c.peso, c.creatore};
		socket.function("modifica_dati_corsi", params);
	} 
	
	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	*/
	public void assegnamentoCorsi(Utente studente) throws ClassNotFoundException, SQLException {
		Object[] params = {studente.getInfo().matricola};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di piu' studenti al loro piano di studi	*/
	public void assegnamentoCorsi(List<Utente> studenti) throws ClassNotFoundException, SQLException {
		for (Utente studente : studenti) {
			Object[] params = {studente.getInfo().matricola};
			socket.function("assegna_studenti", params);
		}
	}
	
	/** Assegnamento di un corso di competenza ad un docente	*/
	public void assegnamentoCorsi(Utente docente, Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {docente.getInfo().matricola, c.nome };
		socket.function("assegna_docente", params);
	}
	
	/** Assegnamento di piu' corsi ad un docente	*/
	public void assegnamentoCorsi(Utente docente, List<Corso> lista_corsi) throws ClassNotFoundException, SQLException {
		for (Corso a : lista_corsi) {
			Object[] params = {docente.getInfo().matricola, a.nome };
			socket.function("assegna_docente", params);
		}
	}
	
	/** Ricerca dei docenti che tengono un determinato corso	
	 * 	@return	array con le matricole dei docenti cercati	*/
	public ArrayList<Utente> chiTieneCorso (Corso c) throws Exception {
		Object[] params = {c.codCorso};
		ArrayList<Map<String, Object>> matricolaDocente = socket.function("ricerca_docenti", params);
		
		ArrayList<Object> matricole = new ArrayList<Object>();
		for (Map<String, Object> a : matricolaDocente) {
			matricole.add((int) a.get("docente")); 
			}
		return this.datiDeiDocenti(matricole);
	} 
	
	/** Estrae i dati principali di un docente dal database e li memorizza 
	 * 	@return lista dei docenti	*/
	private ArrayList<Utente> datiDeiDocenti(ArrayList<Object> matricole) throws Exception {
		ArrayList<Utente> docenti = new ArrayList<Utente>();
		ArrayList<Map<String,Object>> datiDocente;
		for (Object a: matricole) {
			datiDocente = socket.function("get_dati_docente", new Object[] {a});
			for (Map<String, Object> b : datiDocente) {
				Utente u = new Utente();
				u.createFromDbResult(b);
				docenti.add(u);
			}
		}
		return docenti;
	}
	
	/** Verifica se uno studente risulta iscritto a un corso	
	 * 	@return check di controllo	*/
	public boolean studenteIscrittoAlCorso (Utente studente, Corso c) throws ClassNotFoundException, SQLException {
		boolean risposta = false;
		Object[] params = {studente.getInfo().matricola, c.codCorso};
		ArrayList<Map<String, Object>> esito = socket.function("verifica_iscrizione_studente", params);
		for (Map<String, Object> a : esito) {
			risposta = a.containsValue(true); 	}
		return risposta;
	}
	
	/** Permette a uno studente di iscriversi a un corso	
	 * @throws Exception */
	public void iscriviAlCorso (Utente studente, Corso c) throws Exception {
		Object[] params = {studente.getInfo().matricola, (short) c.codCorso};
		socket.function("iscrivi_studente_al_corso", params);
		//invio email a utente stesso e ai docenti titolari del corso
		ArrayList<Utente> doc = chiTieneCorso(c);
		Notifier.send_uninsubria_email("usr", "pwd", "utente", "iscrizione a corso", "iscritto a");
		for(Utente d : doc){
				Notifier.send_uninsubria_email("usr", "pwd", "docente", "iscrizione a corso", "iscritto a");
		}
	}
}
// STORED PROCEDURE
/* 
-- FUNCTION: public.iscrivi_studente_al_corso(integer, smallint)

-- DROP FUNCTION public.iscrivi_studente_al_corso(integer, smallint);

CREATE OR REPLACE FUNCTION public.iscrivi_studente_al_corso(
	matr integer,
	cod_corso smallint)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	INSERT INTO iscritto_a (matricola, codice_corso)
	VALUES (matr, cod_corso);
END;

$BODY$;

ALTER FUNCTION public.iscrivi_studente_al_corso(integer, smallint)
    OWNER TO postgres;
*/
