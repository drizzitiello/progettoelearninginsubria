package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sessione.Session;
import socketDb.SocketDb;
import Utente.User;

/**
* Servizi di analisi globale della piattaforma.
* L'utilizzo della classe e' riservato esclusivamente agli utenti amministratori
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class GlobalAnalytics {

     /* Dichiarazione dei componenti di servizio */
     private SocketDb socket;
     
    /**
     * Istanzia l'oggetto relativo al SocketDb di sistema.
     * @throws Exception 
     */	
     public GlobalAnalytics() throws Exception {
         this.socket = SocketDb.getInstanceDb();
     }


   /**
    * Ottiene il numero di utenti attualmente online sulla piattaforma
    * @return numero di utenti loggati sulla piattaforma 
    * @throws ClassNotFoundException 
    * @throws SQLException 
    */	
     public int onlineUsers() throws ClassNotFoundException, SQLException{
        ArrayList<Map<String,Object>> response = this.socket.query("SELECT CAST(COUNT(matricola) as integer) AS uc FROM sessione WHERE fine_sessione IS NULL");
        return (int) response.get(0).get("uc");
     }


   /**
    * Ottiene il numero di accessi effettuati per ogni corso, dato un intervallo di tempo definito.
    * i parametri dateStart e dateEnd avranno il seguente formato: YYYY-MM-DD HH24:MI
    * @return mappa che associa un codice corso alla relativa somma degli accessi 
    * @throws ClassNotFoundException 
    * @throws SQLException 
    */	
     public Map<Integer, Integer> accessByInterval(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException{ 
         
         Map<Integer, Integer> accessList = new HashMap<Integer, Integer>();
      
         Object[] p = {dateStart, dateEnd};
         ArrayList<Map<String,Object>> r = this.socket.function("get_accessi_by_corso", p);
        
         for (Map<String, Object> b : r) {
            accessList.put((int) b.get("cod_corso"),(int) b.get("conteggio_accesi"));
         }
         
         return accessList;
     }


   /**
    * Ottiene il tempo medio di connessione espresso in minuti di ogni corso
    *
    * @return mappa che associa un codice corso al relativo tempo medio di accesso espresso in minuti
    * @throws ClassNotFoundException 
    * @throws SQLException 
    */	
    public Map<Integer, Integer> avgMinsOnlineForCourse() throws ClassNotFoundException, SQLException{

        Map<Integer, Integer> avgMins = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corsi", new Object[] {});
        for (Map<String, Object> b : r) {
            avgMins.put((int) b.get("cod_corso"),(int) b.get("tempo_medio"));
        }
       
        return avgMins;
    }


   /**
    * Ottiene il numero complessivo di download delle risorse relativi a ogni corso
    *
    * @return mappa che associa un codice corso al relativo numero complessivo di download
    * @throws ClassNotFoundException 
    * @throws SQLException 
    */	
    public Map<Integer, Integer> downloadsForCourse() throws ClassNotFoundException, SQLException{
     
        Map<Integer, Integer> downloads = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_corsi", new Object[] {});
        for (Map<String, Object> b : r) {
           downloads.put((int) b.get("cod_corso"),(int)  b.get("conteggio_download"));
        }
       
        return downloads;
    }
}



/*

STORED FUNCTION: get_accessi_by_corso(dateStart, dateEnd)
 --------------------------------------------------------------
   DROP FUNCTION get_accessi_by_corso(VARCHAR, VARCHAR);
   CREATE OR REPLACE FUNCTION get_accessi_by_corso (p_date_start VARCHAR, p_date_end VARCHAR) 
         RETURNS TABLE (
                           cod_corso INT,
                           conteggio_accesi INT
         ) 
      AS $$
      BEGIN
         RETURN QUERY SELECT codice_corso,
                             CAST(COUNT(matricola) AS INTEGER)
                      FROM accesso_corso
                      WHERE inizio_accesso BETWEEN to_timestamp(p_date_start, 'YYYY-MM-DD HH24:MI') AND to_timestamp(p_date_end, 'YYYY-MM-DD HH24:MI')
                      GROUP BY codice_corso;
      END; $$ 
      
      LANGUAGE 'plpgsql';




 STORED FUNCTION: get_downloads_corsi()
 --------------------------------------------------------------
   ***** Da aggiornare ****



 STORED FUNCTION: get_tempo_medio_corsi()
 --------------------------------------------------------------
   ***** Da aggiornare ****

*/