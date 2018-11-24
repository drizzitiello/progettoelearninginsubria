package uniserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


public class InfoUtente {
       /* Dichiarazione delle info utente: */
       public int matricola;
       public String nome;
       public String cognome;
       public String email;
       public int userLevel;
       public int annoImmatricolazione;
       public String corsoLaurea;
       public String facolta;
       public String statoCarriera;
       public String strutturaRiferimnto;
}


public class Utente {
    
    /* Dichiarazione dei componenti di servizio: */
    private SocketDb socket;
    private boolean isCreated;
    private InfoUtente myInfo;
    
    /**
	 * Istanzia l'oggetto relativo al SocketDb di sistema.
     * Inizializza flag di riconoscimento di avvenuta creazione
     * dell'utente a false.
	 */	

    public Utente() {
        this.socket = SocketDb.getInstanceDb();
        this.isCreated = false;
    }

    public boolean Utente(int matricola) {
        this();
        return this.createFromMatricola(matricola);
    }


    /**
	 * Si occupa della creazione dell'utente partendo da una matricola recuperando 
     * le relative informazioni dal database
	 *
	 * @return	flag di avvenuta creazione dell'utente
	 */
    public boolean createFromMatricola(int matricola){
        this.isCreated = false;
        
        //https://stackoverflow.com/questions/14582674/java-analog-for-phps-stdclass

        ArrayList<Map<String,Object>> response = this.socket.function("getDatiUtente", {matricola});

        if(response.size() != 1) return this.isCreated;
        this.createFromDbResult(response.get(0));
        return this.isCreated;
    }



     /**
	 * Si occupa della creazione dell'utente partendo da informazioni passate 
     * da un componente esterno
	 */
    public void createFromInfo(InfoUtente infos){
        this.myinfo = infos;
        this.isCreated = true;
    }



     /**
	 * Si occupa della creazione dell'utente partendo da informazioni passate 
     * da un risultato dal DB
	 */
    public void createFromDbResult(Map<String,Object> row){
        this.myInfo = new InfoUtente();
        this.myInfo.matricola               = row.get('matricola');
        this.myInfo.nome                    = row.get('nome');
        this.myInfo.cognome                 = row.get('cognome');
        this.myInfo.email                   = row.get('email');
        this.myInfo.userLevel               = row.get('userLevel');
        this.myInfo.annoImmatricolazione    = row.get('annoImmatricolazione');
        this.myInfo.corsoLaurea             = row.get('corsoLaurea');
        this.myInfo.facolta                 = row.get('facolta');
        this.myInfo.statoCarriera           = row.get('statoCarriera');
        this.myInfo.strutturaRiferimnto     = row.get('strutturaRiferimnto');
        
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
    public int getInfo(){
        if(!this.isCreated) return null;
        return this.myInfo;
    }

 }



 /*

 STORED FUNCTION: getDatiUtente(matricola)
 --------------------------------------------------------------

    CREATE OR REPLACE FUNCTION getDatiUtente (p_matricola VARCHAR) 
        RETURNS TABLE (
                        matricola INT,
                        nome VARCHAR,
                        cognome VARCHAR,
                        email VARCHAR,
                        userLevel INT,
                        annoImmatricolazione INT,
                        corsoLaurea VARCHAR,
                        facolta VARCHAR,
                        statoCarriera VARCHAR,
                        strutturaRiferimnto VARCHAR
        ) 
    AS $$
    BEGIN
        RETURN QUERY SELECT
                        matricola,
                        nome,
                        cognome,
                        email,
                        userLevel,
                        annoImmatricolazione,
                        corsoLaurea,
                        facolta,
                        statoCarriera,
                        strutturaRiferimnto
                     FROM
                        utente
                     WHERE
                        matricola = p_matricola;
    END; $$ 
    
    LANGUAGE 'plpgsql';


 */