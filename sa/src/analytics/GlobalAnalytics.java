package analytics;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.RemoteInterface;
import socketDb.SocketDb;
import user.User;
import session.Session;

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
	private RemoteInterface socket;
     
    /**
     * Istanzia l'oggetto relativo al SocketDb di sistema.
     * @throws Exception 
     */	
     public GlobalAnalytics() throws Exception {
    	 socket = (RemoteInterface) Naming.lookup ("rmi://localhost/SocketDb");
     }


   /**
    * Ottiene il numero di utenti attualmente online sulla piattaforma
    * @return numero di utenti loggati sulla piattaforma 
    * @throws ClassNotFoundException 
    * @throws SQLException 
 * @throws RemoteException 
    */	
     public int onlineUsers() throws ClassNotFoundException, SQLException, RemoteException{
        ArrayList<Map<String,Object>> response = this.socket.query("SELECT CAST(COUNT(matricola) as integer) AS uc FROM sessione WHERE fine_sessione IS NULL");
        return (int) response.get(0).get("uc");
     }


   /**
    * Ottiene il numero di accessi effettuati per ogni corso, dato un intervallo di tempo definito.
    * i parametri dateStart e dateEnd avranno il seguente formato: YYYY-MM-DD HH24:MI
    * @return mappa che associa un codice corso alla relativa somma degli accessi 
    * @throws ClassNotFoundException 
    * @throws SQLException 
 * @throws RemoteException 
    */	
     public Map<Integer, Integer> accessByInterval(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException, RemoteException{ 
         
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
 * @throws RemoteException 
    */	
    public Map<Integer, Integer> avgMinsOnlineForCourse() throws ClassNotFoundException, SQLException, RemoteException{

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
 * @throws RemoteException 
    */	
    public Map<Integer, Integer> downloadsForCourse() throws ClassNotFoundException, SQLException, RemoteException{
     
        Map<Integer, Integer> downloads = new HashMap<Integer, Integer>();

        ArrayList<Map<String,Object>> r = this.socket.function("get_downloads_corsi", new Object[] {});
        for (Map<String, Object> b : r) {
           downloads.put((int) b.get("cod_corso"),(int)  b.get("conteggio_download"));
        }
       
        return downloads;
    }
}