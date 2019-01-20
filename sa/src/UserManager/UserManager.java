package UserManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;
import Sessione.Sessione;
import Utente.Utente;
import authService.AuthenticationService;
import notifier.notifier;

/**
* Modifica e inserimento massivo delle informazioni utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class UserManager {
    
    /* Dichiarazione dei componenti di servizio */
    private SocketDb socket;
    private Sessione session;
    private boolean enabled = false;
    
    /**
	 * Istanzia oggetti relativi al SocketDb di sistema e alla Sessione corrente.
     * Imposta il flag enabled che abilita l'uso del componente ai soli utenti amministratori.
     * @throws Exception 
	 */
    public UserManager() throws Exception {
        this.socket = SocketDb.getInstanceDb();
        this.session = Sessione.getInstance();
        this.enabled = this.session.info().tipoUtente == Utente.admin;
    }


    /**
	 * Si occupa della modifica dei dati utente partendo da un oggetto Utente 
     * la modifica e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta modifica dei dati
     * @throws SQLException
	 */
    public static boolean modificaDatiUtente(Utente user) throws SQLException{
        if(!user.created() || !this.enabled)
            return false;


        Object[] p = { user.getInfo().nome,
                       user.getInfo().cognome,
                       user.getInfo().email,
                       user.getInfo().tipoUtente,
                       user.getInfo().annoImmatricolazione,
                       user.getInfo().corsoLaurea,
                       user.getInfo().statoCarriera,
                       user.getInfo().strutturaRiferimento,
                       user.getInfo().matricola
                     };
        
        this.socket.function("modifica_dati_utente", p);
        
        return true;
    }



     /**
	 * Elimina un utente registrato partendo da un oggetto Utente 
     * l'eliminazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta eliminazione utente
     * @throws SQLException
     * @throws ClassNotFoundException 
	 */
    public boolean eliminaUtente(Utente user) throws SQLException, ClassNotFoundException{
        if(!user.created() || !this.enabled)
            return false;

        Object[] p = { user.getInfo().matricola };
        this.socket.query("DELETE FROM utente WHERE matricola = ?", p);
        
        return true;
    }


    
     /**
	 * Sblocca un profilo utente bloccato per tentativi massimi di accessi con credeniali errate
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta eliminazione utente
     * @throws SQLException
     * @throws ClassNotFoundException 
	 */
    public boolean sbloccaUtente(Utente user) throws SQLException, ClassNotFoundException{
        if(!user.created() || !this.enabled)
            return false;

        Object[] p = { user.getInfo().matricola };
        
        this.socket.query("UPDATE TABLE utente SET loginAttemps = 0 WHERE matricola = ?", p);
        
        return true;
    }

    /**
	 * Creazione nuovo utente
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta creazione
     * @throws SQLException
	 */
    public boolean creaUtente(InfoUtente info) throws SQLException{
        if(!this.enabled)
        return false;

        String randomPassword = AuthenticationService.randomString();
        int randomCodAttivaz = AuthenticationService.createActivationCode();

        Object[] p = {
                    info.nome,
                    info.cognome,
                    info.email,
                    info.tipoUtente,
                    info.annoImmatricolazione,
                    info.corsoLaurea,
                    info.statoCarriera,
                    info.strutturaRiferimento,
                    info.matricola,
                    randomPassword,
                    randomCodAttivaz
                    };
        
        this.socket.function("crea_utente", p);
        
        String body;
        body = "Ciao " + info.nome + "! Ti diamo il benvenuto su SeatIn.\n\n";
        body += "Di seguito troverai le credenziali per accedere al portale:\n";
        body += "Utente: " + info.email + "\n";
        body += "Password: " + randomPassword + "\n";
        body += "Codice di attivazione: " + Integer.toString(randomCodAttivaz) + "\n";

        Notifier.sendSystemMail(info.email, "SeatIn", body);

        return true;
    }

     /**
	 * Importa utenti tramite file Comma Separated (CSV),
     * L'import avente la prima riga riservata all'intestazione assumera' il seguente formato:
     * MATRICOLA, NOME, COGNOME, EMAIL, TIPOUTENTE[S, D, A], ANNOIMMATRICOLAZIONE, CORSOLAUREA, STATOCARRIERA, STRUTTURARIF
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta importazione
     * @throws SQLException
	 */
    public boolean csvImportUtente(String path) throws SQLException{
        if(!this.enabled)
            return false;

            Path report = Paths.get(path);
            List<InfoUtente> utenti = new ArrayList<InfoUtente>();

            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {		
                br.readLine();			        // scarto la prima riga con intestazioni
                String line = br.readLine();	// leggo prima riga
                while (line != null) {			// loop righe
                    String[] attributi = line.split(",");
                    
                    InfoUtente i = new InfoUtente();
                    i.matricola = Integer.parseInt(attributi[0]);
                    i.nome = attributi[1];
                    i.cognome = attributi[2];
                    i.email = attributi[3];

                    switch(attributi[4]){
                        case "S":
                            i.tipoUtente = Utente.studente;
                        break;

                        case "D":
                            i.tipoUtente = Utente.docente;
                        break;

                        case "A":
                            i.tipoUtente = Utente.admin;
                        break;
                    }

                    i.annoImmatricolazione = Integer.parseInt(attributi[5]);
                    i.corsoLaurea = attributi[6];
                    i.statoCarriera = attributi[7];
                    i.strutturaRiferimento = attributi[8];

                    utenti.add(i);
                    line = br.readLine();				// prossima riga
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int i = 1;
            for (InfoUtente user: utenti) {				// memorizziamo ogni utente nel db
                if(!creaUtente(user)) System.err.println("Errore csv file riga " + i);
                i++;
            }

        return true;
    }




 }




 /*
 STORED FUNCTION: modifica_dati_utente(...)
 --------------------------------------------------------------
    -- FUNCTION: public.modifica_dati_utente(character varying, character varying, character varying, smallint, smallint, character varying, character varying, character varying, integer)

    -- DROP FUNCTION public.modifica_dati_utente(character varying, character varying, character varying, smallint, smallint, character varying, character varying, character varying, integer);

    CREATE OR REPLACE FUNCTION public.modifica_dati_utente(
        p_nome character varying,
        p_cognome character varying,
        p_email character varying,
        p_tipo_utente smallint,
        p_anno_immatricolazione smallint,
        p_corso_laurea character varying,
        p_stato_carriera character varying,
        p_struttura_riferimento character varying,
        p_matricola integer)
        RETURNS void
        LANGUAGE 'plpgsql'

        COST 100
        VOLATILE 
    AS $BODY$
        BEGIN
            UPDATE utente SET
                            nome = p_nome,
                            cognome = p_cognome,
                            email = p_email,
                            tipo_utente = p_tipo_utente
                        WHERE
                            matricola = p_matricola;

            UPDATE studente SET
                            anno_immatricolazione = p_anno_immatricolazione,
                            corso_laurea = p_corso_laurea,
                            stato_carriera = p_stato_carriera
                        WHERE
                            matricola = p_matricola;

            UPDATE docente SET
                            struttura_riferimento = p_struttura_riferimento
                        WHERE
                            matricola = p_matricola;
            
            UPDATE amministratore SET
                            struttura_riferimento = p_struttura_riferimento
                        WHERE
                            matricola = p_matricola;
                            
                            
        END; $BODY$;






STORED FUNCTION: crea_utente(...)
 --------------------------------------------------------------
    -- FUNCTION: public.crea_utente(character varying, character varying, character varying, smallint, smallint, character varying, character varying, character varying, integer, character varying, integer)

    -- DROP FUNCTION public.crea_utente(character varying, character varying, character varying, smallint, smallint, character varying, character varying, character varying, integer, character varying, integer);

    CREATE OR REPLACE FUNCTION public.crea_utente(
        p_nome character varying,
        p_cognome character varying,
        p_email character varying,
        p_tipo_utente smallint,
        p_anno_immatricolazione smallint,
        p_corso_laurea character varying,
        p_stato_carriera character varying,
        p_struttura_riferimento character varying,
        p_matricola integer,
        p_random_password character varying,
        p_random_codatt integer)
        RETURNS void
        LANGUAGE 'plpgsql'

        COST 100
        VOLATILE 
    AS $BODY$
        BEGIN

        INSERT INTO utente (matricola, nome, cognome, email, tipo_utente, password_hash, codice_attivazione)
                    VALUES (p_matricola, p_nome, p_cognome, p_email, p_tipo_utente, MD5(p_random_password), p_random_codatt);
        
        IF p_tipo_utente = 3 THEN
            INSERT INTO studente (matricola, anno_immatricolazione, corso_laurea, stato_carriera)
                    VALUES (p_matricola, p_anno_immatricolazione, p_corso_laurea, p_stato_carriera);
        ELSIF p_tipo_utente = 2 THEN
            INSERT INTO docente (matricola, struttura_riferimento)
                    VALUES (p_matricola, p_struttura_riferimento);
        ELSIF p_tipo_utente = 1 THEN
            INSERT INTO amministratore (matricola, struttura_riferimento)
                    VALUES (p_matricola, p_struttura_riferimento);
        END IF;
                            
        END; $BODY$;

 */