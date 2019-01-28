package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import session.Session;

public class AddCourse extends MyFrame {

	private JPanel contentPane;
	private AddCourse thisframe;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public AddCourse(ManageCourses mc, Session ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisframe=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel code = new JLabel("Codice corso: ");
		contentPane.add(code);
		JTextField courseCode = new JTextField();
		contentPane.add(courseCode);
		courseCode.setColumns(10);
		
		JLabel name = new JLabel("Nome: ");
		contentPane.add(name);
		JTextField insertName = new JTextField();
		contentPane.add(insertName);
		insertName.setColumns(10);
		
		JLabel activationYear = new JLabel("Anno di attivazione: ");
		contentPane.add(activationYear);
		JTextField annoAttiv = new JTextField();
		contentPane.add(annoAttiv);
		annoAttiv.setColumns(10);
		
		JLabel faculty = new JLabel("Facolta di riferimento: ");
		contentPane.add(faculty);
		JTextField fac = new JTextField();
		contentPane.add(fac);
		fac.setColumns(10);
		
		JLabel description = new JLabel("Descrizione");
		contentPane.add(description);
		JTextField desc = new JTextField();
		contentPane.add(desc);
		desc.setColumns(10);
		
		JLabel weight = new JLabel("CFU: ");
		contentPane.add(weight);
		JTextField wght = new JTextField();
		contentPane.add(wght);
		wght.setColumns(10);
		
		JButton addCourse = new JButton("AggiungiCorso");
		addCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CourseManagement gc = new CourseManagement();
					Course course = new Course();
					course.setCourseCode(Integer.parseInt(courseCode.getText()));
	                course.setName(insertName.getText());
	                course.setYear(Integer.parseInt(annoAttiv.getText()));
	                course.setFaculty(fac.getText());
	                course.setDescription(desc.getText());
	                course.setWeight(Integer.parseInt(wght.getText()));
	                course.setCreator(ses.getUser().getInfo().student_number);
					gc.createCourse(course);
				} catch (ClassNotFoundException | SQLException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nella connessione al database");
					e1.printStackTrace();
				} catch (MalformedURLException | RemoteException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(addCourse);
		setVisible(true);
	}

}
