package authService;

/**
* Gestione della fase di autenticazione di un utente con la piattaforma.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 2.0
*/

import java.sql.SQLException;
import java.util.*;
import java.math.BigInteger;
import java.security.*;
import javax.mail.*;

public class AuthenticationService {
	
	class InfoFromDb {
		private int attivation_code;
		private String login_attempts;
		private String pwd_hash;
		private String mail;
		private boolean isBlocked = false;	}
	
	//CAMPI
	InfoFromDb user = new InfoFromDb();
	private SocketDb socket;
	private int matricola;
	
	/** Costruttore: assegnamento del socket */
	public AuthenticationService (SocketDb s) throws ClassNotFoundException, SQLException {
		socket = SocketDb.getInstanceDb();
	}
	
	/** Effettua il login dell'utente con la piattaforma
	 * @return flag di avvenuta creazione di una nuova sessione */
	public boolean login (String mail, String pass) throws Exception { 
		if (controlloEsistenzaUtente(mail)) {
			this.getInfoFromDb();
			if (controlloTentativi())
				if (controlloUtenteAttivo())
					if (controlloCredenziali(pass, mail)) 
						return Sessione.getInstance().create(matricola);
		}
		return false;
	}
	
	/** Verifica se i dati inseriti sono corretti e se si tratta del primo tentativo di accesso
	 * @return check di controllo */
	private boolean attivation (String mail, String pass) throws Exception {
		return (this.controlloCredenziali(pass, mail) && user.login_attempts == null);
	}
	
	/** Memorizza la nuova password inserita (in fase di attivazione o nel servizio di password dimenticata)
	 * @return void	*/
	private void storeNewPassword (String new_pass) throws Exception {
		Object[] arg = {this.toHash(new_pass), user.mail};
		this.socket.function("reset_password", arg);
	}
	
	/** Azzera i tentativi di accesso dell'utente
	 * @return void	*/
	private void resetLoginAttempts () throws Exception {
		Object[] arg = {user.pwd_hash};
		this.socket.function("reset_login", arg);
	}
	
	/** Incrementa di un'unita' i tentativi di accesso dell'utente
	 * @return void	*/
	private void loginAttemptsIncrease () throws Exception {
		Object[] arg = {user.mail};
		this.socket.function("incremento_login", arg);
	}
	
	/** Genera un codice di attivazione generico a 8 cifre
	 * @return codice di attivazione */
	private int createAttivationCode () {
		SecureRandom n = new SecureRandom();				
		return n.nextInt(99999999);
    }
	
	/** Invia via mail un nuovo codice di attivazione e una nuova password
	 * @return void */
	private void sendNewLoginCredentials (String codice_att, String new_pass) throws SendFailedException, MessagingException {
		Notifier.send_uninsubria_email("mailIstituzionale", "pwdmailIstit", user.mail,
				 "NUOVA PWD", "PWD: "+new_pass+" CODATTIVAZIONE: "+ codice_att);
	}
	
	/** Controlla se il codice di attivazione inserito e' corretto
	 * @return check di controllo */
	private boolean controlloCodiceAttivazione (int codice_inserito) {
		return (codice_inserito == user.attivation_code);
	}
	
	/** Verifica se un utente e' bloccato
	 * @return stato dell'utente */
	private boolean controlloStatoUtente () throws ClassNotFoundException, SQLException {
		user.isBlocked = (!this.controlloTentativi());
		return user.isBlocked;
	}
	
	/** Genera la versione in hashcode di una stringa, applicando l'algoritmo MD5
	 * @return stringa in hashcode */
	private String toHash(String stringa) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5"); 	// creiamo un'istanza e passiamo come riferimento la funzione di hash da usare sulla stringa (MD5 nel nostro caso)
		byte [] p = m.digest(stringa.getBytes()); 				// computiamo la password fornita 
		BigInteger number = new BigInteger(1, p); 				// convertiamo l'array di byte ottenuto in BigInteger, perche' un oggetto BigInteger è immutabile (evitiamo quindi che il valore ottenuto subisca modifiche)
		return number.toString(16).toUpperCase(); 				// convertiamo infine il BigInteger in formato testuale e in maiuscolo (l'argomento '16' indica la base esadecimale)
	}
	
	/**	Genera una stringa casuale composta da 16 caratteri (tra lettere e cifre)
	 * @return stringa random */
	private String randomString () {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { 
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index)); 
        }
        return salt.toString();	
	}
	
	/** Fornisce le informazioni dell'utente dal database, usando la matricola 
	 * @return void */
	private void getInfoFromDb () throws ClassNotFoundException, SQLException {
		String sqlScript = "SELECT passwordHash, codice_attivazione, tentativi_login, email FROM utente WHERE matricola = '" + matricola + "';";
		ArrayList<Map<String, Object>> takeInfo = new ArrayList<Map<String, Object>>();
		takeInfo = socket.query(sqlScript);
		for (Map<String, Object> a : takeInfo) {
			user.pwd_hash = (String) a.get("password_hash");
			user.attivation_code = (int) a.get("codice_attivazione");
			user.login_attempts = (String) a.get("tentativi_login");
			user.mail = (String) a.get("email");
		}
	}
	
	/** Verifica che l'utente sia memorizzato nel database 
	 * @return check di controllo */
	private boolean controlloEsistenzaUtente (String email_digitata) throws Exception { 
		Object[] o = {email_digitata};
		ArrayList<Map<String,Object>> result_set = socket.function("get_matricola_from_mail", o);
		if (!result_set.isEmpty()) {	
			for (Map<String, Object> a : result_set)
				matricola = (int) a.get("matricola");
		}	// se la matricola e' stata restituita dalla funzione allora la memorizziamo
		return (!result_set.isEmpty());
			
	}
	
	/** Verifica che il numero di tentativi di accesso sia inferiore o uguale a 10
	 * @return check di controllo */
	private boolean controlloTentativi() throws ClassNotFoundException, SQLException {
		return (Integer.parseInt(user.login_attempts) <= 10);
	}
	
	/**  Verifica che il profilo dell'utente sia gia' attivo
	 * @return check di controllo */
	private boolean controlloUtenteAttivo() throws ClassNotFoundException, SQLException { 
		return (user.login_attempts != null);
	}
	
	/** Verifica che le credenziali inserite coincidano con quelle presenti nel database
	 * @return check di controllo */
	private boolean controlloCredenziali (String pass, String mail_digitata) throws Exception { 
		return (this.toHash(pass) == user.pwd_hash && mail_digitata == user.mail);
	}
}