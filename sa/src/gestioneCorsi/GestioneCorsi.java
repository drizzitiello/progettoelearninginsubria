package gestioneCorsi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

import Utente.Utente;
import gestioneContenutiCorso.Corso;
import socketDb.SocketDb;

/** Gestione dei corsi.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.1	*/

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
                Corso corso = new Corso(attributi[0], attributi[1], attributi[2], attributi[3], 
                		attributi[4], attributi[5], attributi[6]);
                corso.setCodCorso(attributi[0]);
                corso.setNome(attributi[1]);
                corso.setAnno(attributi[2]);
                corso.setLaurea(attributi[3]);
                corso.setDescrizione(attributi[4]);
                corso.setPeso(attributi[5]);
                corso.setCreatore(attributi[6]);
                corsi.add(corso);					// aggiungiamo il corso all'array list
                line = br.readLine();				// leggiamo la prossima riga
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        for (Corso a: corsi) {						// memorizziamo ogni corso nel database
        	Object[] params = {a.codCorso, a.nome, a.anno, a.laurea, a.descrizione, a.peso, a.creatore};
        	socket.function("import_dati_corsi", params);
        }
	}
	
	/** Creazione di un nuovo corso nel database	*/
	public void creazioneCorso (Corso c) throws ClassNotFoundException, SQLException {				
        Object[] params = {c.codCorso, c.nome, c.anno, c.laurea, c.descrizione, c.peso, c.creatore};
        socket.function("import_dati_corsi", params);
	} 
	
	/** Eliminazione dei dati relativi ad un corso presente nel database	*/
	protected void cancellaCorso (Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.codCorso};
		socket.function("elimina_dati_corsi", params);
	} 
	
	/** Modifica dei dati relativi ad un corso presente nel database	*/
	public void modificaCorso (Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.codCorso, c.nome, c.anno, c.laurea, c.descrizione, c.peso, c.creatore};
		socket.function("modifica_dati_corsi", params);
	} 
	
	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	*/
	protected void assegnamentoCorsi(Utente studente) throws ClassNotFoundException, SQLException {
		Object[] params = {studente.getInfo().matricola};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di piu' studenti al loro piano di studi	*/
	protected void assegnamentoCorsi(List<Utente> studenti) throws ClassNotFoundException, SQLException {
		for (Utente studente : studenti) {
			Object[] params = {studente.getInfo().matricola};
			socket.function("assegna_studenti", params);
		}
	}
	
	/** Assegnamento di un corso di competenza ad un docente	*/
	protected void assegnamentoCorsi(Utente docente, Corso c) throws ClassNotFoundException, SQLException {
		Object[] params = {docente.getInfo().matricola, c.nome };
		socket.function("assegna_docente", params);
	}
	
	/** Assegnamento di piu' corsi ad un docente	*/
	protected void assegnamentoCorsi(Utente docente, List<Corso> lista_corsi) throws ClassNotFoundException, SQLException {
		for (Corso a : lista_corsi) {
			Object[] params = {docente.getInfo().matricola, a.nome };
			socket.function("assegna_docente", params);
		}
	}
}

//	---> STORED PROCEDURES <---
/* 
-- FUNCTION: public.import_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)

-- DROP FUNCTION public.import_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer);

CREATE OR REPLACE FUNCTION public.import_dati_corsi(
	code smallint,
	nominative character varying,
	a_year smallint,
	faculty character varying,
	description character varying,
	cfu smallint,
	creator integer)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	INSERT INTO corso (codice_corso, nome, anno_attivazione, facolta, descrizione, peso, creatore)
	VALUES (code, nominative, a_year, faculty, description, cfu, creator);
END;

$BODY$;

ALTER FUNCTION public.import_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)
    OWNER TO postgres;

COMMENT ON FUNCTION public.import_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)
    IS 'Importa i dati presi dal file CSV nel database';	*/
// -------------------------------------------------------------------------------
/* 
-- FUNCTION: public.elimina_dati_corsi(smallint)
 
-- DROP FUNCTION public.elimina_dati_corsi(smallint);

CREATE OR REPLACE FUNCTION public.elimina_dati_corsi(
	codice smallint)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	DELETE FROM corso
	WHERE codice_corso = codice;
END;

$BODY$;

ALTER FUNCTION public.elimina_dati_corsi(smallint)
    OWNER TO postgres;

COMMENT ON FUNCTION public.elimina_dati_corsi(smallint)
    IS 'Elimina i dati di un corso basandosi sul confronto del codice identificativo';	*/
//-------------------------------------------------------------------------------
/* 
-- FUNCTION: public.modifica_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)

-- DROP FUNCTION public.modifica_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer);

CREATE OR REPLACE FUNCTION public.modifica_dati_corsi(
	code smallint,
	nominative character varying,
	a_year smallint,
	faculty character varying,
	description character varying,
	cfu smallint,
	creator integer)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	UPDATE corso
	SET codice_corso = code, 
	nome = nominative,
	anno_attivazione = a_year, 
	facolta = faculty,
	descrizione = description,
	peso = cfu,
	creatore = creator
	WHERE codice_corso = code;
END;

$BODY$;

ALTER FUNCTION public.modifica_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)
    OWNER TO postgres;

COMMENT ON FUNCTION public.modifica_dati_corsi(smallint, character varying, smallint, character varying, character varying, smallint, integer)
    IS 'Aggiorna i dati relativi al corso specificato';	*/
//-------------------------------------------------------------------------------
/* 
-- FUNCTION: public.assegna_studenti(integer)

-- DROP FUNCTION public.assegna_studenti(integer);

CREATE OR REPLACE FUNCTION public.assegna_studenti(
	matricola_fornita integer)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
BEGIN
	INSERT INTO iscritto_a (matricola, codice_corso)
	SELECT matricola, codice_corso
	FROM studente, corso
	WHERE facolta IN (SELECT corso_laurea
			   		FROM studente
			   		WHERE matricola = matricola_fornita);
END;

$BODY$;

ALTER FUNCTION public.assegna_studenti(integer)
    OWNER TO postgres;

COMMENT ON FUNCTION public.assegna_studenti(integer)
    IS 'Assegna i corsi di competenza di uno studente al suo piano di studi';	*/
//-------------------------------------------------------------------------------
/*
-- FUNCTION: public.assegna_docente(integer, character varying)

-- DROP FUNCTION public.assegna_docente(integer, character varying);

CREATE OR REPLACE FUNCTION public.assegna_docente(
	matricola_fornita integer,
	materia character varying)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	INSERT INTO tiene (docente, codice_corso)
	SELECT matricola, codice_corso
	FROM docente, corso
	WHERE corso.nome = materia AND docente.matricola = matricola_fornita; 
END;

$BODY$;

ALTER FUNCTION public.assegna_docente(integer, character varying)
    OWNER TO postgres;

COMMENT ON FUNCTION public.assegna_docente(integer, character varying)
    IS 'Assegna un corso di competenza ad un docente'; */
