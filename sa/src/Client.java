import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import graphics.HomePage;
import graphics.Login;
import session.Session;
import user.User;

public class Client {
	public static void main(String[] args)
	 {//20
			for(int i=0; i<50; i++) {
				Session ses;
				try {
					ses = Session.getInstance();
					ses.create(2);
					HomePage hp = new HomePage(ses, "ciao");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	 }
}
