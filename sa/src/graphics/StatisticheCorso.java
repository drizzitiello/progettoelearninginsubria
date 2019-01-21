package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import analytics.CorsoAnalytics;
import gestioneContenutiCorso.Corso;
import socketDb.SocketDb;

public class StatisticheCorso extends JFrame {
	
	/* Mostrare il numero complessivo di utenti connessi che stanno visualizzando/interagendo con i contenuti del corso 
	 2. Mostrare il numero complessivo di utenti che hanno effettuato il download di una o più risorse in intervalli temporali dati 
	 3. Derivare il tempo medio di connessione degli studenti alle pagine del corso */

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public StatisticheCorso(Sessione ses, Corso cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		try {
			CorsoAnalytics ca = new CorsoAnalytics(cor.codCorso);
			
			JLabel numeroAccessiCorso = new JLabel("N. utenti su questa pagina: "+ca.utentiConnessi());
			contentPane.add(numeroAccessiCorso);
			
			JLabel nDownloadRisorsaPerTempo = new JLabel("N. download risorsa r :");
			contentPane.add(nDownloadRisorsaPerTempo);
			
			JLabel tempoMedioConnessioniPagina = new JLabel("Tempo medio connessioni alla pagina: "+ca.tempoMedioConn());
			contentPane.add(tempoMedioConnessioniPagina);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
