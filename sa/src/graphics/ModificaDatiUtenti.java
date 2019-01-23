package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import session.Session;
import socketDb.SocketDb;

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
		setBounds(100, 100, 450, 300);
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
			ArrayList<Map<String,Object>> hm = SocketDb.getInstanceDb().function("getutentiregistrati", params);
			ArrayList<JButton> ajb = new ArrayList<JButton>();
			for(Map<String,Object> m : hm) {
				JButton modifica = new JButton("Matricola n. "+m.get("matricola")+": modifica dati utente");
				modifica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InfoUtente iu = new InfoUtente(ses,m.get("matricola"));
					}
				});
				modifiche.add(modifica);
				ajb.add(modifica);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		contentPane.add(matricole);
		contentPane.add(modifiche);
		setVisible(true);
	}
}