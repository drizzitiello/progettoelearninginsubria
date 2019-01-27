package user;

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
    static public final int ADMIN = 3;
    static public final int PROFESSOR = 2;
    static public final int STUDENT = 1; //modificare anche in stored crea_utente

    /* Dichiarazione dei componenti di servizio: */
    private RemoteInterface socket;
	private AnotherInterface server;
    private boolean isCreated;
    private UserInfo myInfo;
    
    /**
	 * Istanzia l'oggetto relativo al SocketDb di sistema.
     * Inizializza flag di riconoscimento di avvenuta creazione
     * dell'utente a false.
     * @throws Exception 
	 */
    public User() throws Exception {
    	server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		System.out.println(i);
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
        this.isCreated = false;
    }


    /**
	 * Si occupa della creazione dell'utente partendo da una matricola recuperando 
     * le relative informazioni dal database
	 *
	 * @return	flag di avvenuta creazione dell'utente
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean createFromStudentNumber(int studentNumber) throws ClassNotFoundException, SQLException, RemoteException{
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


	public int getStudentNumber(String nome, String cognome) {
		try {
			ArrayList<Map<String, Object>> hm;
			Object[] param = {nome, cognome};
			hm = socket.function("get_matricola_docente", param);
			int studentNumber = (int) hm.get(0).get("matricola");
			return studentNumber;
		} catch (ClassNotFoundException | SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

 }