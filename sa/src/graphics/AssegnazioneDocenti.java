package graphics;

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

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import notifier.Notifier;
import session.Session;
import user.User;

public class AssegnazioneDocenti extends MyFrame {

	private JPanel contentPane;
	private AssegnazioneDocenti thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public AssegnazioneDocenti(HomePage hp, Session ses) {
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
				hp.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel seleziona = new JLabel("Seleziona corso");
		contentPane.add(seleziona);
		
		JTextField corso = new JTextField();
		contentPane.add(corso);
		corso.setColumns(10);
		
		JLabel sel = new JLabel("Seleziona docente");
		contentPane.add(sel);
		
		JTextField docente = new JTextField();
		contentPane.add(docente);
		docente.setColumns(10);
		
		JButton cercaCorsi = new JButton("Assegna");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					User doc = new User();
					doc.createFromStudentNumber(Integer.parseInt(docente.getText()));
					Course c = Notifier.getCourse(corso.getText());
					CourseManagement gc = new CourseManagement();
					gc.coursesAssignment(doc, c);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cercaCorsi);
		
		setVisible(true);
	}

}
