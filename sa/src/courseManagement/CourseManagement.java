package courseManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.*;
import courseContentManagement.Course;
import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
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
	private RemoteInterface socket;
	private AnotherInterface server;
	protected List<Course> courses = new ArrayList<Course>();
	
	/** Costruttore: assegnamento del socket 
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException */
	public CourseManagement () throws ClassNotFoundException, MalformedURLException, RemoteException, NotBoundException {
		server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		System.out.println(i);
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
	}
	
	/** Importazione dei dati dei corsi da file CSV e salvataggio 
	 * 	degli stessi all'interno del database  
	 * @throws RemoteException 
	 * @throws NotBoundException */
	public void dataInput (String CSV_fileName) throws ClassNotFoundException, SQLException, RemoteException, NotBoundException {
        Path pathToFile = Paths.get(CSV_fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {		
            String line = br.readLine();			
            while (line != null) {					
                String[] attributes = line.split(",");
                Course course = new Course();
                course.setCourseCode(Integer.parseInt(attributes[0]));
                course.setName(attributes[1]);
                course.setYear(Integer.parseInt(attributes[2]));
                course.setFaculty(attributes[3]);
                course.setDescription(attributes[4]);
                course.setWeight(Integer.parseInt(attributes[5]));
                course.setCreator(Integer.parseInt(attributes[6]));
                courses.add(course);					
                line = br.readLine();				
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        	}
        for (Course a: courses) {						
        	Object[] params = {a.courseCode, a.name, (Integer) a.activation_year, a.faculty, a.description, a.weight, a.creator};
        	socket.function("import_dati_corsi", params);
        	}
	}
	
	/** Creazione di un nuovo corso nel database	
	 * @throws RemoteException */
	public void createCourse (Course c) throws ClassNotFoundException, SQLException, RemoteException {				
        Object[] params = {c.courseCode, c.name, c.activation_year, c.faculty, c.description, c.weight, c.creator};
        socket.function("import_dati_corsi", params);
	} 
	
	/** Eliminazione dei dati relativi ad un corso presente nel database	
	 * @throws RemoteException */
	protected void cancelCourse (Course c) throws ClassNotFoundException, SQLException, RemoteException {
		Object[] params = {c.courseCode};
		socket.function("elimina_dati_corsi", params);
	} 
	
	/** Modifica dei dati relativi ad un corso presente nel database	
	 * @throws RemoteException */
	public void modifyCourse (Course c) throws ClassNotFoundException, SQLException, RemoteException {
		Object[] params = {c.courseCode, c.name, c.activation_year, c.faculty, c.description, c.weight, c.creator};
		socket.function("modifica_dati_corsi", params);
	} 

	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	
	 * @throws RemoteException */
	public void coursesAssignment(int studentNumber) throws ClassNotFoundException, SQLException, RemoteException {
		Object[] params = {studentNumber};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di uno studente al suo piano di studi	
	 * @throws RemoteException */
	public void coursesAssignment(User student) throws ClassNotFoundException, SQLException, RemoteException {
		Object[] params = {student.getInfo().student_number};
		socket.function("assegna_studenti", params);
	}
	
	/** Assegnamento dei corsi di competenza di piu' studenti ai loro piano di studi	
	 * @throws RemoteException */
	public void coursesAssignment(List<User> students) throws ClassNotFoundException, SQLException, RemoteException {
		for (User student : students) {
			Object[] params = {student.getInfo().student_number};
			socket.function("assegna_studenti", params);
		}
	}
	
	/** Assegnamento di un corso di competenza ad un docente	
	 * @throws RemoteException */
	public void coursesAssignment(User professor, Course c) throws ClassNotFoundException, SQLException, RemoteException {
		Object[] params = {professor.getInfo().student_number, c.name };
		socket.function("assegna_docente", params);
	}
	
	/** Assegnamento di piu' corsi ad un docente	
	 * @throws RemoteException */
	public void coursesAssignment(User professor, List<Course> courseList) throws ClassNotFoundException, SQLException, RemoteException {
		for (Course a : courseList) {
			Object[] params = {professor.getInfo().student_number, a.name };
			socket.function("assegna_docente", params);
		}
	}
	
	/** Ricerca dei docenti che tengono un determinato corso	
	 * 	@return	array con le matricole dei docenti cercati	*/
	public ArrayList<User> whoTeachCourse (Course c) throws Exception {
		Object[] params = {c.courseCode};
		ArrayList<Map<String, Object>> professorNumbers = socket.function("ricerca_docenti", params);
		ArrayList<Object> professorNumber = new ArrayList<Object>();
		for (Map<String, Object> a : professorNumbers) {
			professorNumber.add((int) a.get("docente")); 
			}
		return this.professorsData(professorNumber);
	} 
	
	/** Estrae i dati principali di un docente dal database e li memorizza 
	 * 	@return lista dei docenti	*/
	private ArrayList<User> professorsData(ArrayList<Object> professorNumber) throws Exception {
		ArrayList<User> professors = new ArrayList<User>();
		ArrayList<Map<String,Object>> professorData;
		for (Object a: professorNumber) {
			professorData = socket.function("get_dati_docente", new Object[] {a});
			for (Map<String, Object> b : professorData) {
				User u = new User();
				u.createFromDbResult(b);
				professors.add(u);
			}
		}
		return professors;
	}
	
	/** Verifica se uno studente risulta iscritto a un corso	
	 * 	@return check di controllo	
	 * @throws RemoteException */
	public boolean studentEnrolledInTheCourse (User student, Course c) throws ClassNotFoundException, SQLException, RemoteException {
		boolean response = false;
		Object[] params = {student.getInfo().student_number, c.courseCode};
		ArrayList<Map<String, Object>> outcome = socket.function("verifica_iscrizione_studente", params);
		for (Map<String, Object> a : outcome) {
			response = a.containsValue(true); 	}
		return response;
	}
	
	/** Permette a uno studente di iscriversi a un corso	
	 * @throws Exception */
	public void signUpForCourse (User student, Course c) throws Exception {
		Object[] params = {student.getInfo().student_number, (short) c.courseCode};
		socket.function("iscrivi_studente_al_corso", params);
		ArrayList<User> prof = whoTeachCourse(c);
		Notifier.send_professor_email("usr", "pwd", "utente", "iscrizione a corso", "iscritto a");
		for(User d : prof){
				Notifier.send_professor_email("usr", "pwd", "docente", "iscrizione a corso", "iscritto a");
		}
	}
	
	/** Creazione di una nuova sessione relativa a un corso	
	 * @throws RemoteException */
	public void createSession (User u, Course c) throws ClassNotFoundException, SQLException, RemoteException {
        socket.query("DELETE FROM accesso_corso WHERE matricola = "+ u.getInfo().student_number + " AND codice_corso = " + c.courseCode + " AND fine_accesso IS NULL;");
        socket.query("INSERT INTO accesso_corso (matricola, codice_corso, inizio_accesso) VALUES (" + u.getInfo().student_number + ", " + c.courseCode + ", NOW());");
	} 
	
	/** Terminazione di una sessione relativa a un corso	
	 * @throws RemoteException */
	public void deleteSession (User u, Course c) throws ClassNotFoundException, SQLException, RemoteException {
		socket.query("UPDATE accesso_corso SET fine_accesso = NOW() WHERE matricola = " + u.getInfo().student_number + " AND codice_corso = " + c.courseCode + " AND fine_accesso IS NULL");
	} 
}
