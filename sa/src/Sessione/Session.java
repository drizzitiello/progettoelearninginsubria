package Sessione;

import java.sql.SQLException;

import Utente.User;
import Utente.User.UserInfo;
import socketDb.SocketDb;

/**
* Gestione della sessione utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/

public class Session {
    
    /* Dichiarazione dei componenti di servizio: */
    private boolean isCreated;
    private SocketDb socket;
    public User user;    
    public static Session s;
    
    /**
	 * Ottiene l'istanza della sessione (pattern Singleton)
     * 
     * @return istanza della sessione o nuovo oggetto.
	 */
    public static Session getInstance() throws ClassNotFoundException {
		if(s == null)
			s = new Session();
		return s;
	}
    
    
    /**
     * Inizializza flag di riconoscimento di avvenuta creazione
     * della sessione a false.
	 */	 
    private Session() throws ClassNotFoundException{
        this.socket = SocketDb.getInstanceDb();
        this.isCreated = false;
	}


    /**
	 * Si occupa della creazione della sessione instanziando
     * cio' che sara' l'oggetto Utente autenticato nella piattaforma
	 *
	 * @return	flag di avvenuta creazione della sessione
     * @throws Exception 
	 */
    public boolean create(int student_number) throws Exception{
        this.isCreated = false;
        this.user = new User();
        this.user.createFromStudentNumber(student_number);
        this.isCreated = this.user.created();
        if(!this.isCreated) return this.isCreated;

        Object[] p = { student_number };

        //Purging delle sessioni non chiuse correttamente
        this.socket.query("DELETE FROM sessione WHERE matricola = ? AND fine_sessione IS NULL", p);

        //Apertura nuova sessione
        this.socket.query("INSERT INTO sessione (matricola, inizio_sessione) VALUES (?, NOW())", p);

        return this.isCreated;
    }



    /**
	 * Si occupa della chiusura della sessione in corso
	 *
	 * @return	flag di avvenuta distruzione della sessione
     * @throws SQLException 
     * @throws ClassNotFoundException 
	 */
    public boolean destroy() throws ClassNotFoundException, SQLException{
        if(!this.isCreated) return false;
        
        Object[] p = { this.user.getInfo().student_number };
        this.socket.query("UPDATE sessione SET fine_sessione = NOW() WHERE matricola = ? AND fine_sessione IS NULL", p);

        this.isCreated = false;
        return true;
    }


    /* Metodi Getters: */


     /**
	 * Getter del flag di avvenuta creazione della sessione
	 *
	 * @return	flag di avvenuta creazione della sessione
	 */
    public boolean created(){
        return this.isCreated;
    }

     /**
	 * Getter dell'oggetto Utente autenticato nella piattaforma
	 *
	 * @return	Oggetto Utente autenticato nella piattaforma
	 */
    public User getUser(){
        if(!this.isCreated) return null;
        return this.user;
    }


     /**
	 * Getter delle info utente autenticato nella piattaforma
	 *
	 * @return	Oggetto Utente autenticato nella piattaforma
	 */
    public UserInfo info(){
        if(!this.isCreated) return null;
        return this.user.getInfo();
    }

}