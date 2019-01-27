package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import notifier.Notifier;
import session.Session;
import socketDb.SocketDb;
import userManager.UserManager;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.Box;

public class HomePage extends MyFrame {

	private JPanel contentPane;
	private JTextField corso;
	private JButton last;
	private ArrayList<JButton> la;
	private HomePage hp;

	/**
	 * Create the frame.
	 * @param pwd 
	 */
	public HomePage(Session ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		hp=this;
		try {
		if(!ses.create(ses.getUser().getInfo().student_number)) {
			JOptionPane.showMessageDialog(contentPane, "Troppi utenti connessi");
		}
		else {
		
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ses.destroy();
					Login l = new Login();
					hp.setVisible(false);
				} catch (ClassNotFoundException | SQLException | RemoteException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nella distruzione della sessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(backButton);
		
		JLabel cerca = new JLabel("Cerca corsi");
		contentPane.add(cerca);
		
		corso = new JTextField();
		contentPane.add(corso);
		corso.setColumns(10);
		
		Box verticalBox = Box.createVerticalBox();
		
		JButton cercaCorsi = new JButton("Cerca");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(last!=null)verticalBox.remove(last);
					Notifier n = new Notifier();
					Course i = n.getCourse(corso.getText());
					if(i!=null) {
						JButton b = new JButton(corso.getText());
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(hp, ses, corso.getText(), false);
								hp.setVisible(false);
							}
						});
						last=b;
						verticalBox.add(b);
						contentPane.revalidate();
					    validate();
					}
					else {
						JOptionPane.showMessageDialog(cercaCorsi, "Il corso cercato non esiste");
					}
				} catch (ClassNotFoundException | SQLException | RemoteException | MalformedURLException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nel caricamento dei corsi");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cercaCorsi);
		contentPane.add(verticalBox);
		
		Box verticalBox2 = Box.createVerticalBox();
		
		JButton corsiAssegnati = new JButton("I miei corsi");
		corsiAssegnati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(la!=null) for(JButton j : la) {
						verticalBox2.remove(j);
						contentPane.revalidate();validate();
					}
					
					UserManager um = new UserManager();
					ArrayList<String> c = um.getStudentCourses(ses.getUser().getInfo().student_number);
					
					for(String s : c) {
						JButton b = new JButton(s);
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(hp, ses, b.getText(), false);
								hp.setVisible(false);
							}
						});
						la.add(b);
						verticalBox2.add(b);
						}
					contentPane.revalidate();
					validate();
				} catch (ClassNotFoundException | SQLException | RemoteException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nel caricamento dei corsi");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(corsiAssegnati);
		contentPane.add(verticalBox2);
		
		if(ses.getUser().getInfo().userType!=1) {
			JButton email = new JButton("Accedi all'email");
			email.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmailSender eh = new EmailSender(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(email);
		}
		
		JButton visualizzaInfo = new JButton("Informazioni personali");
		visualizzaInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoUtente eh = new InfoUtente(hp, ses, pwd);
				hp.setVisible(false);
			}
		});
		contentPane.add(visualizzaInfo);
		
		
		
		if(ses.getUser().getInfo().userType==3) {
			JButton modificaDatiUtenti = new JButton("Modifica dati utenti");
			modificaDatiUtenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModificaDatiUtenti mdu = new ModificaDatiUtenti(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(modificaDatiUtenti);
			
			JButton aggiungiCorsi = new JButton("Gestisci corsi");
			aggiungiCorsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GestisciCorsi mdu = new GestisciCorsi(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(aggiungiCorsi);
			
			JButton statistiche = new JButton("Analisi statistiche");
			statistiche.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Statistiche mdu = new Statistiche(hp, ses);
					hp.setVisible(false);
				}
			});
			contentPane.add(statistiche);
			
			JButton registraUtenti = new JButton("Registra nuovi utenti e corsi");
			registraUtenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegistrazioneUtentiCorsi mdu = new RegistrazioneUtentiCorsi(hp, ses);
					hp.setVisible(false);
				}
			});
			contentPane.add(registraUtenti);
			
			JButton assegnaDocenti = new JButton("Assegna docenti a corsi");
			assegnaDocenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						AssegnazioneDocenti mdu = new AssegnazioneDocenti(hp, ses);
					} catch (MalformedURLException | RemoteException | NotBoundException e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
						e1.printStackTrace();
					}
					hp.setVisible(false);
				}
			});
			contentPane.add(assegnaDocenti);
		}
		
		la=new ArrayList<JButton>();
		setVisible(true);
		}
		}
		catch(NullPointerException e) {
			JOptionPane.showMessageDialog(contentPane, "Troppi utenti connessi");
		}
	}

}