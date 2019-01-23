package userManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import courseManagement.CourseManagement;
import notifier.Notifier;
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
    private SocketDb socket;
    private Session session;
    private boolean enabled = false;
    
    /**
	 * Istanzia oggetti relativi al SocketDb di sistema e alla Sessione corrente.
     * Imposta il flag enabled che abilita l'uso del componente ai soli utenti amministratori.
     * @throws Exception 
	 */
    public UserManager() throws Exception {
        this.socket = SocketDb.getInstanceDb();
        this.session = Session.getInstance();
        this.enabled = this.session.info().userType == User.admin;
    }


    /**
	 * Si occupa della modifica dei dati utente partendo da un oggetto Utente 
     * la modifica e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta modifica dei dati
     * @throws SQLException
     * @throws ClassNotFoundException 
	 */
    public boolean modifiyUserData(User user) throws SQLException, ClassNotFoundException{
        if(!user.created() || !enabled)
            return false;


        Object[] p = { user.getInfo().name,
                       user.getInfo().surname,
                       user.getInfo().email,
                       (short) (int) user.getInfo().userType,
                       user.getInfo().registrationYear,
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
	 */
    public boolean deleteUser(User user) throws SQLException, ClassNotFoundException{
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
	 */
    public boolean unlockUser(User user) throws SQLException, ClassNotFoundException{
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
	 */
    public boolean createUser(UserInfo info) throws SQLException, ClassNotFoundException{
        if(!this.enabled)
        return false;

        String randomPassword = AuthenticationService.randomString();
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

     /**
	 * Importa utenti tramite file Comma Separated (CSV),
     * L'import avente la prima riga riservata all'intestazione assumera' il seguente formato:
     * MATRICOLA, NOME, COGNOME, EMAIL, TIPOUTENTE[S, D, A], ANNOIMMATRICOLAZIONE, CORSOLAUREA, STATOCARRIERA, STRUTTURARIF
     * l'operazione e' riservata ai soli utenti amministratori
	 *
	 * @return	flag di avvenuta importazione
     * @throws SQLException
     * @throws ClassNotFoundException 
	 */
    public boolean csvImportUser(String path) throws SQLException, ClassNotFoundException{
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
                            i.userType = User.student;
                        break;

                        case "D":
                            i.userType = User.professor;
                        break;

                        case "A":
                            i.userType = User.admin;
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
}