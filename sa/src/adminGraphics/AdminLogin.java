package adminGraphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import server.Server;
import socketDb.SocketDb;
import userManager.UserManager;

public class AdminLogin extends JFrame{
	
	private JPanel contentPane;
	private AdminLogin thisFrame;
	private Server server;
	
	public AdminLogin() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 450, 300);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	
	thisFrame=this;
	
	JLabel lblLogin = new JLabel("Avvia database");
	contentPane.add(lblLogin);
	
	JLabel hostLabel = new JLabel("Host");
	contentPane.add(hostLabel);
	
	JTextField host = new JTextField();
	host.setColumns(10);
	contentPane.add(host);
	
	JLabel emailLabel = new JLabel("Utente");
	contentPane.add(emailLabel);
	
	JTextField email = new JTextField();
	email.setColumns(10);
	contentPane.add(email);
	
	JLabel pwdLabel = new JLabel("Password");
	contentPane.add(pwdLabel);
	
	JPasswordField pwd = new JPasswordField();
	pwd.setColumns(10);
	contentPane.add(pwd);
	
	JButton startButton = new JButton("Avvia");
	startButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			try {
				//server = new Server(SocketDb.getAdminInstanceDb(host.getText(), email.getText(), String.valueOf(pwd.getPassword())));
				server=Server.getInstance(SocketDb.getAdminInstanceDb(host.getText(), email.getText(), String.valueOf(pwd.getPassword())));
				server.bind();
				UserManager um = new UserManager("");
				if(!um.dbContainsAdmin()) {
					RegistraAdmin ra = new RegistraAdmin(thisFrame);
					thisFrame.setVisible(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	contentPane.add(startButton);
	
	JButton stopButton = new JButton("Stoppa server");
	stopButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			try {
				System.out.println(SocketDb.getInstanceDb().isActive());
				if(server==null||server.isActive()) {
					JOptionPane.showMessageDialog(stopButton, "Il server non è attivo");
				}
				else {
					server.close();
					SocketDb.getInstanceDb().destroySql();
					JOptionPane.showMessageDialog(stopButton, "Il server è stato stoppato");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	contentPane.add(stopButton);
	
	setVisible(true);
	}
}
