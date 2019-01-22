package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Section;
import session.Session;

public class ModificaSezione extends MyFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 * @param string 
	 */
	public ModificaSezione(Session ses, Course cor, int codSezione) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Section s = null;
		CourseContentManagement gc = new CourseContentManagement();
		try {
			s=gc.getSection(codSezione);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		final int matricola=s.studentNumber;
		final int codCorso=s.courseCode;
		final Integer figlioDi=s.sonOf;
		
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);
		
		JTextField descrizione = new JTextField(s.description);
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JLabel tit = new JLabel("Titolo: ");
		contentPane.add(tit);
		
		JTextField titolo = new JTextField(s.title);
		contentPane.add(titolo);
		titolo.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);
		
		JTextField visibilita = new JTextField(String.valueOf(s.visibility));
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JButton modificaSezione = new JButton("Modifica sezione");
		modificaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean pubblica=false;
					if(visibilita.getText().equals("pubblica")) {
						pubblica=true;
					}
					Section sezione = new Section(titolo.getText(),descrizione.getText(),
							pubblica, codSezione, matricola, codCorso, figlioDi);
					gc.modifySection(sezione);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modificaSezione);
		
		JButton creaRisorsa = new JButton("Crea risorsa");
		creaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaRisorsa ms = new CreaRisorsa(ses, cor);
			}
		});
		contentPane.add(creaRisorsa);
		
		JButton cancellaQuestaSezione = new JButton("Cancella questa sezione");
		cancellaQuestaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseContentManagement gc = new CourseContentManagement();
				try {
					gc.cancelSection(codSezione);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				//torna indietro
			}
		});
		contentPane.add(cancellaQuestaSezione);
		
		setVisible(true);
	}

}
