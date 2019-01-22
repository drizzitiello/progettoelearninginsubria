package analytics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sessione.Session;
import socketDb.SocketDb;
import Utente.User;

/**
* Servizi di analisi delle informazioni utente.
* L'utilizzo della classe e' riservato esclusivamente ai docenti proprietari del corso
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
     private int courseCode;
     
     /**
      * Istanzia l'oggetto relativo al SocketDb di sistema.
      * @throws Exception 
      */
     public CorsoAnalytics(int courseCode) throws Exception {
         this.socket = SocketDb.getInstanceDb();
         this.courseCode = courseCode;
     }

     /**
     * Ottiene il numero di utenti attualmente connessi al corso
     * @return numero di utenti connessi al corso
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
     public int onlineUsers() throws ClassNotFoundException, SQLException{
         Object[] p = {this.courseCode};  
         ArrayList<Map<String,Object>> r = this.socket.query("SELECT CAST(COUNT(matricola) AS INTEGER) AS uc FROM accesso_corso WHERE fine_accesso IS NULL AND codice_corso = ?", p);
         return (int) r.get(0).get("uc");
     }


     /**
     * Ottiene il numero complessivo di download delle risorse del corso
     * i parametri dateStart e dateEnd avranno il seguente formato: YYYY-MM-DD HH24:MI
     * 
     * @return mappa che associa un codice risorsa al relativo numero complessivo di download
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
     public Map<Integer, Integer> downloadByInterval(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException{
      
         Map<Integer, Integer> outmap = new HashMap<Integer, Integer>();
      
         Object[] p = {this.courseCode, dateStart, dateEnd};
         ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_intervallo", p);
         for (Map<String, Object> b : r) {
            outmap.put((int) b.get("codice_risorsa"), (int) b.get("conteggio_download"));
         }
        
         return outmap;
     }


     /**
     * Ottiene il tempo medio di connessione al corso espresso in minuti
     *
     * @return tempo medio di accesso al corso espresso in minuti
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
     public int avgMinsOnline() throws ClassNotFoundException, SQLException{
         Object[] p = {this.courseCode};  
         ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corso", p);
         if(r.get(0).get("tempo_medio")==null) return 0;
         else return (int) r.get(0).get("tempo_medio");
     }
}


/*
 STORED FUNCTION: get_downloads_intervallo(codice_corso, dataStart, dataEnd)
 --------------------------------------------------------------
  ***** Da aggiornare ****


 STORED FUNCTION: get_tempo_medio_corso(codice_corso)
 --------------------------------------------------------------
  ***** Da aggiornare ****


    */
 