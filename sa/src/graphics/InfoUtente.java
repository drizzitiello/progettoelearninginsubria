package graphics;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import user.User;
import userManager.UserManager;
import session.Session;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class InfoUtente extends MyFrame {

	private JPanel contentPane;
	private InfoUtente thisFrame;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public InfoUtente(HomePage hp, Session ses, String pwd) {
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
		
		JLabel matricola = new JLabel("Matricola: "+ses.getUser().getInfo().student_number);
		contentPane.add(matricola);
		
		JLabel nome = new JLabel("Nome: "+ses.getUser().getInfo().name);
		contentPane.add(nome);
		
		JLabel cognome = new JLabel("Cognome: "+ses.getUser().getInfo().surname);
		contentPane.add(cognome);
		
		JLabel email = new JLabel("Email: "+ses.getUser().getInfo().email);
		contentPane.add(email);
		
		String tipo="";
		switch(ses.getUser().getInfo().userType) {
			case 1: tipo="Studente";
			break;
			case 2: tipo="Docente";
			break;
			case 3: tipo="Amministratore";
			break;
			default:
			break;
		}
		JLabel tipoUtente = new JLabel("Tipo Utente: "+tipo);
		contentPane.add(tipoUtente);
		
		if(ses.getUser().getInfo().userType==1) {
			JLabel annoImmatricolazione = new JLabel("Anno Immatricolazione: "+ses.getUser().getInfo().registrationYear);
			contentPane.add(annoImmatricolazione);
		
			JLabel corsoLaurea = new JLabel("Corso di Laurea: "+ses.getUser().getInfo().faculty);
			contentPane.add(corsoLaurea);
		
			JLabel statoCarriera = new JLabel("Stato carriera: "+ses.getUser().getInfo().careerStatus);
			contentPane.add(statoCarriera);
		}
		else {
			JLabel strutturaRiferimento = new JLabel("Struttura di riferimento: "+ses.getUser().getInfo().referenceStructure);
			contentPane.add(strutturaRiferimento);
		}
		
		setVisible(true);
	}

	public InfoUtente(ModificaDatiUtenti mdu, Session ses, Object matr) {
		int mat = (int) matr;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisFrame=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdu.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		User user;
		try {
			user = new User();
			user.createFromStudentNumber(mat);
			JLabel matricola = new JLabel("Matricola: "+mat);
			contentPane.add(matricola);
			
			JLabel nome = new JLabel("Nome: ");
			contentPane.add(nome);
			JTextField modificaNome = new JTextField(user.getInfo().name);
			contentPane.add(modificaNome);
			modificaNome.setColumns(10);
			
			JLabel cognome = new JLabel("Cognome: ");
			contentPane.add(cognome);
			JTextField modificaCognome = new JTextField(user.getInfo().surname);
			contentPane.add(modificaCognome);
			modificaCognome.setColumns(10);
			
			JLabel email = new JLabel("Email: ");
			contentPane.add(email);
			JTextField modificaEmail = new JTextField(user.getInfo().email);
			contentPane.add(modificaEmail);
			modificaEmail.setColumns(10);
			
			JLabel tipoUtente = new JLabel("Tipo Utente: ");
			contentPane.add(tipoUtente);
			JTextField modificaTipoUtente = new JTextField(""+user.getInfo().userType);
			contentPane.add(modificaTipoUtente);
			modificaTipoUtente.setColumns(10);
			
			
			JLabel annoImmatricolazione = new JLabel("Anno Immatricolazione: ");
			JTextField modificaAnnoImmatricolazione;
			if(user.getInfo().registrationYear==null) {
				modificaAnnoImmatricolazione = new JTextField("");
			}
			else {
				modificaAnnoImmatricolazione = new JTextField(""+user.getInfo().registrationYear);
			}
			
			JLabel corsoLaurea = new JLabel("Corso di Laurea: ");
			JTextField modificaCorsoLaurea = new JTextField(""+user.getInfo().faculty);
		
			JLabel statoCarriera = new JLabel("Stato carriera: ");
			JTextField modificastatoCarriera = new JTextField(""+user.getInfo().careerStatus);
			
			JLabel strutturaRiferimento = new JLabel("Struttura di riferimento: ");
			JTextField modificaStrutturaRiferimento = new JTextField(""+user.getInfo().referenceStructure);
			
			if(user.getInfo().userType==1) {
				contentPane.add(annoImmatricolazione);
				contentPane.add(modificaAnnoImmatricolazione);
				modificaAnnoImmatricolazione.setColumns(10);
			
				contentPane.add(corsoLaurea);
				contentPane.add(modificaCorsoLaurea);
				modificaCorsoLaurea.setColumns(10);
			
				contentPane.add(statoCarriera);
				contentPane.add(modificastatoCarriera);
				modificastatoCarriera.setColumns(10);
			}
			
			else {
				contentPane.add(strutturaRiferimento);
				contentPane.add(modificaStrutturaRiferimento);
				modificaStrutturaRiferimento.setColumns(10);
			}
			
			JButton modifica = new JButton("Modifica dati utente");
			modifica.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					user.getInfo().student_number=mat;
					user.getInfo().name=modificaNome.getText();
					user.getInfo().surname=modificaCognome.getText();
					user.getInfo().email=modificaEmail.getText();
					
					user.getInfo().userType=Integer.parseInt(modificaTipoUtente.getText());
					System.out.println(modificaAnnoImmatricolazione.getText());
					if(modificaAnnoImmatricolazione.getText().equals("")) user.getInfo().registrationYear = null;
	                else user.getInfo().registrationYear = Integer.parseInt(modificaAnnoImmatricolazione.getText());
					user.getInfo().faculty=modificaCorsoLaurea.getText();
					user.getInfo().careerStatus=modificastatoCarriera.getText();
					user.getInfo().referenceStructure=modificaStrutturaRiferimento.getText();
					try {
						UserManager um = new UserManager();
						um.modifiyUserData(user);
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			contentPane.add(modifica);
			
			JButton elimina = new JButton("Elimina dati utente");
			elimina.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UserManager um = new UserManager();
						um.deleteUser(user);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
			});
			contentPane.add(elimina);
			
			JButton sblocca = new JButton("Sblocca utente");
			sblocca.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UserManager um = new UserManager();
						um.unlockUser(user);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
			});
			contentPane.add(sblocca);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
