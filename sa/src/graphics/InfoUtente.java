package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import socketDb.SocketDb;
import UserManager.UserManager;
import Utente.Utente;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class InfoUtente extends MyFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public InfoUtente(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 4, 0, 0));
		
		JLabel matricola = new JLabel("Matricola: "+ses.getUtente().getInfo().matricola);
		contentPane.add(matricola);
		
		JLabel nome = new JLabel("Nome: "+ses.getUtente().getInfo().nome);
		contentPane.add(nome);
		
		JLabel cognome = new JLabel("Cognome: "+ses.getUtente().getInfo().cognome);
		contentPane.add(cognome);
		
		JLabel email = new JLabel("Email: "+ses.getUtente().getInfo().email);
		contentPane.add(email);
		
		String tipo="";
		switch(ses.getUtente().getInfo().tipoUtente) {
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
		
		if(ses.getUtente().getInfo().tipoUtente==1) {
			JLabel annoImmatricolazione = new JLabel("Anno Immatricolazione: "+ses.getUtente().getInfo().annoImmatricolazione);
			contentPane.add(annoImmatricolazione);
		
			JLabel corsoLaurea = new JLabel("Corso di Laurea: "+ses.getUtente().getInfo().corsoLaurea);
			contentPane.add(corsoLaurea);
		
			JLabel statoCarriera = new JLabel("Stato carriera: "+ses.getUtente().getInfo().statoCarriera);
			contentPane.add(statoCarriera);
		}
		else {
			JLabel strutturaRiferimento = new JLabel("Struttura di riferimento: "+ses.getUtente().getInfo().strutturaRiferimento);
			contentPane.add(strutturaRiferimento);
		}
		
		setVisible(true);
	}

	public InfoUtente(Sessione ses, Object matr) {
		int mat = (int) matr;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		Utente user;
		try {
			user = new Utente();
			user.createFromMatricola(mat);
			JLabel matricola = new JLabel("Matricola: "+mat);
			contentPane.add(matricola);
			
			JLabel nome = new JLabel("Nome: ");
			contentPane.add(nome);
			JTextField modificaNome = new JTextField(user.getInfo().nome);
			contentPane.add(modificaNome);
			modificaNome.setColumns(10);
			
			JLabel cognome = new JLabel("Cognome: ");
			contentPane.add(cognome);
			JTextField modificaCognome = new JTextField(user.getInfo().cognome);
			contentPane.add(modificaCognome);
			modificaCognome.setColumns(10);
			
			JLabel email = new JLabel("Email: ");
			contentPane.add(email);
			JTextField modificaEmail = new JTextField(user.getInfo().email);
			contentPane.add(modificaEmail);
			modificaEmail.setColumns(10);
			
			JLabel tipoUtente = new JLabel("Tipo Utente: ");
			contentPane.add(tipoUtente);
			JTextField modificaTipoUtente = new JTextField(""+user.getInfo().tipoUtente);
			contentPane.add(modificaTipoUtente);
			modificaTipoUtente.setColumns(10);
			
			
			JLabel annoImmatricolazione = new JLabel("Anno Immatricolazione: ");
			JTextField modificaAnnoImmatricolazione;
			if(user.getInfo().annoImmatricolazione==null) {
				modificaAnnoImmatricolazione = new JTextField("");
			}
			else {
				modificaAnnoImmatricolazione = new JTextField(""+user.getInfo().annoImmatricolazione);
			}
			
			JLabel corsoLaurea = new JLabel("Corso di Laurea: ");
			JTextField modificaCorsoLaurea = new JTextField(""+user.getInfo().corsoLaurea);
		
			JLabel statoCarriera = new JLabel("Stato carriera: ");
			JTextField modificastatoCarriera = new JTextField(""+user.getInfo().statoCarriera);
			
			JLabel strutturaRiferimento = new JLabel("Struttura di riferimento: ");
			JTextField modificaStrutturaRiferimento = new JTextField(""+user.getInfo().strutturaRiferimento);
			
			if(user.getInfo().tipoUtente==1) {
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
					/*Object[] params= {modificaMatricola.getText(), modificaNome.getText(), modificaCognome.getText(),
							modificaEmail.getText(), modificaTipoUtente.getText(), modificaAnnoImmatricolazione.getText(),
							modificaCorsoLaurea.getText(), modificaFacolta.getText(),
							modificastatoCarriera.getText(), modificaStrutturaRiferimento.getText()};*/
					user.getInfo().matricola=mat;
					user.getInfo().nome=modificaNome.getText();
					user.getInfo().cognome=modificaCognome.getText();
					user.getInfo().email=modificaEmail.getText();
					
					user.getInfo().tipoUtente=Integer.parseInt(modificaTipoUtente.getText());
					System.out.println(modificaAnnoImmatricolazione.getText());
					if(modificaAnnoImmatricolazione.getText().equals("")) user.getInfo().annoImmatricolazione = null;
	                else user.getInfo().annoImmatricolazione = Integer.parseInt(modificaAnnoImmatricolazione.getText());
					user.getInfo().corsoLaurea=modificaCorsoLaurea.getText();
					user.getInfo().statoCarriera=modificastatoCarriera.getText();
					user.getInfo().strutturaRiferimento=modificaStrutturaRiferimento.getText();
					try {
						UserManager um = new UserManager();
						um.modificaDatiUtente(user);
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
						um.eliminaUtente(user);
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
						um.sbloccaUtente(user);
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
