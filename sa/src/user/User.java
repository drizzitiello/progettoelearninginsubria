package user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import socketDb.SocketDb;

/**
* Gestione delle informazioni utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class User {
	
	public static class UserInfo {
	       /* Dichiarazione delle info utente: */
	       public Integer student_number;
	       public String name;
	       public String surname;
	       public String email;
	       public Integer userType;
	       public Integer registrationYear;
	       public String faculty;
	       public String careerStatus;
	       public String referenceStructure;
	}
    
    /* Dichiarazione livelli utente */
    static public final int admin = 3;
    static public final int professor = 2;
    static public final int student = 1; //modificare anche in stored crea_utente

    /* Dichiarazione dei componenti di servizio: */
    private SocketDb socket;
    private boolean isCreated;
    private UserInfo myInfo;
    
    /**
	 * Istanzia l'oggetto relativo al SocketDb di sistema.
     * Inizializza flag di riconoscimento di avvenuta creazione
     * dell'utente a false.
     * @throws Exception 
	 */
    public User() throws Exception {
        this.socket = SocketDb.getInstanceDb();
        this.isCreated = false;
    }


    /**
	 * Si occupa della creazione dell'utente partendo da una matricola recuperando 
     * le relative informazioni dal database
	 *
	 * @return	flag di avvenuta creazione dell'utente
     * @throws SQLException 
     * @throws ClassNotFoundException 
	 */
    public boolean createFromStudentNumber(int studentNumber) throws ClassNotFoundException, SQLException{
        this.isCreated = false;


        Object[] p = {studentNumber};
        ArrayList<Map<String,Object>> response = this.socket.function("get_dati_utente", p);
        
        if(response.size() != 1) return this.isCreated;
        this.createFromDbResult(response.get(0));
        return this.isCreated;
    }



     /**
	 * Si occupa della creazione dell'utente partendo da informazioni passate 
     * da un risultato dal DB
	 */
    public void createFromDbResult(Map<String,Object> row){
        this.myInfo = new UserInfo();
        this.myInfo.student_number               = (int)		row.get("matricola");
        this.myInfo.name                    = (String)	row.get("nome");
        this.myInfo.surname                 = (String)	row.get("cognome");
        this.myInfo.email                   = (String)	row.get("email");
        this.myInfo.userType              = (int)		row.get("tipo_utente");
        
        if(row.containsKey("anno_immatricolazione"))
            this.myInfo.registrationYear    = (Integer) row.get("anno_immatricolazione");
        if(row.containsKey("corso_laurea"))
            this.myInfo.faculty             = (String) row.get("corso_laurea");
        if(row.containsKey("stato_carriera"))
            this.myInfo.careerStatus           = (String) row.get("stato_carriera");
        if(row.containsKey("struttura_riferimento"))
            this.myInfo.referenceStructure    = (String) row.get("struttura_riferimento");
       
        this.isCreated = true;
    }


    /* Metodi Getters: */


     /**
	 * Getter del flag di avvenuta creazione dell'Utente
	 *
	 * @return	flag di avvenuta creazione dell'Utente
	 */
    public boolean created(){
        return this.isCreated;
    }

     /**
	 * Getter delle informazioni relative all'Utente
	 *
	 * @return	Informazioni dell'Utente
	 */
    public UserInfo getInfo(){
        if(!this.isCreated) return null;
        return this.myInfo;
    }

 }