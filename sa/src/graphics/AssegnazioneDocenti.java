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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import courseManagement.CourseManagement;
import notifier.Notifier;
import session.Session;
import user.User;
import userManager.UserManager;

public class AssegnazioneDocenti extends MyFrame {

	private JPanel contentPane;
	private AssegnazioneDocenti thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public AssegnazioneDocenti(HomePage hp, Session ses) throws MalformedURLException, RemoteException, NotBoundException {
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
		
		try {
			FindCourse fc = new FindCourse();
			ArrayList<Course> courses = fc.getCourses();
			String[] corsi = new String[courses.size()];
			int i=0;
			for(Course c : courses) {
				String corso = c.name;
				corsi[i] = corso;
				i++;
			}
			JComboBox selezionaCorso = new JComboBox(corsi);
			selezionaCorso.setMaximumRowCount(100);
			contentPane.add(selezionaCorso);
			
			JLabel sel = new JLabel("Seleziona docente");
			contentPane.add(sel);
			
			JComboBox selezionaDocente;
			ArrayList<Map<String, Object>> hm2;
			try {
				UserManager um = new UserManager();
				ArrayList<User> professors = um.getProfessors();
				String[] docenti = new String[professors.size()];
				int k=0;
				for(User p : professors) {
					String docente = p.getInfo().name;
					docente += " "+p.getInfo().surname;
					docenti[k] = docente;
					k++;
				}
				selezionaDocente = new JComboBox(docenti);
				selezionaDocente.setMaximumRowCount(100);
				contentPane.add(selezionaDocente);
				
				JLabel matricola = new JLabel("Matricola docente: ");
				contentPane.add(matricola);
				
				JLabel numero = new JLabel("");
				contentPane.add(numero);
				
				selezionaDocente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							User u = new User();
							String nomeCognome = selezionaDocente.getSelectedItem().toString();
							StringTokenizer st = new StringTokenizer(nomeCognome, " ");  
						    String nome = st.nextToken();
						    String cognome = st.nextToken();
						    int matr = u.getStudentNumber(nome, cognome);
							String num = ""+matr;
							numero.setText(""+num);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				
				JButton assegnaCorsi = new JButton("Assegna");
				assegnaCorsi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							User doc = new User();
							doc.createFromStudentNumber(Integer.parseInt(numero.getText()));
							Course c = Notifier.getCourse(selezionaCorso.getSelectedItem().toString());
							CourseManagement gc = new CourseManagement();
							gc.coursesAssignment(doc, c);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(assegnaCorsi);
				
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
		setVisible(true);
	}

}
