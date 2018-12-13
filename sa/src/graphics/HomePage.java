package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import notifier.Notifier;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class HomePage extends JFrame {

	private JPanel contentPane;
	private JTextField corso;
	private JList<JButton> list ;

	/**
	 * Create the frame.
	 * @param pwd 
	 */
	public HomePage(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel cerca = new JLabel("Cerca corsi");
		contentPane.add(cerca);
		
		corso = new JTextField();
		contentPane.add(corso);
		corso.setColumns(10);
		
		JButton cercaCorsi = new JButton("Cerca");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Integer i = Notifier.getCorso(corso.getText());
					if(i!=null) {list.add(new JButton(corso.getText()));}
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cercaCorsi);
		
		JButton email = new JButton("Accedi all'email");
		email.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailSender eh = new EmailSender(ses, pwd);
			}
		});
		contentPane.add(email);
		
		list = new JList<JButton>();
		contentPane.add(list);
		setVisible(true);
	}

}
