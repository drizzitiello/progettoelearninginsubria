package Sessione;

import Utente.Utente;

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
    public Utente utente;    
    public static Sessione s;
    
    /**
	 * Ottiene l'istanza della sessione (pattern Singleton)
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
        isCreated = false;
        this.utente = new Utente();
        this.utente.createFromMatricola(matricola);
        this.isCreated = this.utente.created();
        return this.isCreated;
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
    public Utente
    getUtente(){
        if(!this.isCreated) return null;
        return this.utente;
    }

}