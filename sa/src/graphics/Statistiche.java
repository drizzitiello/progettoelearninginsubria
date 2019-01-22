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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import analytics.CorsoAnalytics;
import analytics.GlobalAnalytics;
import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.ReperisciCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Corso;
import notifier.Notifier;
import socketDb.SocketDb;

public class Statistiche extends MyFrame {
	
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
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		try {
			GlobalAnalytics ga = new GlobalAnalytics();
			
			JLabel utentiComplessivi;
			utentiComplessivi = new JLabel("Utenti connessi: "+ga.utentiConnessi());
			contentPane.add(utentiComplessivi);
			
			JLabel numeroAccessiPerOra = new JLabel("N. accessi per corso per fascia oraria");
			contentPane.add(numeroAccessiPerOra);
			
			JLabel date = new JLabel("Inserire data inizio e data fine: ");
			contentPane.add(date);
			
			JTextField dataInizio = new JTextField();
			contentPane.add(dataInizio);
			dataInizio.setColumns(10);
			
			JTextField dataFine = new JTextField();
			contentPane.add(dataFine);
			dataFine.setColumns(10);
			
			JButton accessButton = new JButton("Calcola il n. di accessi per corso "
					+ "per fascia temporale");
			accessButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						ReperisciCorso rc = new ReperisciCorso();
						ArrayList<Corso> corsi = rc.getCorsi();
						for(Corso c : corsi) {
							ga.accessiIntervallo(dataInizio.getText(), dataFine.getText());
							JLabel accessiCorsoFasciaTemporale = new JLabel("N. accessi al corso "+c.nome+" "
									+ "nella fascia temporale data : "
									+ ga.accessiIntervallo(dataInizio.getText(), dataFine.getText(), c));
							contentPane.add(accessiCorsoFasciaTemporale);
							contentPane.revalidate();
							validate();
							repaint();
						}
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			});
			contentPane.add(accessButton);
			
			JButton tempoMedioPerCorso = new JButton("Calcola il tempo medio degli accessi ai corsi");
			tempoMedioPerCorso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						Map<Integer, Integer> tmc = ga.tempoMedioPerCorso();
						for(Integer m : tmc.keySet()) {
							Corso cor = Notifier.getCorso(m);
							JLabel tempoMedioConnessioniCorso = new JLabel("Tempo medio connessioni per"
									+ " il corso "+cor.nome+" : "+tmc.get(m));
							contentPane.add(tempoMedioConnessioniCorso);
							contentPane.revalidate();
							validate();
							repaint();
						}
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			});
			contentPane.add(tempoMedioPerCorso);
			
			JButton nDownloadPerCorso = new JButton("Calcola il numero di download per corso");
			nDownloadPerCorso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						Map<Integer, Integer> dpc = ga.downloadsPerCorso();
						for(Integer m : dpc.keySet()) {
							Corso cor = Notifier.getCorso(m);
							JLabel numeroAccessiCorso = new JLabel("Numero di download per"
									+ " il corso "+cor.nome+" : "+dpc.get(m));
							contentPane.add(numeroAccessiCorso);
							contentPane.revalidate();
							validate();
							repaint();
						}
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			});
			contentPane.add(nDownloadPerCorso);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
