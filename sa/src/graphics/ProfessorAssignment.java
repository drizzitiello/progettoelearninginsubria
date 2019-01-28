package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import courseManagement.CourseManagement;
import notifier.Notifier;
import session.Session;
import user.User;
import userManager.UserManager;

public class ProfessorAssignment extends MyFrame {

	private JPanel contentPane;
	private ProfessorAssignment thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public ProfessorAssignment(HomePage hp, Session ses) throws MalformedURLException, RemoteException, NotBoundException {
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
		
		JLabel select = new JLabel("Seleziona corso");
		contentPane.add(select);
		
		try {
			FindCourse fc = new FindCourse();
			ArrayList<Course> courses = fc.getCourses();
			String[] coursesName = new String[courses.size()];
			int i=0;
			for(Course c : courses) {
				String corso = c.name;
				coursesName[i] = corso;
				i++;
			}
			JComboBox selectCourse = new JComboBox(coursesName);
			selectCourse.setMaximumRowCount(100);
			contentPane.add(selectCourse);
			
			JLabel sel = new JLabel("Seleziona docente");
			contentPane.add(sel);
			
			try {
				UserManager um = new UserManager();
				ArrayList<User> professors = um.getProfessors();
				String[] professorsName = new String[professors.size()];
				int k=0;
				for(User p : professors) {
					String docente = p.getInfo().name;
					docente += " "+p.getInfo().surname;
					professorsName[k] = docente;
					k++;
				}
				JComboBox selectProfessor = new JComboBox(professorsName);
				selectProfessor.setMaximumRowCount(100);
				contentPane.add(selectProfessor);
				
				JLabel studentNumber = new JLabel("Matricola docente: ");
				contentPane.add(studentNumber);
				
				User u = new User();
				String nameSurname = selectProfessor.getSelectedItem().toString();
				StringTokenizer st = new StringTokenizer(nameSurname, " ");  
			    String name = st.nextToken();
			    String surname = st.nextToken();
			    int numb = u.getStudentNumber(name, surname);
				String num = ""+numb;
				JLabel number = new JLabel(num);
				contentPane.add(number);
				
				selectProfessor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							User u = new User();
							String nameSurname = selectProfessor.getSelectedItem().toString();
							StringTokenizer st = new StringTokenizer(nameSurname, " ");  
						    String name = st.nextToken();
						    String surname = st.nextToken();
						    int numb = u.getStudentNumber(name, surname);
							String num = ""+numb;
							number.setText(""+num);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane, "Errore nel caricamento dei docenti");
							e1.printStackTrace();
						}
					}
				});
				
				JButton assignCourse = new JButton("Assegna");
				assignCourse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							User doc = new User();
							doc.createFromStudentNumber(Integer.parseInt(number.getText()));
							Notifier n = new Notifier();
							Course c = n.getCourse(selectCourse.getSelectedItem().toString());
							CourseManagement gc = new CourseManagement();
							gc.coursesAssignment(doc, c);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane, "Errore nell'assegnazione del corso");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(assignCourse);
				
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} catch (ClassNotFoundException | SQLException e1) {
			JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
			e1.printStackTrace();
		}
		
		setVisible(true);
	}

}
