package Utente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;

/**
* Gestione delle informazioni utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class Utente {
	
	class InfoUtente {
	       /* Dichiarazione delle info utente: */
	       public int matricola;
	       public String nome;
	       public String cognome;
	       public String email;
	       public int tipoUtente;
	       public int annoImmatricolazione;
	       public String corsoLaurea;
	       public String facolta;
	       public String statoCarriera;
	       public String strutturaRiferimnto;
	}
    
    /* Dichiarazione dei componenti di servizio: */
    private SocketDb socket;
    private boolean isCreated;
    private InfoUtente myInfo;
    
    /**
	 * Istanzia l'oggetto relativo al SocketDb di sistema.
     * Inizializza flag di riconoscimento di avvenuta creazione
     * dell'utente a false.
     * @throws Exception 
	 */	

    public Utente() throws Exception {
        this.socket = SocketDb.getInstanceDb();
        this.isCreated = false;
    }


    /**
	 * Si occupa della creazione dell'utente partendo da una matricola recuperando 
     * le relative informazioni dal database
	 *
	 * @return	flag di avvenuta creazione dell'utente
     * @throws SQLException 
     * @throws ClassNotFoundException 
	 */
    public boolean createFromMatricola(int matricola) throws ClassNotFoundException, SQLException{
        this.isCreated = false;
        
        //https://stackoverflow.com/questions/14582674/java-analog-for-phps-stdclass

        Object[] p = {matricola};
        ArrayList<Map<String,Object>> response = this.socket.function("getDatiUtente", p);
        
        if(response.size() != 1) return this.isCreated;
        this.createFromDbResult(response.get(0));
        return this.isCreated;
    }



     /**
	 * Si occupa della creazione dell'utente partendo da informazioni passate 
     * da un risultato dal DB
	 */
    public void createFromDbResult(Map<String,Object> row){
        this.myInfo = new InfoUtente();
        this.myInfo.matricola               = (int)		row.get("matricola");
        this.myInfo.nome                    = (String)	row.get("nome");
        this.myInfo.cognome                 = (String)	row.get("cognome");
        this.myInfo.email                   = (String)	row.get("email");
        this.myInfo.tipoUtente              = (int)		row.get("tipoutente");
        this.myInfo.annoImmatricolazione    = 0;
        this.myInfo.corsoLaurea             = "";
        this.myInfo.facolta                 = "";
        this.myInfo.statoCarriera           = "";
        this.myInfo.strutturaRiferimnto     = "";
        
        this.isCreated = true;
    }


    /* Metodi Getters: */


     /**
	 * Getter del flag di avvenuta creazione dell'Utente
	 *
	 * @return	flag di avvenuta creazione dell'Utente
	 */
    public boolean created(){
        return this.isCreated;
    }

     /**
	 * Getter delle informazioni relative all'Utente
	 *
	 * @return	Informazioni dell'Utente
	 */
    public InfoUtente getInfo(){
        if(!this.isCreated) return null;
        return this.myInfo;
    }

 }



 /*
 STORED FUNCTION: getDatiUtente(matricola)
 --------------------------------------------------------------
    DROP FUNCTION getdatiutente(integer);
CREATE OR REPLACE FUNCTION getDatiUtente (p_matricola INT) 
        RETURNS TABLE (
                        matricola INT,
                        nome VARCHAR,
                        cognome VARCHAR,
                        email VARCHAR,
                        tipoUtente SMALLINT
        ) 
    AS $$
    BEGIN
        RETURN QUERY SELECT
                        "Utente".matricola AS p1,
                        "Utente".nome AS p2,
                        "Utente".cognome AS p3,
                        "Utente".email AS p4,
                        "Utente"."tipoUtente" AS p5
                     FROM
                        "Utente"
                     WHERE
                        "Utente".matricola = p_matricola;
    END; $$ 
    
    LANGUAGE 'plpgsql';
 */