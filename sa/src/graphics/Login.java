package graphics;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;

import Sessione.Sessione;

import javax.swing.JButton;

import javax.mail.MessagingException;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import authService.AuthenticationService;
import socketDb.SocketDb;

public class Login {

	private JFrame frame;
	private JTextField pwd;
	private JTextField email;
	AuthenticationService as;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Login() throws ClassNotFoundException, SQLException {
		initialize();
		as = new AuthenticationService(SocketDb.getInstanceDb());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblLogin = new JLabel("Autenticazione");
		
		JLabel emailLabel = new JLabel("Email");
		
		email = new JTextField();
		email.setColumns(10);
		
		JLabel pwdLabel = new JLabel("Password");
		
		pwd = new JTextField();
		pwd.setColumns(10);
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String log = as.login(email.getText(), pwd.getText());
					if(log.equals("Credenziali corrette")) {
						new HomePage(Sessione.getInstance(), pwd.getText());
					}
					else {
						JOptionPane.showMessageDialog(loginButton, log);
					}
				} catch (Exception e) {
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
		
		JLabel recuperoPwd = new JLabel("Hai dimenticato la password? Effettua il reset");
		frame.getContentPane().add(recuperoPwd);
		
		JButton recuperoButton = new JButton("Reset password");
		recuperoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!email.getText().equals("")) {
						as.sendNewLoginCredentials(email.getText());
					}
					else {
						JOptionPane.showMessageDialog(recuperoButton,"Inserire l'email nel campo apposito");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(recuperoButton);
	}

}
