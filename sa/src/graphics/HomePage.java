package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import notifier.Notifier;
import session.Session;
import socketDb.SocketDb;

import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

//public class HomePage extends JFrame {
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		hp=this;
		
		try {
			ses.create(ses.getUser().getInfo().student_number);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Login l = new Login();
					hp.setVisible(false);
				} catch (ClassNotFoundException | SQLException e1) {
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
		
		JButton cercaCorsi = new JButton("Cerca");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(last!=null)contentPane.remove(last);
					Course i = Notifier.getCourse(corso.getText());
					if(i!=null) {
						JButton b = new JButton(corso.getText());
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(hp, ses, corso.getText(), false);
								hp.setVisible(false);
							}
						});
						last=b;
						contentPane.add(b);
						contentPane.revalidate();
					    validate();
					}
					else {
						JOptionPane.showMessageDialog(cercaCorsi, "Il corso cercato non esiste");
					}
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cercaCorsi);
		
		JButton corsiAssegnati = new JButton("I miei corsi");
		corsiAssegnati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(la!=null) for(JButton j : la) {
						contentPane.remove(j);contentPane.revalidate();validate();
					}
					ArrayList<Map<String, Object>> hm;
					Object[] param = {ses.getUser().getInfo().student_number};
					hm = SocketDb.getInstanceDb().function("getcorsiutente", param);
					for(Map<String,Object> m : hm) {
						JButton b = new JButton((String) m.get("nome"));
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(hp, ses, b.getText(), false);
								hp.setVisible(false);
							}
						});
						la.add(b);
						contentPane.add(b);
						}
					contentPane.revalidate();
					validate();
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(corsiAssegnati);
		
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
					AssegnazioneDocenti mdu = new AssegnazioneDocenti(hp, ses);
					hp.setVisible(false);
				}
			});
			contentPane.add(assegnaDocenti);
		}
		
		la=new ArrayList<JButton>();
		setVisible(true);
	}

}
