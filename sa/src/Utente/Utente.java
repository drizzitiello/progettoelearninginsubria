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
	
	public class InfoUtente {
	       /* Dichiarazione delle info utente: */
	       public Integer matricola;
	       public String nome;
	       public String cognome;
	       public String email;
	       public int tipoUtente;
	       public int annoImmatricolazione;
	       public String corsoLaurea;
	       public String statoCarriera;
	       public String strutturaRiferimento;
	}
    
    /* Dichiarazione livelli utente */
    static public final int admin = 1;
    static public final int docente = 2;
    static public final int studente = 3; //modificare anche in stored crea_utente

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
        this.myInfo.tipoUtente              = (int)		row.get("tipo_utente");

        if(row.containsKey("anno_immatricolazione"))
            this.myInfo.annoImmatricolazione    = (int) row.get("anno_immatricolazione");
        if(row.containsKey("corso_laurea"))
            this.myInfo.corsoLaurea             = (int) row.get("corso_laurea");
        if(row.containsKey("stato_carriera"))
            this.myInfo.statoCarriera           = (int) row.get("stato_carriera");
        if(row.containsKey("struttura_riferimento"))
            this.myInfo.strutturaRiferimento    = (int) row.get("struttura_riferimento");
       
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
                        tipoutente SMALLINT,
                        anno_immatricolazione SMALLINT,
                        corso_laurea VARCHAR,
                        stato_carriera VARCHAR,
                        struttura_riferimento VARCHAR
        ) 
    AS $$
    BEGIN
        RETURN QUERY SELECT
                        utente.matricola,
                        utente.nome,
                        utente.cognome,
                        utente.email,
                        utente.tipo_utente,
                        studente.anno_immatricolazione,
                        studente.corso_laurea,
                        studente.stato_carriera,
                        struttura.struttura_riferimento
                     FROM
                        utente
                     LEFT JOIN studente ON studente.matricola = utente.matricola
                     LEFT JOIN struttura ON struttura.matricola = utente.matricola
                     WHERE
                        utente.matricola = p_matricola;
    END; $$ 
    
    LANGUAGE 'plpgsql';
 */


