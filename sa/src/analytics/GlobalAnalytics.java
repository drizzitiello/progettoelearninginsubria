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
}