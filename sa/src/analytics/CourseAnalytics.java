package analytics;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import socketDb.SocketDb;
import user.User;
import session.Session;

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


public class CourseAnalytics {

     /* Dichiarazione dei componenti di servizio */
	private RemoteInterface socket;
	private AnotherInterface server;
     private int courseCode;
     
     /**
      * Istanzia l'oggetto relativo al SocketDb di sistema.
      * @throws Exception 
      */
     public CourseAnalytics(int courseCode) throws Exception {
    	server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
 		int i = server.getRegistry();
 		System.out.println(i);
 		Registry registry = LocateRegistry.getRegistry("localhost",i); 
 		socket = (RemoteInterface) registry.lookup ("SocketDb");
        this.courseCode = courseCode;
     }

     /**
     * Ottiene il numero di utenti attualmente connessi al corso
     * @return numero di utenti connessi al corso
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * @throws RemoteException 
     */
     public int onlineUsers() throws ClassNotFoundException, SQLException, RemoteException{
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
     * @throws RemoteException 
     */
     public Map<Integer, Integer> downloadByInterval(String dateStart, String dateEnd) throws ClassNotFoundException, SQLException, RemoteException{
      
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
     * @throws RemoteException 
     */
     public int avgMinsOnline() throws ClassNotFoundException, SQLException, RemoteException{
         Object[] p = {this.courseCode};  
         ArrayList<Map<String,Object>> r = this.socket.function("get_tempo_medio_corso", p);
         if(r.get(0).get("tempo_medio")==null) return 0;
         else return (int) r.get(0).get("tempo_medio");
     }
}