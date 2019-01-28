package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import session.Session;

public class ManageCourses extends MyFrame {

	private JPanel contentPane;
	private ManageCourses thisFrame;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public ManageCourses(HomePage hp, Session ses, String pwd) {
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
		
		FindCourse rc;
		try {
			rc = new FindCourse();
			try {
				ArrayList<Course> ali = rc.getCourses();
				for(Course c : ali) {
					Box course = Box.createVerticalBox();
					JLabel courseCode = new JLabel("  Codice corso: "+((Integer) c.courseCode).toString());
					course.add(courseCode);
					JLabel name = new JLabel("  Nome: "+c.name);
					course.add(name);
					JLabel activationYear = new JLabel("  Anno attivazione: "+String.valueOf(c.activation_year));
					course.add(activationYear);
					JLabel faculty = new JLabel("  Facolta: "+c.faculty);
					course.add(faculty);
					String descr = "  Descrizione: "+c.description;
					if (descr.length()>35) descr="  Descrizione: "+c.description.substring(0, 5)+"...";
					JLabel description = new JLabel(descr);
					course.add(description);
					JLabel weight = new JLabel("  CFU: "+((Integer) c.weight).toString()+"  ");
					course.add(weight);
					JButton modifyCourse = new JButton("Modifica Corso");
					modifyCourse.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							ModifyCourse ac = new ModifyCourse(thisFrame, ses, c);
						}
					});
					course.add(modifyCourse);
					contentPane.add(course);
				}
			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(contentPane, "Errore durante la connessione al databse");
				e.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			JOptionPane.showMessageDialog(contentPane, "Errore durante la connessione al database");
			e1.printStackTrace();
		}
		JButton addCourse = new JButton("Aggiungi corso");
		addCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCourse ac = new AddCourse(thisFrame, ses, pwd);
			}
		});
		contentPane.add(addCourse);
		setVisible(true);
	}
}
