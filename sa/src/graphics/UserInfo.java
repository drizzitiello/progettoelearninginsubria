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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserInfo extends MyFrame {

	private JPanel contentPane;
	private UserInfo thisFrame;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public UserInfo(HomePage hp, Session ses, String pwd) {
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
		
		JLabel studentNumber = new JLabel("Matricola: "+ses.getUser().getInfo().student_number);
		contentPane.add(studentNumber);
		
		JLabel name = new JLabel("Nome: "+ses.getUser().getInfo().name);
		contentPane.add(name);
		
		JLabel surname = new JLabel("Cognome: "+ses.getUser().getInfo().surname);
		contentPane.add(surname);
		
		JLabel email = new JLabel("Email: "+ses.getUser().getInfo().email);
		contentPane.add(email);
		
		String type="";
		switch(ses.getUser().getInfo().userType) {
			case 1: type="Studente";
			break;
			case 2: type="Docente";
			break;
			case 3: type="Amministratore";
			break;
			default:
			break;
		}
		JLabel userType = new JLabel("Tipo Utente: "+type);
		contentPane.add(userType);
		
		if(ses.getUser().getInfo().userType==1) {
			JLabel registrationYear = new JLabel("Anno Immatricolazione: "+ses.getUser().getInfo().registrationYear);
			contentPane.add(registrationYear);
		
			JLabel faculty = new JLabel("Corso di Laurea: "+ses.getUser().getInfo().faculty);
			contentPane.add(faculty);
		
			JLabel careerStatus = new JLabel("Stato carriera: "+ses.getUser().getInfo().careerStatus);
			contentPane.add(careerStatus);
		}
		else {
			JLabel referenceStructure = new JLabel("Struttura di riferimento: "+ses.getUser().getInfo().referenceStructure);
			contentPane.add(referenceStructure);
		}
		
		setVisible(true);
	}

	public UserInfo(ModifyUsersData mdu, Session ses, Object numb) {
		int num = (int) numb;
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
			user.createFromStudentNumber(num);
			JLabel studentNumber = new JLabel("Matricola: "+num);
			contentPane.add(studentNumber);
			
			JLabel name = new JLabel("Nome: ");
			contentPane.add(name);
			JTextField modifyName = new JTextField(user.getInfo().name);
			contentPane.add(modifyName);
			modifyName.setColumns(10);
			
			JLabel surname = new JLabel("Cognome: ");
			contentPane.add(surname);
			JTextField modifySurname = new JTextField(user.getInfo().surname);
			contentPane.add(modifySurname);
			modifySurname.setColumns(10);
			
			JLabel email = new JLabel("Email: ");
			contentPane.add(email);
			JTextField modifyEmail = new JTextField(user.getInfo().email);
			contentPane.add(modifyEmail);
			modifyEmail.setColumns(10);
			
			JLabel userType = new JLabel("Tipo Utente: ");
			contentPane.add(userType);
			JTextField modifyUserType = new JTextField(""+user.getInfo().userType);
			contentPane.add(modifyUserType);
			modifyUserType.setColumns(10);
			
			
			JLabel registrationYear = new JLabel("Anno Immatricolazione: ");
			JTextField modifyRegistrationYear;
			if(user.getInfo().registrationYear==null) {
				modifyRegistrationYear = new JTextField("");
			}
			else {
				modifyRegistrationYear = new JTextField(""+user.getInfo().registrationYear);
			}
			
			JLabel faculty = new JLabel("Corso di Laurea: ");
			JTextField modifyFaculty = new JTextField(""+user.getInfo().faculty);
		
			JLabel careerStatus = new JLabel("Stato carriera: ");
			JTextField modifyCareerStatus = new JTextField(""+user.getInfo().careerStatus);
			
			JLabel referenceStructure = new JLabel("Struttura di riferimento: ");
			JTextField modifyReferenceStructure = new JTextField(""+user.getInfo().referenceStructure);
			
			if(user.getInfo().userType==1) {
				contentPane.add(registrationYear);
				contentPane.add(modifyRegistrationYear);
				modifyRegistrationYear.setColumns(10);
			
				contentPane.add(faculty);
				contentPane.add(modifyFaculty);
				modifyFaculty.setColumns(10);
			
				contentPane.add(careerStatus);
				contentPane.add(modifyCareerStatus);
				modifyCareerStatus.setColumns(10);
			}
			
			else {
				contentPane.add(referenceStructure);
				contentPane.add(modifyReferenceStructure);
				modifyReferenceStructure.setColumns(10);
			}
			
			JButton modify = new JButton("Modifica dati utente");
			modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					user.getInfo().student_number=num;
					user.getInfo().name=modifyName.getText();
					user.getInfo().surname=modifySurname.getText();
					user.getInfo().email=modifyEmail.getText();
					
					user.getInfo().userType=Integer.parseInt(modifyUserType.getText());
					System.out.println(modifyRegistrationYear.getText());
					if(modifyRegistrationYear.getText().equals("")) user.getInfo().registrationYear = 0;
	                else user.getInfo().registrationYear = Integer.parseInt(modifyRegistrationYear.getText());
					user.getInfo().faculty=modifyFaculty.getText();
					user.getInfo().careerStatus=modifyCareerStatus.getText();
					user.getInfo().referenceStructure=modifyReferenceStructure.getText();
					try {
						UserManager um = new UserManager();
						um.modifiyUserData(user);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione al database");
						e1.printStackTrace();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore");
						e1.printStackTrace();
					}
				}
			});
			contentPane.add(modify);
			
			JButton delete = new JButton("Elimina dati utente");
			delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UserManager um = new UserManager();
						um.deleteUser(user);
						mdu.setVisible(true);
						thisFrame.setVisible(false);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione al database");
						e1.printStackTrace();
					}
					
				}
			});
			contentPane.add(delete);
			
			JButton unlock = new JButton("Sblocca utente");
			unlock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UserManager um = new UserManager();
						um.unlockUser(user);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione al database");
						e1.printStackTrace();
					}
					
				}
			});
			contentPane.add(unlock);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane, "Errore");
			e.printStackTrace();
		}
		
		setVisible(true);
	}

}
