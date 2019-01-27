package session;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import socketDb.SocketDb;
import user.User;
import user.User.UserInfo;

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
    private RemoteInterface socket;
	private static AnotherInterface server;
    public User user;    
    public static Session s;
    
    /**
	 * Ottiene l'istanza della sessione (pattern Singleton)
     * 
     * @return istanza della sessione o nuovo oggetto.
     * @throws NotBoundException 
     * @throws RemoteException 
     * @throws MalformedURLException 
	 */
    public static Session getInstance() throws ClassNotFoundException, MalformedURLException, RemoteException, NotBoundException {
    	if(s == null)
			s = new Session();
    	else
    		server.starting();
		return s;
	}
    
    
    /**
     * Inizializza flag di riconoscimento di avvenuta creazione
     * della sessione a false.
     * @throws NotBoundException 
     * @throws RemoteException 
     * @throws MalformedURLException 
	 */	 
    private Session() throws ClassNotFoundException, MalformedURLException, RemoteException, NotBoundException{
    	server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		server.starting();
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
        this.isCreated = false;
	}


    /**
	 * Si occupa della creazione della sessione instanziando
     * cio' che sara' l'oggetto Utente autenticato nella piattaforma
	 *
	 * @return	flag di avvenuta creazione della sessione
     * @throws Exception 
	 */
    public boolean create(int student_number){
        try{
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
        }
        catch(Exception e) {
        	this.isCreated=false;
        	return this.isCreated;
        }

        return this.isCreated;
    }



    /**
	 * Si occupa della chiusura della sessione in corso
	 *
	 * @return	flag di avvenuta distruzione della sessione
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean destroy() throws ClassNotFoundException, SQLException, RemoteException{
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