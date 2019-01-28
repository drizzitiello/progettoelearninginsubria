package graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import authService.AuthenticationService;
import session.Session;

public class Login{

	private JFrame frame;
	private JPasswordField pwd;
	private JTextField email;
	AuthenticationService as;

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Login() throws ClassNotFoundException, SQLException {
		initialize();
		try {
			as = new AuthenticationService();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblLogin = new JLabel("Autenticazione");
		
		JLabel emailLabel = new JLabel("Email");
		
		email = new JTextField();
		email.setColumns(10);
		
		JLabel pwdLabel = new JLabel("Password");
		
		pwd = new JPasswordField();
		pwd.setColumns(10);
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String log = as.login(email.getText(), String.valueOf(pwd.getPassword()));
					if(log.equals("Credenziali corrette")) {
						frame.setVisible(false);
						new HomePage(Session.getInstance(),String.valueOf(pwd.getPassword()));
					}
					else if(log.equals("Procedura Attivazione")) {
						frame.setVisible(false);
						new ResetPassword(frame);
					}
					else {
						JOptionPane.showMessageDialog(loginButton, log);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "Errore di connessione");
					e.printStackTrace();
				}
			}
		});
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		frame.getContentPane().add(lblLogin);
		frame.getContentPane().add(emailLabel);
		frame.getContentPane().add(email);
		frame.getContentPane().add(pwdLabel);
		frame.getContentPane().add(pwd);
		frame.getContentPane().add(loginButton);
		
		JLabel pwdRecovery = new JLabel("Hai dimenticato la password? Effettua il reset");
		frame.getContentPane().add(pwdRecovery);
		
		JButton recoveryButton = new JButton("Reset password");
		recoveryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!email.getText().equals("")) {
						as.sendNewLoginCredentials(email.getText());
					}
					else {
						JOptionPane.showMessageDialog(recoveryButton,"Inserire l'email nel campo apposito");
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame,"Errore di connessione al database");
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(recoveryButton);
		frame.setVisible(true);
	}
	
}
