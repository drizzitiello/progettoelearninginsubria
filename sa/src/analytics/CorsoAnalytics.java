package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sessione.Sessione;
import socketDb.SocketDb;
import Utente.Utente;

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

     public int utentiConnessi() throws ClassNotFoundException, SQLException{
         Object[] p = {this.codCorso};  
         ArrayList<Map<String,Object>> r = this.socket.query("SELECT CAST(COUNT(matricola) AS INTEGER) AS uc FROM accesso_corso WHERE fine_accesso IS NULL AND codice_corso = ?", p);
         return (int) r.get(0).get("uc");
     }

     public Map<Integer, Integer> downloadsIntervallo(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException{
      
         Map<Integer, Integer> risorse = new HashMap<Integer, Integer>();
      
         Object[] p = {this.codCorso, dateStart, dateEnd};
         ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_intervallo", p);
         for (Map<String, Object> b : r) {
           risorse.put((int) b.get("codice_risorsa"), (int) b.get("conteggio_download"));
         }
        
         return risorse;
     }

     public int tempoMedioConn() throws ClassNotFoundException, SQLException{
         Object[] p = {this.codCorso};  
         ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corso", p);
         return (int) r.get(0).get("tempo_medio");
     }
}


/*
 STORED FUNCTION: get_downloads_intervallo(codice_corso, dataStart, dataEnd)
 --------------------------------------------------------------
  DROP FUNCTION get_downloads_intervallo(integer, varchar, varchar);
   CREATE OR REPLACE FUNCTION get_downloads_intervallo (p_codice_corso INT, p_start_date varchar, p_end_date varchar) 
         RETURNS TABLE (
                           codice_risorsa INT,
                           conteggio_download INT
         ) 
      AS $$
      BEGIN
         RETURN QUERY SELECT download.codice_risorsa,
                             CAST(COUNT(DISTINCT(download.matricola)) AS INTEGER)
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      WHERE codice_corso = p_codice_corso AND data_e_ora BETWEEN to_timestamp(p_start_date, 'YYYY-MM-DD HH24:MI') AND to_timestamp(p_end_date, 'YYYY-MM-DD HH24:MI')
                      GROUP BY download.codice_risorsa;
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
 