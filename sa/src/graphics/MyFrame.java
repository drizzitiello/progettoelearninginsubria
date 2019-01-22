package graphics;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import session.Session;

public class MyFrame extends JFrame{
	
	private MyFrame thisFrame;
	private Session ses;
	
	public MyFrame() {
		try {
			ses=Session.getInstance();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		thisFrame=this;
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(thisFrame, 
		            "Are you sure you want to close this window?", "Close Window?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	try {
						ses.destroy();
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
		            System.exit(0);
		        }
		    }
		});
	}
}
