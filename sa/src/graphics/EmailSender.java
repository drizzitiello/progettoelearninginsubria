package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import notifier.Notifier;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;

import javax.mail.MessagingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmailSender extends JFrame {

	private JPanel contentPane;
	private JTextField destinatari;
	private JTextField testoOggetto;
	private JTextField testoCorpo;


	/**
	 * Create the frame.
	 * @param ses 
	 * @param pwd 
	 */
	public EmailSender(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel to = new JLabel("To:");
		contentPane.add(to);
		
		destinatari = new JTextField();
		contentPane.add(destinatari);
		destinatari.setColumns(10);
		
		JLabel oggetto = new JLabel("oggetto");
		contentPane.add(oggetto);
		
		testoOggetto = new JTextField();
		contentPane.add(testoOggetto);
		testoOggetto.setColumns(10);
		
		JLabel corpo = new JLabel("Corpo del messaggio:");
		contentPane.add(corpo);
		
		testoCorpo = new JTextField();
		contentPane.add(testoCorpo);
		testoCorpo.setColumns(50);
		
		JButton bottoneInvio = new JButton("Invia");
		bottoneInvio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!destinatari.getText().contains("@")) {
						if(!Notifier.sendEmail(ses.getUtente().getInfo().email, pwd,
								destinatari.getText(), testoOggetto.getText(), testoCorpo.getText())) {
							JOptionPane.showMessageDialog(bottoneInvio, "Destinatario non valido");
						}
					}
					else{
						Notifier.send_docente_email(ses.getUtente().getInfo().email, pwd,
								destinatari.getText(), testoOggetto.getText(), testoCorpo.getText());
					}
					} catch (MessagingException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(bottoneInvio);
		setVisible(true);
	}

}
