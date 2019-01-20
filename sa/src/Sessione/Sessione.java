package Sessione;

import Utente.Utente;
import Utente.Utente.InfoUtente;
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

public class Sessione {
    
    /* Dichiarazione dei componenti di servizio: */
    private boolean isCreated;
    private SocketDb socket;
    public Utente utente;    
    public static Sessione s;
    
    /**
	 * Ottiene l'istanza della sessione (pattern Singleton)
     * 
     * @return istanza della sessione o nuovo oggetto.
	 */
    public static Sessione getInstance() throws ClassNotFoundException {
		if(s == null)
			s = new Sessione();
		return s;
	}
    
    
    /**
     * Inizializza flag di riconoscimento di avvenuta creazione
     * della sessione a false.
	 */	 
    private Sessione() throws ClassNotFoundException{
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
    public boolean create(int matricola) throws Exception{
        this.isCreated = false;
        this.utente = new Utente();
        this.utente.createFromMatricola(matricola);
        this.isCreated = this.utente.created();
        if(!this.isCreated) return this.isCreated;

        Object[] p = { matricola };

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
	 */
    public boolean destroy(){
        if(!this.isCreated) return false;
        
        Object[] p = { matricola };
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
    public Utente getUtente(){
        if(!this.isCreated) return null;
        return this.utente;
    }


     /**
	 * Getter delle info utente autenticato nella piattaforma
	 *
	 * @return	Oggetto Utente autenticato nella piattaforma
	 */
    public InfoUtente info(){
        if(!this.isCreated) return null;
        return this.utente.getInfo();
    }

}