package courseContentManagement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import socketDb.SocketDb;

public class FindCourse {
	
	private RemoteInterface socket;
	private AnotherInterface server;
	
	public FindCourse() throws MalformedURLException, RemoteException, NotBoundException {
		server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		System.out.println(i);
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
	}
	
	public ArrayList<Course> getCourses() throws ClassNotFoundException, SQLException, RemoteException, MalformedURLException, NotBoundException {
		String sql = "getCorsi";
		Object[] s= {};
		ArrayList<Map<String, Object>> obj=socket.function(sql, s);
		ArrayList<Course> courses = new ArrayList<Course>();
		for(Map<String, Object> m : obj) {
			courses.add(new Course((int) m.get("codice_corso"), (String) m.get("nome"),
					(int) m.get("anno_attivazione"), (String) m.get("facolta"), 
					(String) m.get("descrizione"), (int) m.get("peso"), (int) m.get("creatore")));
		}
		return courses;
	}
	
	public Content getContentCourse(Course c) throws ClassNotFoundException, SQLException, RemoteException{
		Content cont=new Content();
		String sql,sql2;
		sql = "getContenutoCorso";
		Object[] s= {c.courseCode};
		ArrayList<Map<String, Object>> obj=socket.function(sql,s);
		for(Map<String, Object> m : obj) {
			int sectionCode=(int) m.get("codice_sezione");
			String title=(String) m.get("titolo");
			String descr=(String) m.get("descrizione");
			Boolean visibility=(Boolean) m.get("is_pubblica");
			int studentNumber=(int) m.get("matricola");
			int courseCode=(int) m.get("cod_corso");
			Integer sonOf=(Integer) m.get("figlio_di");
			Section sec=cont.addSection(title, descr, visibility, sectionCode, studentNumber, courseCode, sonOf);
			sql2 = "getContenutoCorso1";
			Object[] s2= {sectionCode};
			ArrayList<Map<String, Object>> obj2=socket.function(sql2,s2);
			for(Map<String, Object> ms : obj2) {
				String name=(String) ms.get("nome");
				String descr2=(String) ms.get("descrizione");
				String path2=(String) ms.get("percorso");
				int sectionCode2=(int) ms.get("codice_sezione");
				int resourceCode=(int) ms.get("codice_risorsa");
				Boolean visibility2=(Boolean) ms.get("is_pubblica");
				String type=(String) ms.get("tipo");
				sec.addResource(name, descr2, path2, sectionCode2, resourceCode, visibility2, type);
			}
		}
		return cont;
	}
	
	public void download(Resource r) throws ClassNotFoundException, SQLException {
		try {
			String extension = "";
			int i = r.path.lastIndexOf('.');
			if (i > 0) {
			    extension = r.path.substring(i+1);
			}
			BufferedInputStream in =new BufferedInputStream(new URL(r.path).openStream());
			String home = System.getProperty("user.home");
			Files.copy(in, Paths.get(home+"/Downloads/"+r.name+"."+extension),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
