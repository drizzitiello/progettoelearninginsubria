package UserManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;
import Sessione.Sessione;

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
    private enabled = false;
    
    /**
	 * Istanzia l'oggetto relativo al SocketDb di sistema.
     * @throws Exception 
	 */	

    public UserManager() throws Exception {
        this.socket = SocketDb.getInstanceDb();
        this.session = Sessione.getInstance();
        this.enabled = this.session.info().tipoUtente == Utente.admin
    }


    /**
	 * Si occupa della modifica dei dati utente partendo da un oggetto Utente 
     * la modifica e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta modifica dei dati
     * @throws SQLException
	 */
    public boolean modificaDatiUtente(Utente user) throws SQLException{
        if(!user.created() || !this.enabled)
            return false;


        Object[] p = { user.getInfo().nome,
                       user.getInfo().cognome,
                       user.getInfo().email,
                       user.getInfo().tipoUtente,
                       user.getInfo().annoImmatricolazione,
                       user.getInfo().corsoLaurea,
                       user.getInfo().facolta,
                       user.getInfo().statoCarriera,
                       user.getInfo().strutturaRiferimento,
                       user.getInfo().matricola
                     };
        
        this.socket.query("UPDATE TABLE utente 
                           SET nome                     = ?,
                               congome                  = ?,
                               email                    = ?,
                               tipo_utente              = ?,
                               anno_immatricolazione    = ?,
                               corso_laurea             = ?,
                               facolta                  = ?,
                               stato_carriera           = ?,
                               struttura_riferimento    = ?
                           WHERE matricola = ?", p);
        
        return true;
    }



     /**
	 * Elimina un utente registrato partendo da un oggetto Utente 
     * l'eliminazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta eliminazione utente
     * @throws SQLException
	 */
    public boolean eliminaUtente(Utente user) throws SQLException{
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
	 */
    public boolean sbloccaUtente(Utente user) throws SQLException{
        if(!user.created() || !this.enabled)
            return false;

        Object[] p = { user.getInfo().matricola };
        
        this.socket.query("UPDATE TABLE utente SET loginAttemps = 0 WHERE matricola = ?", p);
        
        return true;
    }


    /**
	 * Importa utenti
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta importazione
     * @throws SQLException
	 */
    public boolean csvImportUtente(String path) throws SQLException{
        if(!this.enabled)
            return false;

        String myMatr = this.session.info().matricola;

        
        this.socket.query("CREATE TEMPORARY TABLE temp_" + myMatr + " (matricola INTEGER,
                                                                       nome VARCHAR(30),
                                                                       cognome VARCHAR(40),
                                                                       email VARCHAR(40),
                                                                       password VARCHAR(50),
                                                                       cod_attivazione VARCHAR(40),
                                                                       tipo_utente SMALLINT(2),
                                                                       anno_immatricolazione VARCHAR(40),
                                                                       corso_laurea VARCHAR(40),
                                                                       facolta VARCHAR(40),
                                                                       stato_carriera VARCHAR(40),
                                                                       struttura_riferimento VARCHAR(40))");
        
        
        Object[] p = { path };
        this.socket.query(" COPY temp_" + myMatr + " (matricola,nome,cognome,email,
                                                      password,cod_attivazione,tipo_utente,
                                                      anno_immatricolazione,corso_laurea,facolta,
                                                      stato_carriera,struttura_riferimento)
                            FROM ?
                            WITH (FORMAT CSV, DELIMITER(';'))", p);


        this.socket.query(" INSERT INTO utente (matricola,nome,cognome,email,
                                                passwordHash,cod_attivazione,login_attemps,tipo_utente,
                                                anno_immatricolazione,corso_laurea,facolta,
                                                stato_carriera,struttura_riferimento)
                            SELECT  matricola,nome,cognome,email,
                                    MD5(password),cod_attivazione,0,tipo_utente,
                                    anno_immatricolazione,corso_laurea,facolta,
                                    stato_carriera,struttura_riferimento
                            FROM temp_" + myMatr);

                           
        
        this.socket.query("DROP TABLE temp_" + myMatr);
        
        return true;
    }




 }
