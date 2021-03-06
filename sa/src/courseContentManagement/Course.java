package courseContentManagement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import socketDb.SocketDb;

public class Course {
	
	public int courseCode, weight, creator, activation_year;
	public String name, description, faculty;
	private RemoteInterface socket;
	private AnotherInterface server;
	
	public Course() throws MalformedURLException, RemoteException, NotBoundException {
		server = (AnotherInterface) Naming.lookup ("rmi://localhost/Server");
		int i = server.getRegistry();
		System.out.println(i);
		server.starting();
		Registry registry = LocateRegistry.getRegistry("localhost",i); 
		socket = (RemoteInterface) registry.lookup ("SocketDb");
	}

	public Course(int courseCode, String name, int activation_year, String faculty, String description,
			int weight, int creator) throws MalformedURLException, RemoteException, NotBoundException {
		this.courseCode=courseCode;
		this.name=name;
		this.activation_year=activation_year;
		this.faculty=faculty;
		this.description=description;
		this.weight=weight;
		this.creator=creator;
	}
	/**
	 * 
	 * @param con
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws RemoteException 
	 */
	public void setContents(Content con) throws ClassNotFoundException, SQLException, RemoteException {
		ArrayList<Section> sections = new ArrayList<Section>();
		ArrayList<Resource> resources = new ArrayList<Resource>();
		for(Section section : con.sections) {
			sections.add(section);
			String sql = "INSERT INTO sezione "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
			Object[] p = {section.sectionCode, section.title, section.description,
					section.visibility, section.courseCode, section.sonOf,
					section.creator};
			socket.query(sql,p);
			for(Resource resource : section.resources) {
				resources.add(resource);
				String sql2 = "INSERT INTO risorsa " + 
						"VALUES (?, ?, ?, ?, ?, ?, ?)";
				Object[] p2 = {resource.resourceCode, resource.name, resource.description, 
						resource.path, resource.type, resource.sectionCode, resource.visibility};
				socket.query(sql2,p2);
			}
		}
	}
	
	public void setCourseCode(int courseCode) {
		this.courseCode = courseCode;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setYear(int activation_year) {
		this.activation_year = activation_year;
	}
	
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public void setCreator(int creator) {
		this.creator = creator;
	}
	
}
