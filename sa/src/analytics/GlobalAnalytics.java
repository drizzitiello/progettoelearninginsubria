package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sessione.Sessione;
import socketDb.SocketDb;
import Utente.Utente;

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

     public int utentiConnessi() throws ClassNotFoundException, SQLException{
        ArrayList<Map<String,Object>> response = this.socket.query("SELECT CAST(COUNT(matricola) as integer) AS uc FROM sessione WHERE fine_sessione IS NULL");
        return (int) response.get(0).get("uc");
     }

     public int accessiIntervallo(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException{ 
        Object[] p = {dateStart, dateEnd};
        ArrayList<Map<String,Object>> r = this.socket.query("SELECT CAST(COUNT(matricola) as integer) AS cnt FROM sessione WHERE inizio_sessione BETWEEN to_timestamp(?, 'YYYY-MM-DD HH24:MI') AND to_timestamp(?, 'YYYY-MM-DD HH24:MI')", p);
        return (int) r.get(0).get("cnt");
     }


    public Map<Integer, Integer> tempoMedioPerCorso() throws ClassNotFoundException, SQLException{
     //in minuti

        Map<Integer, Integer> tempiMedi = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corsi", new Object[] {});
        for (Map<String, Object> b : r) {
            tempiMedi.put((int) b.get("cod_corso"),(int) b.get("tempo_medio"));
        }
       
        return tempiMedi;
    }

    public Map<Integer, Integer> downloadsPerCorso() throws ClassNotFoundException, SQLException{
     
        Map<Integer, Integer> downloads = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_corsi", new Object[] {});
        for (Map<String, Object> b : r) {
           downloads.put((int) b.get("cod_corso"),(int)  b.get("conteggio_download"));
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
                           cod_corso INT,
                           conteggio_download INT
         ) 
      AS $$
      BEGIN
         RETURN QUERY SELECT codice_corso,
                             COUNT(download.matricola)
                      FROM download
                      JOIN risorsa ON risorsa.codice_risorsa = download.codice_risorsa
                      JOIN sezione ON sezione.codice_sezione = risorsa.codice_sezione
                      GROUP BY codice_corso;
      END; $$ 
      
      LANGUAGE 'plpgsql';



 STORED FUNCTION: get_tempo_medio_corsi()
 --------------------------------------------------------------
     DROP FUNCTION get_tempo_medio_corsi();
    CREATE OR REPLACE FUNCTION get_tempo_medio_corsi () 
          RETURNS TABLE (
                            cod_corso INT,
                            tempo_medio INT
          ) 
       AS $$
       BEGIN
          RETURN QUERY SELECT codice_corso, CAST(ceil(AVG((DATE_PART('day', fine_accesso - inizio_accesso) * 24 * 60 + 
                                                 DATE_PART('hour', fine_accesso - inizio_accesso)) * 60 +
                                                 DATE_PART('minute', fine_accesso - inizio_accesso))) as integer)
                        FROM accesso_corso
                        WHERE fine_accesso IS NOT NULL
                        GROUP BY codice_corso;
       END; $$ 
       
       LANGUAGE 'plpgsql';

      */