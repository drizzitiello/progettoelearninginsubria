package graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import notifier.Notifier;
import session.Session;
import socketDb.SocketDb;
import userManager.UserManager;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.Box;

public class HomePage extends MyFrame {

	private JPanel contentPane;
	private JTextField course;
	private JButton last;
	private ArrayList<JButton> la;
	private HomePage hp;

	/**
	 * Create the frame.
	 * @param pwd 
	 */
	public HomePage(Session ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		hp=this;
		try {
		if(!ses.create(ses.getUser().getInfo().student_number)) {
			JOptionPane.showMessageDialog(contentPane, "Troppi utenti connessi");
		}
		else {
		
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ses.destroy();
					Login l = new Login();
					hp.setVisible(false);
				} catch (ClassNotFoundException | SQLException | RemoteException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nella distruzione della sessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(backButton);
		
		JLabel find = new JLabel("Cerca corsi");
		contentPane.add(find);
		
		course = new JTextField();
		contentPane.add(course);
		course.setColumns(10);
		
		Box verticalBox = Box.createVerticalBox();
		
		JButton findCourses = new JButton("Cerca");
		findCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(last!=null)verticalBox.remove(last);
					Notifier n = new Notifier();
					Course i = n.getCourse(course.getText());
					if(i!=null) {
						JButton b = new JButton(course.getText());
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								CoursePage pc = new CoursePage(hp, ses, course.getText(), false);
								hp.setVisible(false);
							}
						});
						last=b;
						verticalBox.add(b);
						contentPane.revalidate();
					    validate();
					}
					else {
						JOptionPane.showMessageDialog(findCourses, "Il corso cercato non esiste");
					}
				} catch (ClassNotFoundException | SQLException | RemoteException | MalformedURLException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nel caricamento dei corsi");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(findCourses);
		contentPane.add(verticalBox);
		
		Box verticalBox2 = Box.createVerticalBox();
		
		JButton assignedCourses = new JButton("I miei corsi");
		assignedCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(la!=null) for(JButton j : la) {
						verticalBox2.remove(j);
						contentPane.revalidate();validate();
					}
					
					UserManager um = new UserManager();
					ArrayList<String> c = um.getStudentCourses(ses.getUser().getInfo().student_number);
					
					for(String s : c) {
						JButton b = new JButton(s);
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								CoursePage pc = new CoursePage(hp, ses, b.getText(), false);
								hp.setVisible(false);
							}
						});
						la.add(b);
						verticalBox2.add(b);
						}
					contentPane.revalidate();
					validate();
				} catch (ClassNotFoundException | SQLException | RemoteException e1) {
					JOptionPane.showMessageDialog(contentPane, "Errore nel caricamento dei corsi");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(assignedCourses);
		contentPane.add(verticalBox2);
		
		if(ses.getUser().getInfo().userType!=1) {
			JButton email = new JButton("Accedi all'email");
			email.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmailSender eh = new EmailSender(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(email);
		}
		
		JButton visualizeInfo = new JButton("Informazioni personali");
		visualizeInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserInfo eh = new UserInfo(hp, ses, pwd);
				hp.setVisible(false);
			}
		});
		contentPane.add(visualizeInfo);
		
		
		
		if(ses.getUser().getInfo().userType==3) {
			JButton modifyUsersData = new JButton("Modifica dati utenti");
			modifyUsersData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModifyUsersData mdu = new ModifyUsersData(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(modifyUsersData);
			
			JButton addCourses = new JButton("Gestisci corsi");
			addCourses.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ManageCourses mdu = new ManageCourses(hp, ses, pwd);
					hp.setVisible(false);
				}
			});
			contentPane.add(addCourses);
			
			JButton statistics = new JButton("Analisi statistiche");
			statistics.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Statistics mdu = new Statistics(hp, ses);
					hp.setVisible(false);
				}
			});
			contentPane.add(statistics);
			
			JButton registerUsers = new JButton("Registra nuovi utenti e corsi");
			registerUsers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UsersCoursesRegistration mdu = new UsersCoursesRegistration(hp, ses);
					hp.setVisible(false);
				}
			});
			contentPane.add(registerUsers);
			
			JButton assignProfessor = new JButton("Assegna docenti a corsi");
			assignProfessor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ProfessorAssignment mdu = new ProfessorAssignment(hp, ses);
					} catch (MalformedURLException | RemoteException | NotBoundException e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
						e1.printStackTrace();
					}
					hp.setVisible(false);
				}
			});
			contentPane.add(assignProfessor);
		}
		
		la=new ArrayList<JButton>();
		setVisible(true);
		}
		}
		catch(NullPointerException e) {
			JOptionPane.showMessageDialog(contentPane, "Troppi utenti connessi");
		}
	}

}