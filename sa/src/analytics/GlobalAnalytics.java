package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;

/**
* Servizi di analisi globale della piattaforma.
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
     private Sessione session;
     private boolean enabled = false;
     
     /**
      * Istanzia l'oggetto relativo al SocketDb di sistema.
      * @throws Exception 
      */	
 
     public GlobalAnalytics() throws Exception {
         this.socket = SocketDb.getInstanceDb();
         this.session = Sessione.getInstance();
         this.enabled = this.session.info().tipoUtente == Utente.admin;
     }

     public int utentiConnessi(){
        ArrayList<Map<String,Object>> response = this.socket.query("SELECT COUNT(matricola) AS UC FROM sessione WHERE fine_sessione IS NULL");
        return response.get(0).get("UC");
     }

     public int accessiIntervallo(String dateStart, String dateEnd){ 
        Object[] p = {dateStart, dateEnd);
        ArrayList<Map<String,Object>> r = this.socket.query("SELECT COUNT(matricola) AS CNT FROM sessione WHERE inizio_sesione BETWEEN ? AND ?");
        return response.get(0).get("CNT");
     }


    public Map<Integer, Integer> tempoMedioPerCorso(){
     //in minuti

        Map<Integer, Integer> tempiMedi = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corsi");
        for (Map<String, Object> b : r) {
            tempiMedi.put(b.get("codice_corso"), b.get("tempo_medio"));
        }
       
        return tempiMedi;
    }

    public Map<Integer, Integer> downloadsPerCorso(){
     
        Map<Integer, Integer> downloads = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_corsi");
        for (Map<String, Object> b : r) {
           downloads.put(b.get("codice_corso"), b.get("conteggio_download"));
        }
       
        return downloads;
    }
}



/*
 STORED FUNCTION: get_downloads_corsi()
 --------------------------------------------------------------
   DROP FUNCTION get_downloads_corsi();
   CREATE OR REPLACE FUNCTION get_downloads_corsi () 
         RETURNS TABLE (
                           codice_corso INT,
                           conteggio_download INT
         ) 
      AS $$
      BEGIN
         RETURN QUERY SELECT codice_corso,
                             COUNT(download.matricola)
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      GROUP BY codice_corso
      END; $$ 
      
      LANGUAGE 'plpgsql';



 STORED FUNCTION: get_tempo_medio_corsi()
 --------------------------------------------------------------
    DROP FUNCTION get_tempo_medio_corsi();
    CREATE OR REPLACE FUNCTION get_tempo_medio_corsi () 
          RETURNS TABLE (
                            codice_corso INT,
                            tempo_medio INT
          ) 
       AS $$
       BEGIN
          RETURN QUERY SELECT codice_corso, AVG((DATE_PART('day', fine_accesso - inizio_accesso) * 24 * 60 + 
                                                 DATE_PART('hour', fine_accesso - inizio_accesso)) * 60 +
                                                 DATE_PART('minute', fine_accesso - inizio_accesso))
                        FROM accesso_corso
                        WHERE fine_accesso IS NOT NULL
                        GROUP BY codice_corso;
       END; $$ 
       
       LANGUAGE 'plpgsql';

      */