package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import socketDb.SocketDb;

public class ModificaDatiUtenti extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public ModificaDatiUtenti(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Object[] params= {};
		try {
			ArrayList<Map<String,Object>> hm = SocketDb.getInstanceDb().function("getutentiregistrati", params);
			ArrayList<JLabel> ajl = new ArrayList<JLabel>();
			ArrayList<JButton> ajb = new ArrayList<JButton>();
			for(Map<String,Object> m : hm) {
				JLabel utente = new JLabel("Matricola n. "+m.get("matricola"));
				contentPane.add(utente);
				ajl.add(utente);
				JButton modifica = new JButton("Modifica dati utente");
				modifica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InfoUtente iu = new InfoUtente(ses,m.get("matricola"));
					}
				});
				contentPane.add(modifica);
				ajb.add(modifica);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

}
