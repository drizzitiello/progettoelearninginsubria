package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import authService.AuthenticationService;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResetPassword extends MyFrame {

	private JPanel contentPane;
	private JTextField codAttivazione;
	private JTextField newPwd;
	private JTextField newPwd2;
	private ResetPassword thisframe;

	/**
	 * Create the frame.
	 */
	public ResetPassword(JFrame l) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisframe=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel codice = new JLabel("Inserire codice di attivazione");
		contentPane.add(codice);
		
		codAttivazione = new JTextField();
		contentPane.add(codAttivazione);
		codAttivazione.setColumns(10);
		
		JLabel password = new JLabel("Inserire nuova password");
		contentPane.add(password);
		
		newPwd = new JTextField();
		contentPane.add(newPwd);
		newPwd.setColumns(10);
		
		JLabel p = new JLabel("Inserire nuovamente la password");
		contentPane.add(p);
		
		newPwd2 = new JTextField();
		contentPane.add(newPwd2);
		newPwd2.setColumns(10);
		
		JButton reset = new JButton("Imposta nuova password");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(newPwd.getText().equals(newPwd2.getText())){
					try {
						AuthenticationService as = new AuthenticationService();
						as.storeNewPassword(newPwd.getText());
						as.resetLoginAttempts();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(reset, "Hai inserito 2 password diverse");
				}
			}
		});
		contentPane.add(reset);
		
		setVisible(true);
	}

}
