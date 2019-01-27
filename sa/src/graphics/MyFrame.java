package graphics;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.JFrame;

import session.Session;

public class MyFrame extends JFrame{
	
	private MyFrame thisFrame;
	private Session ses;
	
	public MyFrame() {
		try {
			ses=Session.getInstance();
		} catch (ClassNotFoundException | MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}
		
		thisFrame=this;
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        	try {
						ses.destroy();
					} catch (ClassNotFoundException | SQLException | RemoteException e) {
						e.printStackTrace();
					}
		            System.exit(0);
		    }
		});
	}
}
