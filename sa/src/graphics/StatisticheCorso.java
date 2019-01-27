package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import analytics.CourseAnalytics;
import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Resource;
import session.Session;

public class StatisticheCorso extends MyFrame {
	
	/* Mostrare il numero complessivo di utenti connessi che stanno visualizzando/interagendo con i contenuti del corso 
	 2. Mostrare il numero complessivo di utenti che hanno effettuato il download di una o pi� risorse in intervalli temporali dati 
	 3. Derivare il tempo medio di connessione degli studenti alle pagine del corso */

	private JPanel contentPane;
	private StatisticheCorso thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public StatisticheCorso(PaginaCorso pc, Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisframe=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		try {
			CourseAnalytics ca = new CourseAnalytics(cor.courseCode);
			
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
			
			Box ab = Box.createVerticalBox();
			
			JButton downloadButton = new JButton("Calcola il n. di utenti che hanno effettuato download");
			downloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Map<Integer, Integer> downloads;
					try {
						if(ab.getComponentCount()!=0) {
							ab.removeAll();
						}
						downloads = ca.downloadByInterval(dataInizio.getText(), dataFine.getText());
						for(Integer m : downloads.keySet()) {
							CourseContentManagement gcc = new CourseContentManagement();
							Resource r = gcc.getResource(m);
							JLabel nDownloadRisorsaPerTempo = new JLabel("N. utenti che hanno effettuato il download"
									+ "della risorsa "+r.name+" : "+downloads.get(m));
							//contentPane.add(nDownloadRisorsaPerTempo);
							ab.add(nDownloadRisorsaPerTempo);
							contentPane.revalidate();
							validate();
							repaint();
						}
					} catch (ClassNotFoundException | SQLException | RemoteException | MalformedURLException | NotBoundException e) {
						JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
						e.printStackTrace();
					}
				}
			});
			contentPane.add(downloadButton);
			contentPane.add(ab);
			
			JLabel tempoMedioConnessioniPagina = new JLabel("Tempo medio connessioni alla pagina: "+
			ca.avgMinsOnline());
			contentPane.add(tempoMedioConnessioniPagina);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane,"Errore");
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
