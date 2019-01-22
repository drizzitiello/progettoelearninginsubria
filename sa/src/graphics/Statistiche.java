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

import analytics.CourseAnalytics;
import analytics.GlobalAnalytics;
import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;
import notifier.Notifier;
import session.Session;
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
	public Statistiche(Session ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		try {
			GlobalAnalytics ga = new GlobalAnalytics();
			
			JLabel utentiComplessivi;
			utentiComplessivi = new JLabel("Utenti connessi: "+ga.onlineUsers());
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
						Map<Integer, Integer> tmc = ga.accessByInterval(dataInizio.getText(), dataFine.getText());
						for(Integer m : tmc.keySet()) {
							Course cor = Notifier.getCourse(m);
							JLabel accessiCorsoFasciaTemporale = new JLabel("N. accessi al corso "+cor.name+" "
									+ "nella fascia temporale data : "
									+ tmc.get(m));
							contentPane.add(accessiCorsoFasciaTemporale);
							contentPane.revalidate();
							validate();
							repaint();
						}
						
						
						
						
						/*ReperisciCorso rc = new ReperisciCorso();
						ArrayList<Corso> corsi = rc.getCorsi();
						for(Corso c : corsi) {
							ga.accessByInterval(dataInizio.getText(), dataFine.getText());
							JLabel accessiCorsoFasciaTemporale = new JLabel("N. accessi al corso "+c.nome+" "
									+ "nella fascia temporale data : "
									+ ga.accessByInterval(dataInizio.getText(), dataFine.getText()));
							contentPane.add(accessiCorsoFasciaTemporale);
							contentPane.revalidate();
							validate();
							repaint();
						}*/
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
						Map<Integer, Integer> tmc = ga.avgMinsOnlineForCourse();
						for(Integer m : tmc.keySet()) {
							Course cor = Notifier.getCourse(m);
							JLabel tempoMedioConnessioniCorso = new JLabel("Tempo medio connessioni per"
									+ " il corso "+cor.name+" : "+tmc.get(m));
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
						Map<Integer, Integer> dpc = ga.downloadsForCourse();
						for(Integer m : dpc.keySet()) {
							Course cor = Notifier.getCourse(m);
							JLabel numeroAccessiCorso = new JLabel("Numero di download per"
									+ " il corso "+cor.name+" : "+dpc.get(m));
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
