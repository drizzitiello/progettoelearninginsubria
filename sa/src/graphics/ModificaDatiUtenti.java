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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import session.Session;
import userManager.UserManager;

public class ModificaDatiUtenti extends MyFrame {

	private JPanel contentPane;
	private ModificaDatiUtenti thisFrame;


	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public ModificaDatiUtenti(HomePage hp, Session ses, String pwd) {
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
		
		Box matricole = Box.createVerticalBox();
		Box modifiche = Box.createVerticalBox();
		Object[] params= {};
		try {
			UserManager um = new UserManager();
			ArrayList<Integer> kk = um.getRegisteredUsers();
			ArrayList<JButton> ajb = new ArrayList<JButton>();
			for(Integer u : kk) {
				JButton modifica = new JButton("Matricola n. "+u+": modifica dati utente");
				modifica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InfoUtente iu = new InfoUtente(thisFrame, ses,u);
					}
				});
				modifiche.add(modifica);
				ajb.add(modifica);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		contentPane.add(matricole);
		contentPane.add(modifiche);
		setVisible(true);
	}
}