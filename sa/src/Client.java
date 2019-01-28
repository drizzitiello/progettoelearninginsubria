import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import graphics.HomePage;
import graphics.Login;
import interfaces.AnotherInterface;
import interfaces.RemoteInterface;
import server.Server;
import session.Session;
import socketDb.SocketDb;
import user.User;

public class Client {
	public static void main(String[] args)
	 {//20
		for(int i=0; i<1;i++) {
			try {
				Login l = new Login();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
			/*for(int i=0; i<50; i++) {
				Session ses;
				try {
					ses = Session.getInstance();
					ses.create(2);
					HomePage hp = new HomePage(ses, "ciao");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}§/
		/*try
		  {
			AnotherInterface s = Server.getInstance(SocketDb.getAdminInstanceDb("localhost", "postgres", "makaay"));
			Naming.rebind("rmi://localhost/Server",s);
			RemoteInterface se = SocketDb.getAdminInstanceDb("localhost", "postgres", "makaay");
			System.out.println(se);
			Naming.rebind("rmi://localhost/SocketDb",se);
		  }
		  catch (RemoteException e){e.printStackTrace( );}
		  catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(int i=0; i<50; i++) {
			try {
				CourseManagement cm = new CourseManagement();
				System.out.println(i);
			} catch (ClassNotFoundException | MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		/*for(int i=0; i<50; i++) {
			Session ses;
			try {
				ses = Session.getInstance();
				ses.create(2);
				HomePage hp = new HomePage(ses, "ciao");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}*/
	 }
}
