package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import UserManager.UserManager;

public class RegistrazioneUtentiCorsi extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public RegistrazioneUtentiCorsi(Sessione ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel inserisci = new JLabel("Inserisci percorso file csv da caricare");
		contentPane.add(inserisci);
		
		JTextField path = new JTextField();
		contentPane.add(path);
		path.setColumns(10);
		
		JButton aggiungiUtenti = new JButton("Aggiungi utenti");
		aggiungiUtenti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserManager um = new UserManager();
					um.csvImportUtente(path.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(aggiungiUtenti);
		
		JButton aggiungiCorsi = new JButton("Aggiungi corsi");
		aggiungiCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//import csv corsi
					UserManager um = new UserManager();
					um.csvImportUtente(path.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(aggiungiCorsi);
		
		setVisible(true);
	}

}
