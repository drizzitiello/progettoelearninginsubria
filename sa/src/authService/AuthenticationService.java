package authService;

/**
* Gestione della fase di autenticazione di un utente con la piattaforma.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
*  
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.0
* 
*/

import java.sql.SQLException;
import java.util.*;

import Sessione.Sessione;
import notifier.Notifier;
import socketDb.SocketDb;

import java.io.*;
import java.math.BigInteger;
import java.security.*;

public class AuthenticationService {
	
	//CAMPI
	private SocketDb socket;
	private int matricola;
	private int codiceAttivazione;
	private int tentativi_login;
	private String passwordhash;
	private String forgot_pwd;
	private String email;
	public BufferedReader in = new BufferedReader( new InputStreamReader (System.in));
	
	/**
	 * Costruttore: assegnamento del socket
	 */
	public AuthenticationService (SocketDb s) throws ClassNotFoundException, SQLException {
		socket = SocketDb.getInstanceDb();
	}
	
	
	/** 
	 * Serve per leggere le credenziali in input. (Da integrare con l'interfaccia grafica) 
	 * @return array di String contenente mail e password
	 */
	public String[] inserisciCredenziali() throws IOException {
		String [] credenziali = new String[2];
		System.out.println("Inserire le credenziali per autenticarsi");
		System.out.println("E-mail: ");
		credenziali[0] = in.readLine();
		System.out.println("Password: ");
		credenziali[1] = in.readLine();
		return credenziali;
	}
	
	
	/** 
	 * Serve per leggere il codice di attivazione. (Da integrare con l'interfaccia grafica) 
	 * @return codice inserito 
	 */
	public int inserisciCodiceAttivazione() throws IOException {
		int codice;
		System.out.println("Inserire il codice di attivazione fornito via mail");
		System.out.println("CODICE: ");
		codice = Integer.parseInt(in.readLine());
		return codice;
	}
	
	
	/** 
	 * Serve per leggere la mail. (Da integrare con l'interfaccia grafica) 
	 * @return email utente
	 */
	public String inserisciEmail() throws IOException {
		String indirizzo_di_posta;
		System.out.println("Inserire l'e-mail: ");
		indirizzo_di_posta = in.readLine();
		return indirizzo_di_posta;
	}
	
	
	/** 
	 * Serve per leggere la password in fase di attivazione. (Da integrare con l'interfaccia grafica) 
	 * @return password
	 */
	public String inserisciPassword() throws IOException {
		String pwd;
		System.out.println("Inserire la nuova password: ");
		pwd = in.readLine();
		return pwd;
	}
	
	/**
	 * Effettua il login dell'utente con la piattaforma
	 * @return void 
	 */
	public void login (String mail, String pass) throws Exception { 
		controlloEsistenzaUtente(mail);
		if (!controlloTentativi())
			System.err.println(this.getError(2));
		else {
			if (!controlloUtenteAttivo()) {
				if (this.toHash(pass) == this.getPassword()) { 
					boolean checkCodice = false;
					while (!checkCodice) {
						codiceAttivazione = this.inserisciCodiceAttivazione(); // digitare il codiceAttivazione fornito per mail in fase di registrazione
						checkCodice = (codiceAttivazione == this.getAttCode());
						}
					passwordhash = this.toHash(inserisciPassword()); 			// chiediamo all'utente di digitare una nuova password (e la convertiamo in hashcode)
					Object[] arg = {passwordhash, email};
					this.socket.function("reset_password", arg); 				// salviamo la password nel database
						Object[] argomento = {passwordhash};
						this.socket.function("reset_login", argomento); 		// resettiamo i tentativi di login
						this.createSession(matricola); 									// si crea una nuova sessione
					}	else {
							String[] credenziali_reinserite = this.inserisciCredenziali();		
							this.login(credenziali_reinserite[0], credenziali_reinserite[1]);
					}
			}
			else {
				if (controlloCredenziali(pass)) {
					Object[] argomento = {passwordhash};
					this.socket.function("reset_login", argomento); // resettiamo i tentativi di login
					this.createSession(matricola);							// creiamo una nuova sessione
					}
				else {
					Object[] codice = {(short) codiceAttivazione};
					this.socket.function("incremento_login", codice);
					System.out.println("Password dimenticata? Si / No ");
					forgot_pwd = in.readLine();
					if (forgot_pwd.equalsIgnoreCase("Si")) {
						boolean checkMail=false;
						String mail_inserita="";
						while (!checkMail) {
							mail_inserita = this.inserisciEmail();
							ArrayList<Map<String, Object>> esito = new ArrayList<Map<String, Object>>();
							Object[] obj = {mail_inserita};
							esito = this.socket.function("controllo_mail", obj);
							for (Map<String, Object> a : esito) {
								checkMail = a.containsValue(true) ? true : false; 
							}
						}
						this.email = mail_inserita; 						// se la mail inserita e' corretta aggiorniamo il campo email
				        String newPassword = this.randomString(); 			// creiamo una password random
				        SecureRandom n = new SecureRandom();				// creiamo nuovo codice di attivazione
				        codiceAttivazione = n.nextInt(99999999); 			// generiamo un numero casuale a 8 cifre 
				        String newPasswordHash = this.toHash(newPassword);	// prima di salvare la password nel database, la convertiamo in hashcode
				        Object[] arg = {newPasswordHash, email};
						this.socket.function("reset_password", arg);
						//Notifier.send_uninsubria_email("mailIstituzionale", "pwdmailIstit", mail,
							//	 "NUOVA PWD", "PWD: "+newPassword+" CODATTIVAZIONE: "+ codiceAttivazione);
						Object[] argomento = {newPasswordHash};
						this.socket.function("reset_login", argomento); 	// resettiamo infine i tentativi di login
					}
					String[] credenziali_reinserite = this.inserisciCredenziali();		
					this.login(credenziali_reinserite[0], credenziali_reinserite[1]);	
				}
			}
		}
	}
	
	
	/** 
	 * Genera i messaggi di errore
	 * @return messaggio di errore
	 */
	private String getError(int codice) {
		String risposta="";
		if (codice=='1') 
			risposta = "Utente non trovato.";
		else if (codice=='2')
			risposta = "Attenzione: il profilo utente Ã¨ stato bloccato. Contattare un amministratore per riattivarlo.";
		return risposta;
	}
	
	
	/**
	 * Genera una nuova sessione
	 * @return sessione
	 * @throws Exception 
	 */
	private boolean createSession(int matricola) throws Exception {
		return Sessione.getInstance().create(matricola);
	}
	
	
	/**
	 * Genera la versione in hashcode di una stringa, applicando l'algoritmo MD5
	 * @return stringa in hashcode
	 */
	private String toHash(String stringa) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5"); 	// creiamo un'istanza e passiamo come riferimento la funzione di hash da usare sulla stringa (MD5 nel nostro caso)
		byte [] p = m.digest(stringa.getBytes()); 				// computiamo la password fornita 
<<<<<<< HEAD
		BigInteger number = new BigInteger(1, p); 				// convertiamo l'array di byte ottenuto in BigInteger, perchè un oggetto BigInteger è immutabile (evitiamo quindi che il valore ottenuto subisca modifiche)
		return number.toString(16).toUpperCase(); 							// convertiamo infine il BigInteger in formato testuale (l'argomento '16' indica la base esadecimale)
=======
		BigInteger number = new BigInteger(1, p); 				// convertiamo l'array di byte ottenuto in BigInteger, perchÃ¨ un oggetto BigInteger Ã¨ immutabile (evitiamo quindi che il valore ottenuto subisca modifiche)
		return number.toString(16); 							// convertiamo infine il BigInteger in formato testuale (l'argomento '16' indica la base esadecimale)
>>>>>>> branch 'dbcomplete' of https://github.com/drizzitiello/progettoelearninginsubria
	}
	
	
	/**
	 * Genera una stringa casuale composta da 16 caratteri (tra lettere e cifre)
	 * @return stringa random
	 */
	private String randomString () {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // lunghezza della stringa random
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index)); //concateniamo un carattere di indice casuale preso da SALTCHARS fino al raggiungimento di 16 caratteri
        }
        return salt.toString();	//restituiamo la stringa costruita in formato testuale
	}
	
	
	/**
	 * Fornisce la password dell'utente (in hashcode)
	 * @return passwordHash
	 */
	private String getPassword () throws ClassNotFoundException, SQLException {
		String password_from_db="";
		String sqlScript = "SELECT passwordHash FROM Utente WHERE matricola = '" + this.matricola + "';";
		ArrayList<Map<String, Object>> takePassword = new ArrayList<Map<String, Object>>();
		takePassword = socket.query(sqlScript);
		for (Map<String, Object> a : takePassword) {
			password_from_db = (String) a.get("passwordHash");
		}
		return password_from_db;
	}
	
	/**
	 * Fornisce i tentativi di login
	 * @return loginAttempts
	 */
	private int getLoginAttempts () throws ClassNotFoundException, SQLException {
		int tentativi=0;
		String sqlScript = "SELECT \"tentativiLogin\" FROM \"Utente\" WHERE matricola = '" + this.matricola + "';";
		ArrayList<Map<String, Object>> takeAttempts = new ArrayList<Map<String, Object>>();
		takeAttempts = socket.query(sqlScript);
		for (Map<String, Object> a : takeAttempts) 
			tentativi = (int) a.get("tentativilogin");
		return tentativi;
	}
	
	
	/**
	 * Fornisce il codice di attivazione 
	 * @return codice attivazione
	 */
	private int getAttCode () throws ClassNotFoundException, SQLException {
		int code=0;
		String sqlScript = "SELECT codiceAttivazione FROM Utente WHERE matricola = '" + this.matricola + "';";
		ArrayList<Map<String, Object>> takeCode = new ArrayList<Map<String, Object>>();
		takeCode = socket.query(sqlScript);
		for (Map<String, Object> a : takeCode) 
			code = (int) a.get("codiceAttivazione");
		return code;
	}
	
	
	/**
	 * Verifica che l'utente sia memorizzato nel database e ne fornisce i dati
	 * @return void
	 */
	private void controlloEsistenzaUtente (String email_digitata) throws Exception { 
		String [] credenziali_reinserite;
		Object[] o = {email_digitata};
		ArrayList<Map<String,Object>> result_set = socket.function("get_matricola_from_mail", o);
		boolean check = (!result_set.isEmpty());								// check e' true se e' stata trovata la matricola
		if (!check) { 															// fino a quando il result set e' vuoto 	
			System.err.println(this.getError(1));	    						// l'utente non compare nel database: restituiamo un messaggio di errore
			credenziali_reinserite = this.inserisciCredenziali();				// chiediamo all'utente di inserire nuovamente le credenziali
			email = credenziali_reinserite[0];									// aggiorniamo il campo email
			this.controlloEsistenzaUtente(email); 								// richiamiamo il controllo ricorsivamente, passando la nuova mail inserita
		} 
		// se superiamo il blocco if vuol dire che l'utente esiste nel database, e ne recuperiamo la matricola (che serve per prelevare le info dal database)
		for (Map<String, Object> a : result_set) {
			matricola = (int) a.get("matricola");
		}
		email = email_digitata; // superato il blocco if sappiamo che l'email digitata e' corretta
	}
	
	
	/**
	 * Verifica che il numero di tentativi di accesso sia inferiore o uguale a 10
	 * @return check sul controllo
	 */
	private boolean controlloTentativi() throws ClassNotFoundException, SQLException {
		tentativi_login = this.getLoginAttempts();
		if (tentativi_login <= 10)
			return true; 
		else 
			return false; 
	}
	
	
	/** 
	 * Verifica che il profilo dell'utente sia gia' attivo
	 * @return check di controllo
	 */
	private boolean controlloUtenteAttivo() throws ClassNotFoundException, SQLException { 
		String sqlScript = "SELECT \"tentativiLogin\" FROM \"Utente\" WHERE matricola = '" + this.matricola + "';";
		ArrayList<Map<String, Object>> takeAttempts = new ArrayList<Map<String, Object>>();
		takeAttempts = socket.query(sqlScript);
		if (!takeAttempts.isEmpty())  //Se il result set non contiene NULL allora l'utente e' attivato
			return true; 
		else 
			return false; 
	}
	
	
	/**
	 * Verifica che le credenziali inserite coincidano con quelle presenti nel database
	 * @return check di controllo
	 */
	private boolean controlloCredenziali (String pass) throws Exception { 
		String passwordhash = this.toHash(pass); 	// Convertiamo la password fornita in hashcode, usando l'algoritmo MD5
	    Object[] o = {passwordhash, email }; 		// Eseguiamo la stored procedure per confrontare le credenziali
		ArrayList<Map<String,Object>> result_set = socket.function("\"queryDati\"", o);
		boolean risultato = false;
		for (Map<String, Object> a : result_set) {
			risultato = a.containsValue(true) ? true : false; 
		}
		return risultato;		
	}

	
//Il metodo main ci serve per testare il funzionamento del componente AuthService
	public static void main (String[] args) throws Exception {
		SocketDb s = SocketDb.getInstanceDb();
		AuthenticationService auth = new AuthenticationService(s);
		String [] v = auth.inserisciCredenziali();
		auth.login(v[0], v[1]);
		System.out.println("Procedura di login eseguita correttamente! ");
		auth.in.close(); // chiusura dello stream
	}
}


/* --> STORED PROCEDURES <-- */

/* 
-- FUNCTION: public."queryDati"(character, character varying)

-- DROP FUNCTION public."queryDati"(character, character varying);

CREATE OR REPLACE FUNCTION public."queryDati"(
	pwd character,
	mail character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
IF (EXISTS (SELECT *
		   FROM Utente
		   WHERE passwordHash = @pwd AND email = @mail ))
THEN RETURN TRUE;
ELSE RETURN FALSE;
END IF;
END;

$BODY$;

ALTER FUNCTION public."queryDati"(character, character varying)
    OWNER TO postgres;

COMMENT ON FUNCTION public."queryDati"(character, character varying)
    IS 'Se le credenziali sono corrette la funzione restituisce true, altrimenti false.';
*/ 

//---------------------------------------------------------------------------------------

/* 
-- FUNCTION: public.get_matricola_from_mail(character varying)

-- DROP FUNCTION public.get_matricola_from_mail(character varying);

CREATE OR REPLACE FUNCTION public.get_matricola_from_mail(
	mail_utente character varying)
    RETURNS record
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN 
	SELECT matricola
	FROM Utente
	WHERE email = @mail_utente;
END;

$BODY$;

ALTER FUNCTION public.get_matricola_from_mail(character varying)
    OWNER TO postgres;

COMMENT ON FUNCTION public.get_matricola_from_mail(character varying)
    IS 'Restituisce il result set contenente la matricola dell''utente con mail corrispondente a quella passata come parametro in ingresso della funzione';
 */

//---------------------------------------------------------------------------------------

/* 
-- FUNCTION: public.reset_login(character)

-- DROP FUNCTION public.reset_login(character);

CREATE OR REPLACE FUNCTION public.reset_login(
	pwd character)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	UPDATE Utente
	SET tentativiLogin = 0
	WHERE passwordHash = @pwd;
END;

$BODY$;

ALTER FUNCTION public.reset_login(character)
    OWNER TO postgres;
*/

//---------------------------------------------------------------------------------------

/* 
-- FUNCTION: public.incremento_login(smallint)

-- DROP FUNCTION public.incremento_login(smallint);

CREATE OR REPLACE FUNCTION public.incremento_login(
	codice smallint)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	UPDATE Utente
	SET tentativiLogin = tentativiLogin + 1
	WHERE codiceAttivazione = @codice;
END;

$BODY$;

ALTER FUNCTION public.incremento_login(smallint)
    OWNER TO postgres;
*/

//---------------------------------------------------------------------------------------

/* 
-- FUNCTION: public.reset_password(character, character varying)

-- DROP FUNCTION public.reset_password(character, character varying);

CREATE OR REPLACE FUNCTION public.reset_password(
	pwd character,
	mail character varying)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	UPDATE Utente
	SET passwordHash = @pwd
	WHERE email = @mail;
END;

$BODY$;

ALTER FUNCTION public.reset_password(character, character varying)
    OWNER TO postgres;
 */
 
//---------------------------------------------------------------------------------------

/* 
-- FUNCTION: public.controllo_mail(character varying)

-- DROP FUNCTION public.controllo_mail(character varying);

CREATE OR REPLACE FUNCTION public.controllo_mail(
	mail character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	IF (EXISTS (SELECT *
		   FROM Utente
		   WHERE email = @mail))
	THEN RETURN TRUE;
	ELSE RETURN FALSE;
	END IF;
END;

$BODY$;

ALTER FUNCTION public.controllo_mail(character varying)
    OWNER TO postgres;

 */
