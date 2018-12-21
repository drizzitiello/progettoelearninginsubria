package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Object[] params= {};
		try {
			ArrayList<Map<String,Object>> hm = SocketDb.getInstanceDb().function("getutentiregistrati", params);
			for(Map<String,Object> m : hm) {
				JLabel utente = new JLabel("Matricola n. "+m.get("matricola"));
				contentPane.add(utente);
				
				JButton modifica = new JButton("Modifica dati utente");
				modifica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InfoUtente iu = new InfoUtente(ses,pwd,m);
					}
				});
				contentPane.add(modifica);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}