package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import notifier.Notifier;
import session.Session;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;

import javax.mail.MessagingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmailSender extends MyFrame {

	private JPanel contentPane;
	private JTextField receivers;
	private JTextField objectText;
	private JTextArea bodyText;
	private EmailSender thisFrame;


	/**
	 * Create the frame.
	 * @param ses 
	 * @param pwd 
	 */
	public EmailSender(HomePage hp, Session ses, String pwd) {
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
		
		JLabel to = new JLabel("To:");
		contentPane.add(to);
		
		receivers = new JTextField();
		contentPane.add(receivers);
		receivers.setColumns(10);
		
		JLabel object = new JLabel("oggetto");
		contentPane.add(object);
		
		objectText = new JTextField();
		contentPane.add(objectText);
		objectText.setColumns(10);
		
		JLabel body = new JLabel("Corpo del messaggio:");
		contentPane.add(body);
		
		bodyText = new JTextArea();
		contentPane.add(bodyText);
		bodyText.setColumns(30);
		bodyText.setRows(20);
		
		JButton submitButton = new JButton("Invia");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Notifier n = new Notifier();
					if(!receivers.getText().contains("@")) {
						if(!n.sendMail(ses.getUser().getInfo().email, pwd,
								receivers.getText(), objectText.getText(), bodyText.getText())) {
							JOptionPane.showMessageDialog(submitButton, "Destinatario non valido");
						}
					}
					else{
						Notifier.send_professor_email(ses.getUser().getInfo().email, pwd,
								receivers.getText(), objectText.getText(), bodyText.getText());
					}
					} catch (MessagingException e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore durante l'invio dell'email");
					e1.printStackTrace();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore durante l'invio dell'email");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(submitButton);
		setVisible(true);
	}

}
