package userManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import socketDb.SocketDb;
import user.User;
import user.User.UserInfo;
import authService.AuthenticationService;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Resource;
import courseManagement.CourseManagement;
import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import notifier.Notifier;
import server.Server;
import session.Session;

/**
* Modifica e inserimento massivo delle informazioni utente.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/


public class UserManager {
    
    /* Dichiarazione dei componenti di servizio */
	private RemoteInterface socket;
	private AnotherInterface server;
    private Session session;
    private boolean enabled = false;
    
    /**
	 * Istanzia oggetti relativi al SocketDb di sistema e alla Sessione corrente.
     * Imposta il flag enabled che abilita l'uso del componente ai soli utenti amministratori.
     * @throws Exception 
	 */
    public UserManager() throws Exception {
    	server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		System.out.println(i);
		server.starting();
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
        this.session = Session.getInstance();
        this.enabled = this.session.info().userType == User.ADMIN;
    }
    
    public UserManager(Server admin) throws Exception {
    	server = admin; 
    	server.starting();
		socket = admin.adminInstanceDb;
        this.enabled = true;
    }


    /**
	 * Si occupa della modifica dei dati utente partendo da un oggetto Utente 
     * la modifica e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta modifica dei dati
     * @throws SQLException
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean modifiyUserData(User user) throws SQLException, ClassNotFoundException, RemoteException{
        if(!user.created() || !enabled)
            return false;


        Object[] p = { user.getInfo().name,
                       user.getInfo().surname,
                       user.getInfo().email,
                       (short) (int) user.getInfo().userType,
                       (short) (int) user.getInfo().registrationYear,
                       user.getInfo().faculty,
                       user.getInfo().careerStatus,
                       user.getInfo().referenceStructure,
                       user.getInfo().student_number
                     };
        
        this.socket.function("modifica_dati_utente", p);
        
        return true;
    }



     /**
	 * Elimina un utente registrato partendo da un oggetto Utente 
     * l'eliminazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta eliminazione utente
     * @throws SQLException
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean deleteUser(User user) throws SQLException, ClassNotFoundException, RemoteException{
        if(!user.created() || !this.enabled)
            return false;

        Object[] p = { user.getInfo().student_number };
        this.socket.query("DELETE FROM utente WHERE matricola = ?", p);
        
        return true;
    }


    
     /**
	 * Sblocca un profilo utente bloccato per tentativi massimi di accessi con credeniali errate
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta eliminazione utente
     * @throws SQLException
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean unlockUser(User user) throws SQLException, ClassNotFoundException, RemoteException{
        if(!user.created() || !this.enabled)
            return false;

        Object[] p = { user.getInfo().student_number };
        
        this.socket.query("UPDATE utente SET tentativi_login = 0 WHERE matricola = ?", p);
        
        return true;
    }

    /**
	 * Creazione nuovo utente
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta creazione
     * @throws SQLException
     * @throws ClassNotFoundException 
     * @throws RemoteException 
	 */
    public boolean createUser(UserInfo info) throws SQLException, ClassNotFoundException, RemoteException{
        if(!this.enabled)
        return false;

        String randomPassword = AuthenticationService.randomString();
        int randomCodAttivaz = AuthenticationService.createActivationCode();

        Object[] p = {
                    info.name,
                    info.surname,
                    info.email,
                    (short) (int)  info.userType,
                    (short) (int)  info.registrationYear,
                    info.faculty,
                    info.careerStatus,
                    info.referenceStructure,
                    info.student_number,
                    randomPassword,
                    randomCodAttivaz
                    };
        
        this.socket.function("crea_utente", p);
        
        String body;
        body = "Ciao " + info.name + "! Ti diamo il benvenuto su SeatIn.\n\n";
        body += "Di seguito troverai le credenziali per accedere al portale:\n";
        body += "Utente: " + info.email + "\n";
        body += "Password: " + randomPassword + "\n";
        body += "Codice di attivazione: " + Integer.toString(randomCodAttivaz) + "\n";

        try {
			Notifier.sendSystemMail(info.email, "SeatIn", body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

        return true;
    }
    
    public boolean createUser(UserInfo info, String pwd) throws SQLException, ClassNotFoundException, RemoteException{
        if(!this.enabled)
        return false;

        int randomCodAttivaz = AuthenticationService.createActivationCode();

        Object[] p = {
                    info.name,
                    info.surname,
                    info.email,
                    (short) (int)  info.userType,
                    info.registrationYear,
                    info.faculty,
                    info.careerStatus,
                    info.referenceStructure,
                    info.student_number,
                    pwd,
                    randomCodAttivaz
                    };
        
        this.socket.function("crea_utente", p);
        
        String body;
        body = "Ciao " + info.name + "! Ti diamo il benvenuto su SeatIn.\n\n";
        body += "Di seguito troverai le credenziali per accedere al portale:\n";
        body += "Utente: " + info.email + "\n";
        body += "Password: " + pwd + "\n";
        body += "Codice di attivazione: " + Integer.toString(randomCodAttivaz) + "\n";

        try {
			Notifier.sendSystemMail(info.email, "SeatIn", body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

        return true;
    }

     /**
	 * Importa utenti tramite file Comma Separated (CSV),
     * L'import avente la prima riga riservata all'intestazione assumera' il seguente formato:
     * MATRICOLA, NOME, COGNOME, EMAIL, TIPOUTENTE[S, D, A], ANNOIMMATRICOLAZIONE, CORSOLAUREA, STATOCARRIERA, STRUTTURARIF
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta importazione
     * @throws SQLException
     * @throws ClassNotFoundException 
     * @throws NotBoundException 
     * @throws RemoteException 
     * @throws MalformedURLException 
	 */
    public boolean csvImportUser(String path) throws SQLException, ClassNotFoundException, MalformedURLException, RemoteException, NotBoundException{
        if(!this.enabled)
            return false;

            Path report = Paths.get(path);
            List<UserInfo> users = new ArrayList<UserInfo>();

            try (BufferedReader br = Files.newBufferedReader(report, StandardCharsets.US_ASCII)) {		
                br.readLine();			        // scarto la prima riga con intestazioni
                String line = br.readLine();	// leggo prima riga
                while (line != null) {			// loop righe
                    String[] attributes = line.split(",");
                    
                    User.UserInfo i = new User.UserInfo();
                    i.student_number = Integer.parseInt(attributes[0]);
                    i.name = attributes[1];
                    i.surname = attributes[2];
                    i.email = attributes[3];

                    switch(attributes[4]){
                        case "S":
                            i.userType = User.STUDENT;
                        break;

                        case "D":
                            i.userType = User.PROFESSOR;
                        break;

                        case "A":
                            i.userType = User.ADMIN;
                        break;
                    }
                    
                    if(attributes[5].equals("")) i.registrationYear = null;
                    else i.registrationYear = Integer.parseInt(attributes[5]);
                    i.faculty = attributes[6];
                    i.careerStatus = attributes[7];
                    i.referenceStructure = attributes[8];

                    users.add(i);
                    line = br.readLine();				// prossima riga
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            CourseManagement gc = new CourseManagement();

            int i = 1;
            for (UserInfo user: users) {				// memorizziamo ogni utente nel db
                if(createUser(user)){
                    gc.coursesAssignment(user.student_number);
                }else{
                    System.err.println("Errore csv file riga " + i);
                }
                i++;
            }

        return true;
    }

	public ArrayList<User> getProfessors() {
		try {
			ArrayList<Map<String, Object>> hm;
			Object[] param = {};
			hm = socket.function("get_docenti", param);
			ArrayList<User> professors = new ArrayList<User>();
			
			for(Map<String,Object> m : hm) {
				User us = new User();
				us.createFromStudentNumber((int) m.get("matricola"));
				professors.add(us);
			}
			
			return professors;
		} catch (ClassNotFoundException | SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public ArrayList<Integer> getRegisteredUsers() {
		try {
			Object[] params= {};
			ArrayList<Integer> studentNumbers = new ArrayList<Integer>();
			ArrayList<Map<String,Object>> hm = socket.function("getutentiregistrati", params);
			for(Map<String,Object> m : hm) {
				studentNumbers.add((int) m.get("matricola"));
			}
			return studentNumbers;
		} catch (ClassNotFoundException | SQLException | RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean dbContainsAdmin() {
		try {
			Object[] params= {};
			ArrayList<Map<String,Object>> hm = socket.function("db_contains_admin", params);
			if((boolean) hm.get(0).get("db_contains_admin")) {
				return true;
			}
			else {
				return false;
			}
		} catch (ClassNotFoundException | SQLException | RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> getStudentCourses(int studentNumber) {
		try {
			ArrayList<String> response = new ArrayList<String>();
			Object[] params= {studentNumber};
			ArrayList<Map<String,Object>> hm = socket.function("getcorsiutente", params);
			for(Map<String,Object> m : hm) {
				response.add((String) m.get("nome"));
			}
			return response;
		} catch (ClassNotFoundException | SQLException | RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
}