package authService;

/**
* Gestione della fase di autenticazione di un utente con la piattaforma.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 2.1
*/

import java.sql.SQLException;
import java.util.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.*;
import javax.mail.*;

import interfaces.RemoteInterface;
import notifier.Notifier;
import session.Session;
import socketDb.SocketDb;

public class AuthenticationService {
	
	class InfoFromDb {
		private int activation_code;
		private Integer login_attempts;
		private String pwd_hash;
		private int student_number;
		private boolean isBlocked = false;	
		
		/** Fornisce le informazioni dell'utente dal database	
		 * @throws RemoteException */
		private void getInfoFromDb (String email) throws ClassNotFoundException, SQLException, RemoteException {
			AuthenticationService.email=email;
			String sqlScript = "SELECT password_hash, codice_attivazione, tentativi_login, matricola"
					+ " FROM utente WHERE email = '" + email + "';";
			ArrayList<Map<String, Object>> takeInfo = new ArrayList<Map<String, Object>>();
			takeInfo = socket.query(sqlScript);
			for (Map<String, Object> a : takeInfo) {
				user.pwd_hash = (String) a.get("password_hash");
				user.activation_code = (int) a.get("codice_attivazione");
				user.login_attempts = (Integer) a.get("tentativi_login");
				user.student_number = (int) a.get("matricola");
			}
		}
	}
	
	//CAMPI
	InfoFromDb user = new InfoFromDb();
	private RemoteInterface socket;
	private int studentNumber;
	private static String email;
	
	/** Costruttore: assegnamento del socket 
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException */
	public AuthenticationService () throws ClassNotFoundException, SQLException, MalformedURLException, RemoteException, NotBoundException {
		socket = (RemoteInterface) Naming.lookup ("rmi://localhost/SocketDb");
	}
	
	/** Effettua il login dell'utente con la piattaforma
	 * @return messaggio con esito dell'operazione di login */
	public String login (String mail, String pass) throws Exception { 
		email=mail;
		if (controlUserExistence(mail)) {
			user.getInfoFromDb(mail);
			if (controlActiveUser()) {
				if (controlAttempts()) {
					if (controlCredentials(pass, mail)) {
						resetLoginAttempts();
						Session.getInstance().create(user.student_number);
						return "Credenziali corrette";
					}
					else 
						{
							loginAttemptsIncrease();
							return "Password errata";
						}
				}
				else {
					return "Numero eccessivo di tentativi: profilo bloccato, contattare l'admin";
				}
			}
			else {
				if(activation(mail, pass)) return "Procedura Attivazione";
				else return "L'utente non e' stato attivato e la password temporanea fornita e' errata";
			}
		}
		else {
			return "L'email inserita non e' registrata";
		}
	}
	
	/** Verifica se i dati inseriti sono corretti e se si tratta del primo tentativo di accesso
	 * @return check di controllo */
	private boolean activation (String mail, String pass) throws Exception {
		return (this.controlCredentials(pass, mail) && user.login_attempts == null);
	}
	
	/** Memorizza la nuova password inserita (in fase di attivazione o nel servizio di password dimenticata) */
	public void storeNewPassword (String new_pass) throws Exception {
		Object[] arg = {this.toHash(new_pass), email};
		this.socket.function("reset_password", arg);
		Notifier.send_professor_email("mailIsituzionale", "pwd istituzionale",
				email, "Nuova pwd", "La tua nuova pwd e' "+new_pass);
	}
	
	/** Azzera i tentativi di accesso dell'utente	*/
	public void resetLoginAttempts () throws Exception {
		Object[] arg = {email};
		this.socket.function("reset_login", arg);
	}
	
	/** Incrementa di un'unita' i tentativi di accesso dell'utente	*/
	private void loginAttemptsIncrease () throws Exception {
		Object[] arg = {email};
		if(user.login_attempts!=null)user.login_attempts++;
		this.socket.function("incremento_login", arg);
	}
	
	/** Genera un codice di attivazione generico a 8 cifre
	 * @return codice di attivazione */
	public static int createActivationCode () {
		SecureRandom n = new SecureRandom();				
		return n.nextInt(99999999);
    }
	
	/** Invia via mail un nuovo codice di attivazione e una nuova password	*/
	public void sendNewLoginCredentials (String email) throws SendFailedException, MessagingException, Exception {
		Notifier.sendSystemMail(email, "NUOVA PWD", "PWD: "+randomString()+" CODATTIVAZIONE: "
				+ createActivationCode());
	}
	
	/** Controlla se il codice di attivazione inserito e' corretto
	 * @return check di controllo */
	private boolean controlActivationCode (int codeInserted) {
		return (codeInserted == user.activation_code);
	}
	
	/** Verifica se un utente e' bloccato
	 * @return stato dell'utente */
	private boolean controlUserState () throws ClassNotFoundException, SQLException {
		user.isBlocked = (!this.controlAttempts());
		return user.isBlocked;
	}
	
	/** Genera la versione in hashcode di una stringa, applicando l'algoritmo MD5
	 * @return stringa in hashcode */
	private String toHash(String string) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5"); 	// creiamo un'istanza e passiamo come riferimento la funzione di hash da usare sulla stringa (MD5 nel nostro caso)
		byte [] p = m.digest(string.getBytes()); 		// computiamo la password fornita 
		BigInteger number = new BigInteger(1, p); 		// convertiamo l'array di byte ottenuto in BigInteger, perche' un oggetto BigInteger e' immutabile (evitiamo quindi che il valore ottenuto subisca modifiche)
		return number.toString(16).toUpperCase(); 		// convertiamo infine il BigInteger in formato testuale e in maiuscolo (l'argomento '16' indica la base esadecimale)
	}
	
	/** Genera una stringa casuale composta da 16 caratteri (tra lettere e cifre) utilizzabile come password 
	 * @return stringa random */
	public static String randomString () {
		StringBuilder finalString = new StringBuilder();
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { 
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index)); 
        }
        String result = new String(salt.toString());
        char [] chr= result.toCharArray();
        for(int i=0; i<16; i++)	{
        	if (Character.isLetter(chr[i]) && i%2==0) 
        		chr[i] = Character.toUpperCase(chr[i]); 
        	if (Character.isLetter(chr[i]) && i%2!=0) 
        		chr[i] = Character.toLowerCase(chr[i]);
       	}
        for(int i=0; i<16; i++)	
        	finalString.append(Character.toString(chr[i]));
	    return finalString.toString();	
	}	
	
	/** Verifica che l'utente sia memorizzato nel database 
	 * @return check di controllo */
	private boolean controlUserExistence (String mailInserted) throws Exception { 
		Object[] o = {mailInserted};
		ArrayList<Map<String,Object>> result_set = socket.function("get_matricola_from_mail", o);
		if (!result_set.isEmpty()) {	
			for (Map<String, Object> a : result_set)
				studentNumber = (int) a.get("matricola");
		}	// se la matricola e' stata restituita dalla funzione allora la memorizziamo
		return (!result_set.isEmpty());
			
	}
	
	/** Verifica che il numero di tentativi di accesso sia inferiore a 10
	 * @return check di controllo */
	private boolean controlAttempts() {
		return (user.login_attempts < 10);
	}
	
	/**  Verifica che il profilo dell'utente sia gia' attivo
	 * @return check di controllo */
	private boolean controlActiveUser() { 
		return (user.login_attempts != null);
	}
	
	/** Verifica che le credenziali inserite coincidano con quelle presenti nel database
	 * @return check di controllo */
	private boolean controlCredentials (String pass, String mailInserted) throws Exception {
		if(this.toHash(pass).equals(user.pwd_hash) && mailInserted.equals(this.email)) {return true;}
		else return false;
	}
}
