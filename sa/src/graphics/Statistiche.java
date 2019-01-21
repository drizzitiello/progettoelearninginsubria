package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import analytics.CorsoAnalytics;
import analytics.GlobalAnalytics;
import socketDb.SocketDb;

public class Statistiche extends JFrame {
	
	/*Mostrare il numero complessivo di utenti contemporaneamente connessi a seatIn 
	2. Mostrare il numero complessivo accessi per corso in una fascia temporale 
	3. Derivare il tempo medio di connessione degli studenti per ogni corso 
	4. Derivare il numero complessivo di download per ogni corso */

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public Statistiche(Sessione ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		try {
			GlobalAnalytics ca = new GlobalAnalytics();
			
			JLabel utentiComplessivi;
			utentiComplessivi = new JLabel("Utenti connessi: "+ca.utentiConnessi());
			contentPane.add(utentiComplessivi);
			
			JLabel numeroAccessiPerOra = new JLabel("N. accessi per fascia oraria");
			contentPane.add(numeroAccessiPerOra);
			
			JLabel tempoMedioConnessioniPerCorso = new JLabel("Tempo medio connessioni per corso");
			contentPane.add(tempoMedioConnessioniPerCorso);
			
			JLabel nDownloadPerCorso = new JLabel("N. download per corso");
			contentPane.add(nDownloadPerCorso);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
