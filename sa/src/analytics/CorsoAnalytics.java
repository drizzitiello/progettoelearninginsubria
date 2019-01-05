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
     
     /**
      * Istanzia l'oggetto relativo al SocketDb di sistema.
      * @throws Exception 
      */	
 
     public CorsoAnalytics(int codCorso) throws Exception {
         this.socket = SocketDb.getInstanceDb();
         this.session = Sessione.getInstance();
         this.enabled = (this.session.info().tipoUtente == Utente.admin) ||
                        (this.session.info().tipoUtente == Utente.docente);

        //check corso proprietario
     }

     public int utentiConnessi(){

     }

     public int downloadsIntervallo(String dateStart, String dateEnd){
         
     }

     public String tempoMedioConn(){
         
     }
}