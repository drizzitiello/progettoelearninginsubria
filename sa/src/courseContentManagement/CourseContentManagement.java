package courseContentManagement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import graphics.HomePage;
import graphics.PaginaCorso;
import interfaces.RemoteInterface;
import session.Session;
import socketDb.SocketDb;

public class CourseContentManagement {
	
	private RemoteInterface socket;
	
	public CourseContentManagement() throws MalformedURLException, RemoteException, NotBoundException {
		socket = (RemoteInterface) Naming.lookup ("rmi://localhost/SocketDb");
	}
	
	public void uploadCourseMaterial(Course cor, Content con) throws Exception {
		cor.setContents(con);
	}
	
	public void createSection(Section s) throws Exception {
		String sql = "INSERT INTO sezione(codice_sezione, titolo, descrizione, is_pubblica, codice_corso, "
				+ "figlio_di, matricola) "
				+ "VALUES ("+s.sectionCode+", '"+s.title+"', '"+s.description+"', '"+s.visibility+"','"+
				s.courseCode+"', "+ " "+s.sonOf+", "+s.studentNumber+");";
		socket.query(sql);
	}
	
	public void cancelSection(int sectionCode) throws ClassNotFoundException, SQLException, RemoteException {
		String sql = "DELETE FROM sezione"
				+ " WHERE codice_sezione = "+sectionCode;
		socket.query(sql);
	}
	
	public Section getSection(int sectionCode) throws ClassNotFoundException, SQLException, RemoteException {
		String sql = "SELECT * FROM sezione"
				+ " WHERE codice_sezione = "+sectionCode;
		ArrayList<Map<String,Object>> sez = socket.query(sql);
		Section section=null;
		for(Map<String,Object> m : sez) {
			if(m.get("figlio_di")!=null)
			section = new Section((String) m.get("titolo"),(String)  m.get("descrizione"),
					(boolean) m.get("is_pubblica"), (int)  m.get("codice_sezione"), 
					(int) m.get("matricola"),(int)  m.get("codice_corso"),(int)  m.get("figlio_di"));
			else
				section = new Section((String) m.get("titolo"),(String)  m.get("descrizione"),
						(boolean) m.get("is_pubblica"), (int)  m.get("codice_sezione"), 
						(int) m.get("matricola"),(int)  m.get("codice_corso"), null);
		}
		return section;
	}
	
	public void modifySection(Section s) throws Exception {
		Object[] params = {s.sectionCode, s.title , s.visibility , s.description,  s.courseCode,
				(Integer) s.sonOf, s.studentNumber};
		socket.function("modificasezione", params);
	}
	
	public void createResource(Resource r) throws Exception {
		String sql = "INSERT INTO risorsa(codice_risorsa, nome, descrizione, percorso, tipo, "
				+ "codice_sezione, is_pubblica) "
				+ "VALUES ("+r.resourceCode+", '"+r.name+"', '"+r.description+"', '"+r.path+"','"+r.type+"', "
						+ " "+r.sectionCode+", "+r.visibility+");";
		socket.query(sql);
	}
	
	public void cancelResource(int resourceCode) throws ClassNotFoundException, SQLException, RemoteException {
		String sql = "DELETE FROM risorsa"
				+ " WHERE codice_risorsa = "+resourceCode;
		socket.query(sql);
	}
	
	public void modifyResource(Resource r) throws Exception {
		Object[] params = {(short) r.resourceCode, r.name,  r.description,
				 r.path, r.type, (short) r.sectionCode,r.visibility};
		socket.function("modificarisorsa", params);
	}
	
	public Resource getResource(int resourceCode) throws ClassNotFoundException, SQLException, RemoteException {
		String sql = "SELECT * FROM risorsa"
				+ " WHERE codice_risorsa = "+resourceCode;
		ArrayList<Map<String,Object>> sez = socket.query(sql);
		Resource resource=null;
		for(Map<String,Object> m : sez) {
			resource = new Resource((String) m.get("nome"),(String)  m.get("descrizione"),
					(String) m.get("percorso"),
					(int)  m.get("codice_sezione"), (int)  m.get("codice_risorsa"), 
					(boolean) m.get("is_pubblica"), (String)  m.get("tipo"));
		}
		return resource;
	}
	
	public ArrayList<Resource> getFolderContent(int resourceCode, String resourceName) throws RemoteException, MalformedURLException, NotBoundException{
		try {
			ArrayList<Map<String, Object>> hm;
			Object[] param = {resourceCode, resourceName};
			hm = SocketDb.getInstanceDb().function("get_contenuto_cartella", param);
			ArrayList<Resource> resources = new ArrayList<Resource>();
			CourseContentManagement ccm = new CourseContentManagement();
			
			for(Map<String,Object> m : hm) {
				resources.add(ccm.getResource((int) m.get("codice_risorsa")));
			}
			
			return resources;
		} catch (ClassNotFoundException | SQLException e2) {
			e2.printStackTrace();
		}
		return null;
	}
	
	public void modifyTitle(int codSezione, String titolo) throws Exception {
		String sql = "UPDATE sezione"+ " SET titolo = '"+titolo+
				"' WHERE codice_sezione = "+codSezione;
		socket.query(sql);
	}
	
	public void modifyVisibility(int codSezione) throws ClassNotFoundException, SQLException, RemoteException {
		String sql = "UPDATE sezione"+ " SET is_pubblica = true"+ " WHERE codice_sezione = "+codSezione;
		socket.query(sql);
	}
	
	public static void viewAsStudent(HomePage thisFrame, String course) throws MalformedURLException, RemoteException, NotBoundException {
		try {
			PaginaCorso mc = new PaginaCorso(thisFrame, Session.getInstance(), course, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
