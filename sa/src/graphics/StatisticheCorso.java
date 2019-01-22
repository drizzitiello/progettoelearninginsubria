package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import analytics.CorsoAnalytics;
import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Resource;
import session.Session;

public class StatisticheCorso extends MyFrame {
	
	/* Mostrare il numero complessivo di utenti connessi che stanno visualizzando/interagendo con i contenuti del corso 
	 2. Mostrare il numero complessivo di utenti che hanno effettuato il download di una o più risorse in intervalli temporali dati 
	 3. Derivare il tempo medio di connessione degli studenti alle pagine del corso */

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public StatisticheCorso(Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		try {
			CorsoAnalytics ca = new CorsoAnalytics(cor.courseCode);
			
			JLabel numeroAccessiCorso = new JLabel("N. utenti su questa pagina: "+ca.onlineUsers());
			contentPane.add(numeroAccessiCorso);
			
			JLabel date = new JLabel("Inserire data inizio e data fine: ");
			contentPane.add(date);
			
			JTextField dataInizio = new JTextField();
			contentPane.add(dataInizio);
			dataInizio.setColumns(10);
			
			JTextField dataFine = new JTextField();
			contentPane.add(dataFine);
			dataFine.setColumns(10);
			
			JButton downloadButton = new JButton("Calcola il n. di utenti che hanno effettuato download");
			downloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Map<Integer, Integer> downloads;
					try {
						downloads = ca.downloadByInterval(dataInizio.getText(), dataFine.getText());
						for(Integer m : downloads.keySet()) {
							CourseContentManagement gcc = new CourseContentManagement();
							Resource r = gcc.getResource(m);
							JLabel nDownloadRisorsaPerTempo = new JLabel("N. utenti che hanno effettuato il download"
									+ "della risorsa "+r.name+" : "+downloads.get(m));
							contentPane.add(nDownloadRisorsaPerTempo);
							contentPane.revalidate();
							validate();
							repaint();
						}
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			});
			contentPane.add(downloadButton);
			
			JLabel tempoMedioConnessioniPagina = new JLabel("Tempo medio connessioni alla pagina: "+
			ca.avgMinsOnline());
			contentPane.add(tempoMedioConnessioniPagina);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
