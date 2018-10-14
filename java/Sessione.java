package uniserver;

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
    private Socket socket;
    private boolean isCreated;
    private Utente utente;    
    
    /**
	 * Istanzia l'oggetto relativo al socket di sistema.
     * Inizializza flag di riconoscimento di avvenuta creazione
     * della sessione a false.
	 */	 
    public Sessione(Socket socket) {
        this.socket = socket;
        this.isCreated = false;
    }


    /**
	 * Si occupa della creazione della sessione instanziando
     * cio' che sara' l'oggetto Utente autenticato nella piattaforma
	 *
	 * @return	flag di avvenuta creazione della sessione
	 */
    public boolean create(int matricola){
        isCreated = false;
        this.utente = new Utente(matricola);
        this.isCreated = this.utente == null ? false : true;
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
    public int getUtente(){
        if(!this.isCreated) return null;
        return this.utente;
    }

 }