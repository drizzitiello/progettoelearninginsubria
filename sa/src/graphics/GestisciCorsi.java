package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import session.Session;

public class GestisciCorsi extends MyFrame {

	private JPanel contentPane;
	private GestisciCorsi thisFrame;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public GestisciCorsi(HomePage hp, Session ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
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
		
		FindCourse rc = new FindCourse();
		try {
			ArrayList<Course> ali = rc.getCourses();
			for(Course c : ali) {
				JLabel codiceCorso = new JLabel("Codice corso: "+((Integer) c.courseCode).toString());
				contentPane.add(codiceCorso);
				JLabel nome = new JLabel("Nome: "+c.name);
				contentPane.add(nome);
				JLabel annoAttivazione = new JLabel("Anno attivazione: "+String.valueOf(c.activation_year));
				contentPane.add(annoAttivazione);
				JLabel facolta = new JLabel("Facolta: "+c.faculty);
				contentPane.add(facolta);
				JLabel descrizione = new JLabel("Descrizione: "+c.description);
				contentPane.add(descrizione);
				JLabel peso = new JLabel("CFU: "+((Integer) c.weight).toString());
				contentPane.add(peso);
				JButton modificaCorso = new JButton("Modifica Corso");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisFrame.setVisible(false);
						ModificaCorso ac = new ModificaCorso(thisFrame, ses, c);
					}
				});
				contentPane.add(modificaCorso);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		JButton aggiungiCorso = new JButton("Aggiungi corso");
		aggiungiCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AggiungiCorso ac = new AggiungiCorso(thisFrame, ses, pwd);
			}
		});
		contentPane.add(aggiungiCorso);
		setVisible(true);
	}

}
