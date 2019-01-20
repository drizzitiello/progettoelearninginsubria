package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;

/**
* Servizi di analisi delle informazioni utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class CorsoAnalytics {

     /* Dichiarazione dei componenti di servizio */
     private SocketDb socket;
     private Sessione session;
     private boolean enabled = false;
     private int codCorso;
     
     /**
      * Istanzia l'oggetto relativo al SocketDb di sistema.
      * @throws Exception 
      */
     public CorsoAnalytics(int codCorso) throws Exception {
         this.socket = SocketDb.getInstanceDb();
         this.session = Sessione.getInstance();
         this.enabled = (this.session.info().tipoUtente == Utente.admin) ||
                        (this.session.info().tipoUtente == Utente.docente);
         this.codCorso = codCorso;
     }

     public int utentiConnessi(){
         Object[] p = {this.codCorso);  
         ArrayList<Map<String,Object>> r = this.socket.query("SELECT COUNT(matricola) AS UC FROM accesso_corso WHERE fine_accesso IS NULL AND codice_corso = ?", p);
         return r.get(0).get("UC");
     }

     public Map<Integer, Integer> downloadsIntervallo(String dateStart, String dateEnd){
      
         Map<Integer, Integer> risorse = new HashMap<Integer, Integer>();
      
         Object[] p = {this.codCorso, dateStart, dateEnd);
         ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_intervallo", p);
         for (Map<String, Object> b : r) {
           risorse.put(b.get("codice_risorsa"), b.get("conteggio_download"));
         }
        
         return risorse;
     }

     public String tempoMedioConn(){
         Object[] p = {this.codCorso);  
         ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corso", p);
         return r.get(0).get("tempo_medio");
     }
}


/*
 STORED FUNCTION: get_downloads_intervallo(codice_corso, dataStart, dataEnd)
 --------------------------------------------------------------
   DROP FUNCTION get_downloads_intervallo(integer, timestamp, timestamp);
   CREATE OR REPLACE FUNCTION get_downloads_intervallo (p_codice_corso INT, p_start_date TIMESTAMP, p_end_date TIMESTAMP) 
         RETURNS TABLE (
                           codice_risorsa INT,
                           conteggio_download INT
         ) 
      AS $$
      BEGIN
         RETURN QUERY SELECT download.codice_risorsa,
                             COUNT(DISTINCT(download.matricola)) 
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      WHERE codice_corso = p_codice_corso AND data_e_ora BETWEEN p_start_date AND p_end_date
                      GROUP BY download.codice_risorsa
      END; $$ 
      
      LANGUAGE 'plpgsql';


  STORED FUNCTION: get_tempo_medio_corso(codice_corso)
  --------------------------------------------------------------
    DROP FUNCTION get_tempo_medio_corso(integer);
    CREATE OR REPLACE FUNCTION get_tempo_medio_corso (p_codice_corso INT) 
          RETURNS TABLE (
                            tempo_medio INT
          ) 
       AS $$
       BEGIN
          RETURN QUERY  SELECT AVG((DATE_PART('day', fine_accesso - inizio_accesso) * 24 * 60 + 
                               DATE_PART('hour', fine_accesso - inizio_accesso)) * 60 +
                               DATE_PART('minute', fine_accesso - inizio_accesso))
                        FROM accesso_corso
                        WHERE fine_accesso IS NOT NULL AND codice_corso = p_codice_corso;
       END; $$ 
       
       LANGUAGE 'plpgsql';
    */
 