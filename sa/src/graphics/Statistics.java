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

import analytics.GlobalAnalytics;
import courseContentManagement.Course;
import notifier.Notifier;
import session.Session;

public class Statistics extends MyFrame {
	
	/*Mostrare il numero complessivo di utenti contemporaneamente connessi a seatIn 
	2. Mostrare il numero complessivo accessi per corso in una fascia temporale 
	3. Derivare il tempo medio di connessione degli studenti per ogni corso 
	4. Derivare il numero complessivo di download per ogni corso */

	private JPanel contentPane;
	private Statistics thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public Statistics(HomePage hp, Session ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
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
		
		try {
			GlobalAnalytics ga = new GlobalAnalytics();
			
			JLabel totalUsers;
			totalUsers = new JLabel("Utenti connessi: "+ga.onlineUsers());
			contentPane.add(totalUsers);
			
			JLabel numberAccessForHour = new JLabel("N. accessi per corso per fascia oraria");
			contentPane.add(numberAccessForHour);
			
			JLabel date = new JLabel("Inserire data inizio e data fine: ");
			contentPane.add(date);
			
			JTextField startDate = new JTextField();
			contentPane.add(startDate);
			startDate.setColumns(10);
			
			JTextField endDate = new JTextField();
			contentPane.add(endDate);
			endDate.setColumns(10);
			
			Box ab = Box.createVerticalBox();
			
			JButton accessButton = new JButton("Calcola il n. di accessi per corso "
					+ "per fascia temporale: ");
			accessButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if(ab.getComponentCount()!=0) {
							ab.removeAll();
						}
						Map<Integer, Integer> tmc = ga.accessByInterval(startDate.getText(), endDate.getText());
						for(Integer m : tmc.keySet()) {
							Notifier n = new Notifier();
							Course cor = n.getCourse(m);
							JLabel accessCourseTimeBand = new JLabel("N. accessi al corso "+cor.name+" "
									+ "nella fascia temporale data : "
									+ tmc.get(m));
							ab.add(accessCourseTimeBand);
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
			contentPane.add(accessButton);
			contentPane.add(ab);
			
			Box tmpc = Box.createVerticalBox();
			
			JButton avgTimeForCourse = new JButton("Calcola il tempo medio degli accessi ai corsi");
			avgTimeForCourse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if(tmpc.getComponentCount()==0) {
						Map<Integer, Integer> tmc = ga.avgMinsOnlineForCourse();
						for(Integer m : tmc.keySet()) {
							Notifier n = new Notifier();
							Course cor = n.getCourse(m);
							JLabel avgTimeConnectionCourse = new JLabel("Tempo medio connessioni per"
									+ " il corso "+cor.name+" : "+tmc.get(m));
							tmpc.add(avgTimeConnectionCourse);
							contentPane.revalidate();
							validate();
							repaint();
						}
						}
					} catch (ClassNotFoundException | SQLException | RemoteException | MalformedURLException | NotBoundException e) {
						JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
						e.printStackTrace();
					}
				}
			});
			contentPane.add(avgTimeForCourse);
			contentPane.add(tmpc);
			
			Box ndpc = Box.createVerticalBox();
			
			JButton nDownloadForCourse = new JButton("Calcola il numero di download per corso");
			nDownloadForCourse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if(ndpc.getComponentCount()==0) {
						Map<Integer, Integer> dpc = ga.downloadsForCourse();
						for(Integer m : dpc.keySet()) {
							Notifier n = new Notifier();
							Course cor = n.getCourse(m);
							JLabel numberAccessCourse = new JLabel("Numero di download per"
									+ " il corso "+cor.name+" : "+dpc.get(m));
							ndpc.add(numberAccessCourse);
							contentPane.revalidate();
							validate();
							repaint();
						}
						}
					} catch (ClassNotFoundException | SQLException | RemoteException | MalformedURLException | NotBoundException e) {
						JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
						e.printStackTrace();
					}
				}
			});
			contentPane.add(nDownloadForCourse);
			contentPane.add(ndpc);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane,"Errore");
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
