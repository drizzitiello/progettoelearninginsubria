package courseManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

import courseContentManagement.Course;
import notifier.Notifier;
import socketDb.SocketDb;
import user.User; 

/** Gestione dei corsi.
* 
*	<P>	Progetto d'esame: SeatIn.
*	<P>	Obiettivo: Realizzazione di una semplice piattaforma di elearning
*
* @author Davide Stagno - Daniele Rizzitiello - Marco Macri'
* @version 1.3	*/

public class CourseManagement {
	
	//CAMPI
	private SocketDb socket;
	protected List<Course> courses = new ArrayList<Course>();
	
	/** Costruttore: assegnamento del socket */
	public CourseManagement () throws ClassNotFoundException {
		socket = SocketDb.getInstanceDb();
	}
	
	/** Importazione dei dati dei corsi da file CSV e salvataggio 
	 * 	degli stessi all'interno del database  */
	public void dataInput (String CSV_fileName) throws ClassNotFoundException, SQLException {
        Path pathToFile = Paths.get(CSV_fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {		// creiamo un'istanza di BufferedReader
            String line = br.readLine();			// leggiamo la prima riga del file
            while (line != null) {					// fino a quando non abbiamo letto tutte le righe
                String[] attributes = line.split(",");
                Course course = new Course();
                course.setCourseCode(Integer.parseInt(attributes[0]));
                course.setName(attributes[1]);
                course.setYear(Integer.parseInt(attributes[2]));
                course.setFaculty(attributes[3]);
                course.setDescription(attributes[4]);
                course.setWeight(Integer.parseInt(attributes[5]));
                course.setCreator(Integer.parseInt(attributes[6]));
                courses.add(course);					// aggiungiamo il corso all'array list
                line = br.readLine();				// leggiamo la prossima riga
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        for (Course a: courses) {						// memorizziamo ogni corso nel database
        	Object[] params = {a.courseCode, a.name, (Integer) a.activation_year, a.faculty, a.description, a.weight, a.creator};
        	socket.function("import_dati_corsi", params);
        }
	}
	
	/** Creazione di un nuovo corso nel database	*/
	public void createCourse (Course c) throws ClassNotFoundException, SQLException {				
        Object[] params = {c.courseCode, c.name, c.activation_year, c.faculty, c.description, c.weight, c.creator};
        socket.function("import_dati_corsi", params);
	} 
	
	/** Eliminazione dei dati relativi ad un corso presente nel database	*/
	protected void cancelCourse (Course c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.courseCode};
		socket.function("elimina_dati_corsi", params);
	} 
	
	/** Modifica dei dati relativi ad un corso presente nel database	*/
	public void modifyCourse (Course c) throws ClassNotFoundException, SQLException {
		Object[] params = {c.courseCode, c.name, c.activation_year, c.faculty, c.description, c.weight, c.creator};
		socket.function("modifica_dati_corsi", params);
	} 


	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	*/
	public void coursesAssignment(int matricola) throws ClassNotFoundException, SQLException {
		Object[] params = {matricola};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	*/
	public void coursesAssignment(User studente) throws ClassNotFoundException, SQLException {
		Object[] params = {studente.getInfo().student_number};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di piu' studenti al loro piano di studi	*/
	public void coursesAssignment(List<User> studenti) throws ClassNotFoundException, SQLException {
		for (User studente : studenti) {
			Object[] params = {studente.getInfo().student_number};
			socket.function("assegna_studenti", params);
		}
	}
	
	/** Assegnamento di un corso di competenza ad un docente	*/
	public void coursesAssignment(User docente, Course c) throws ClassNotFoundException, SQLException {
		Object[] params = {docente.getInfo().student_number, c.name };
		socket.function("assegna_docente", params);
	}
	
	/** Assegnamento di piu' corsi ad un docente	*/
	public void coursesAssignment(User docente, List<Course> lista_corsi) throws ClassNotFoundException, SQLException {
		for (Course a : lista_corsi) {
			Object[] params = {docente.getInfo().student_number, a.name };
			socket.function("assegna_docente", params);
		}
	}
	
	/** Ricerca dei docenti che tengono un determinato corso	
	 * 	@return	array con le matricole dei docenti cercati	*/
	public ArrayList<User> whoTeachCourse (Course c) throws Exception {
		Object[] params = {c.courseCode};
		ArrayList<Map<String, Object>> matricolaDocente = socket.function("ricerca_docenti", params);
		
		ArrayList<Object> matricole = new ArrayList<Object>();
		for (Map<String, Object> a : matricolaDocente) {
			matricole.add((int) a.get("docente")); 
			}
		return this.professorsData(matricole);
	} 
	
	/** Estrae i dati principali di un docente dal database e li memorizza 
	 * 	@return lista dei docenti	*/
	private ArrayList<User> professorsData(ArrayList<Object> matricole) throws Exception {
		ArrayList<User> docenti = new ArrayList<User>();
		ArrayList<Map<String,Object>> datiDocente;
		for (Object a: matricole) {
			datiDocente = socket.function("get_dati_docente", new Object[] {a});
			for (Map<String, Object> b : datiDocente) {
				User u = new User();
				u.createFromDbResult(b);
				docenti.add(u);
			}
		}
		return docenti;
	}
	
	/** Verifica se uno studente risulta iscritto a un corso	
	 * 	@return check di controllo	*/
	public boolean studenteEnrolledInTheCourse (User studente, Course c) throws ClassNotFoundException, SQLException {
		boolean risposta = false;
		Object[] params = {studente.getInfo().student_number, c.courseCode};
		ArrayList<Map<String, Object>> esito = socket.function("verifica_iscrizione_studente", params);
		for (Map<String, Object> a : esito) {
			risposta = a.containsValue(true); 	}
		return risposta;
	}
	
	/** Permette a uno studente di iscriversi a un corso	
	 * @throws Exception */
	public void signUpForCourse (User studente, Course c) throws Exception {
		Object[] params = {studente.getInfo().student_number, (short) c.courseCode};
		socket.function("iscrivi_studente_al_corso", params);
		//invio email a utente stesso e ai docenti titolari del corso
		ArrayList<User> doc = whoTeachCourse(c);
		Notifier.send_professor_email("usr", "pwd", "utente", "iscrizione a corso", "iscritto a");
		for(User d : doc){
				Notifier.send_professor_email("usr", "pwd", "docente", "iscrizione a corso", "iscritto a");
		}
	}
	
	/** Creazione di una nuova sessione relativa a un corso	*/
	public void createSession (User u, Course c) throws ClassNotFoundException, SQLException {
        socket.query("DELETE FROM accesso_corso WHERE matricola = "+ u.getInfo().student_number + " AND codice_corso = " + c.courseCode + " AND fine_accesso IS NULL;");
        socket.query("INSERT INTO accesso_corso (matricola, codice_corso, inizio_accesso) VALUES (" + u.getInfo().student_number + ", " + c.courseCode + ", NOW());");
	} 
	
	/** Terminazione di una sessione relativa a un corso	*/
	public void deleteSession (User u, Course c) throws ClassNotFoundException, SQLException {
		socket.query("UPDATE accesso_corso SET fine_accesso = NOW() WHERE matricola = " + u.getInfo().student_number + " AND codice_corso = " + c.courseCode + " AND fine_accesso IS NULL");
	} 
}
// STORED PROCEDURE
/* 
-- FUNCTION: public.iscrivi_studente_al_corso(integer, smallint)

-- DROP FUNCTION public.iscrivi_studente_al_corso(integer, smallint);

CREATE OR REPLACE FUNCTION public.iscrivi_studente_al_corso(
	matr integer,
	cod_corso smallint)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

BEGIN
	INSERT INTO iscritto_a (matricola, codice_corso)
	VALUES (matr, cod_corso);
END;

$BODY$;

ALTER FUNCTION public.iscrivi_studente_al_corso(integer, smallint)
    OWNER TO postgres;
*/
