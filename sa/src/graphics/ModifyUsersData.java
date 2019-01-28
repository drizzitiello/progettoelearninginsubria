package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import session.Session;
import userManager.UserManager;

public class ModifyUsersData extends MyFrame {

	private JPanel contentPane;
	private ModifyUsersData thisFrame;


	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public ModifyUsersData(HomePage hp, Session ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisFrame=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hp.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		Box studentNumbers = Box.createVerticalBox();
		Box changes = Box.createVerticalBox();
		Object[] params= {};
		try {
			UserManager um = new UserManager();
			ArrayList<Integer> users = um.getRegisteredUsers();
			ArrayList<JButton> buttons = new ArrayList<JButton>();
			for(Integer u : users) {
				JButton modify = new JButton("Matricola n. "+u+": modifica dati utente");
				modify.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UserInfo iu = new UserInfo(thisFrame, ses,u);
					}
				});
				changes.add(modify);
				buttons.add(modify);
			}
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
			e.printStackTrace();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane,"Errore");
			e1.printStackTrace();
		}
		contentPane.add(studentNumbers);
		contentPane.add(changes);
		setVisible(true);
	}
}