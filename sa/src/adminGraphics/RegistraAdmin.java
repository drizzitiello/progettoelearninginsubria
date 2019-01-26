package adminGraphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import user.User;
import userManager.UserManager;

public class RegistraAdmin extends JFrame {

	private JPanel contentPane;
	private RegistraAdmin thisFrame;

	/**
	 * Create the frame.
	 */
	public RegistraAdmin(AdminLogin al) {
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
				al.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel inserisci = new JLabel("Inserisci dati per registrazione");
		contentPane.add(inserisci);
		
		JLabel matricolaLabel = new JLabel("Matricola");
		contentPane.add(matricolaLabel);
		
		JTextField matricola = new JTextField();
		matricola.setColumns(10);
		contentPane.add(matricola);
		
		JLabel cognomeLabel = new JLabel("Cognome");
		contentPane.add(cognomeLabel);
		
		JTextField cognome = new JTextField();
		cognome.setColumns(10);
		contentPane.add(cognome);
		
		JLabel nomeLabel = new JLabel("Nome");
		contentPane.add(nomeLabel);
		
		JTextField nome = new JTextField();
		nome.setColumns(10);
		contentPane.add(nome);
		
		JLabel emailLabel = new JLabel("Email");
		contentPane.add(emailLabel);
		
		JTextField email = new JTextField();
		email.setColumns(10);
		contentPane.add(email);
		
		JLabel strutturaLabel = new JLabel("Struttura");
		contentPane.add(strutturaLabel);
		
		JTextField struttura = new JTextField();
		struttura.setColumns(10);
		contentPane.add(struttura);
		
		JLabel passwordLabel = new JLabel("Password");
		contentPane.add(passwordLabel);
		
		JTextField password = new JTextField();
		password.setColumns(10);
		contentPane.add(password);
		
		JButton loginButton = new JButton("Registrati");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UserManager um = new UserManager("");
					User.UserInfo info = new User.UserInfo();
					info.student_number=Integer.parseInt(matricola.getText());
					info.surname=cognome.getText();
					info.name=nome.getText();
					info.email=email.getText();
					info.referenceStructure=struttura.getText();
					info.userType=3;
					String pwd=password.getText();
					um.createUser(info, pwd);
					
					al.setVisible(true);
					thisFrame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(loginButton);
		
		setVisible(true);
	}

}
