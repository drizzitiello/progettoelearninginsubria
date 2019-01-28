package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseManagement.CourseManagement;
import session.Session;
import userManager.UserManager;

public class UsersCoursesRegistration extends MyFrame {

	private JPanel contentPane;
	private UsersCoursesRegistration thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public UsersCoursesRegistration(HomePage hp, Session ses) {
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
		
		JLabel insert = new JLabel("Inserisci percorso file csv da caricare");
		contentPane.add(insert);
		
		JTextField path = new JTextField();
		contentPane.add(path);
		path.setColumns(10);
		
		JButton addUsers = new JButton("Aggiungi utenti");
		addUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserManager um = new UserManager();
					um.csvImportUser(path.getText());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane,"Errore durante il caricamento dei file");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(addUsers);
		
		JButton addCourses = new JButton("Aggiungi corsi");
		addCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CourseManagement gc = new CourseManagement();
					gc.dataInput(path.getText());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane,"Errore durante il caricamento dei file");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(addCourses);
		
		setVisible(true);
	}

}
