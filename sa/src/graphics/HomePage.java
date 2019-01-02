package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import notifier.Notifier;
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

public class HomePage extends JFrame {

	private JPanel contentPane;
	private JTextField corso;
	private JButton last;
	private ArrayList<JButton> la;

	/**
	 * Create the frame.
	 * @param pwd 
	 */
	public HomePage(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
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
					Integer i = Notifier.getCorso(corso.getText()).codCorso;
					if(i!=null) {
						JButton b = new JButton(corso.getText());
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(ses, corso.getText(), false);
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
					Object[] param = {ses.getUtente().getInfo().matricola};
					hm = SocketDb.getInstanceDb().function("getcorsiutente", param);
					for(Map<String,Object> m : hm) {
						JButton b = new JButton((String) m.get("nome"));
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								PaginaCorso pc = new PaginaCorso(ses, b.getText(), false);
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
		
		if(ses.getUtente().getInfo().tipoUtente!=1) {
			JButton email = new JButton("Accedi all'email");
			email.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmailSender eh = new EmailSender(ses, pwd);
				}
			});
			contentPane.add(email);
		}
		
		JButton visualizzaInfo = new JButton("Informazioni personali");
		visualizzaInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoUtente eh = new InfoUtente(ses, pwd);
			}
		});
		contentPane.add(visualizzaInfo);
		
		if(ses.getUtente().getInfo().tipoUtente==3) {
			JButton modificaDatiUtenti = new JButton("Modifica dati utenti");
			modificaDatiUtenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModificaDatiUtenti mdu = new ModificaDatiUtenti(ses, pwd);
				}
			});
			contentPane.add(modificaDatiUtenti);
			
			JButton aggiungiCorsi = new JButton("Gestisci corsi");
			aggiungiCorsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GestisciCorsi mdu = new GestisciCorsi(ses, pwd);
				}
			});
			contentPane.add(aggiungiCorsi);
			
			JButton statistiche = new JButton("Analisi statistiche");
			statistiche.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Statistiche mdu = new Statistiche(ses);
				}
			});
			contentPane.add(statistiche);
			
			JButton registraUtenti = new JButton("Registra nuovi utenti");
			registraUtenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegistrazioneUtenti mdu = new RegistrazioneUtenti(ses);
				}
			});
			contentPane.add(registraUtenti);
			
			JButton assegnaDocenti = new JButton("Assegna docenti a corsi");
			assegnaDocenti.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AssegnazioneDocenti mdu = new AssegnazioneDocenti(ses);
				}
			});
			contentPane.add(assegnaDocenti);
		}
		
		la=new ArrayList<JButton>();
		setVisible(true);
	}

}
