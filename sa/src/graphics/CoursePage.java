
package graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Content;
import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;
import courseContentManagement.Section;
import courseManagement.CourseManagement;
import notifier.Notifier;
import session.Session;
import user.User;

public class CoursePage extends JFrame {

	private JPanel contentPane;
	private User user;
	private Course cor;
	private Content c;
	private ArrayList<Component> ac;
	private CourseManagement gc;
	private CoursePage thisFrame;

	/**
	 * Create the frame.
	 * @param hp 
	 * @param course 
	 * @throws ClassNotFoundException 
	 */
	public CoursePage(HomePage hp, Session ses, String course, boolean visualAsStudent) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ac=new ArrayList<Component>();
		
		thisFrame=this;
		
		user=ses.getUser();
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hp.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		       try {
					gc.deleteSession(user, cor);
					ses.destroy();
				} catch (ClassNotFoundException | SQLException | RemoteException e) {
					JOptionPane.showMessageDialog(contentPane,"Errore durante la distruzione della sessione");
					e.printStackTrace();
				}
		        System.exit(0);
		    }
		});
		
		
		try {
			gc = new CourseManagement();
			Notifier n = new Notifier();
			gc.createSession(ses.getUser(), n.getCourse(course));
			try {
				cor= n.getCourse(course);
				FindCourse rc = new FindCourse();
				c = rc.getContentCourse(cor);
			} catch (ClassNotFoundException | SQLException e) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e.printStackTrace();
			}
			
		JLabel courseTitle = new JLabel(cor.name);
		contentPane.add(courseTitle);
		
		Box de = Box.createHorizontalBox();
		
		JLabel courseDescription = new JLabel(cor.description);
		de.add(courseDescription);
		contentPane.add(de);;
		
		ArrayList<User> u=new ArrayList<User>();
		try {
			u=gc.whoTeachCourse(cor);
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
			e2.printStackTrace();
		}
		
		String professorsList="Elenco docenti corso : ";
		for(User ut : u) {
			professorsList=professorsList+ut.getInfo().name+" "+ut.getInfo().surname+" ";
		}
		JLabel courseProfessors = new JLabel(professorsList);
		contentPane.add(courseProfessors);
		
		
		try {
			if(ses.info().userType==1||visualAsStudent) {
			if(gc.studentEnrolledInTheCourse(ses.getUser(), cor)||visualAsStudent) {
				Box all = Box.createVerticalBox();
				for(Section s : c.sections) {
					Box se = Box.createHorizontalBox();
					JLabel section = new JLabel(s.title);
					se.add(section);
					se.add(Box.createRigidArea(new Dimension(5,0)));
					for(Resource r : s.resources) {
						JLabel resourceDescription = new JLabel(r.description);
						se.add(resourceDescription);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						if(r.type.equals("cartella")) {
							JButton resource = new JButton(r.name);
							resource.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									thisFrame.setVisible(false);
									FolderPage pc = new FolderPage(r, thisFrame);
								}
							});
							se.add(resource);
						}
						else {
							JButton resource = new JButton(r.name);
							resource.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										FindCourse rc = new FindCourse();
										rc.download(r);
									} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
										e1.printStackTrace();
									}
								}
							});
							se.add(resource);
							se.add(Box.createRigidArea(new Dimension(5,0)));
						}
					}
					all.add(se);
			}
			contentPane.add(all);
			}
			else {
				JLabel notSigned = new JLabel("Non sei iscritto al corso");
				contentPane.add(notSigned);
				
				JButton signIn = new JButton("Iscriviti al corso");
				signIn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							CourseManagement gc = new CourseManagement();
							gc.signUpForCourse(ses.getUser(), cor);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(signIn);
			}
			}
			else if(!visualAsStudent&&ses.info().userType==2) {
				boolean courseProfessor=false;
				for(User utente : gc.whoTeachCourse(cor)) {
					if(utente.getInfo().student_number==ses.info().student_number) {
						courseProfessor=true;
						break;
					}
				}
				if(courseProfessor||gc.studentEnrolledInTheCourse(ses.getUser(), cor)) {//se docente iscritto. funziona ma mettere a posto
					Box all = Box.createVerticalBox();
					for(Section s : c.sections) {
						Box se = Box.createHorizontalBox();
						JLabel section = new JLabel(s.title);
						se.add(section);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						for(Resource r : s.resources) {
							JLabel resourceDescription = new JLabel(r.description);
							se.add(resourceDescription);
							se.add(Box.createRigidArea(new Dimension(5,0)));
							if(r.type.equals("cartella")) {
								JButton resource = new JButton(r.name);
								resource.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										thisFrame.setVisible(false);
										FolderPage pc = new FolderPage(r, thisFrame);
									}
								});
								se.add(resource);
							}
							else {
								JButton resource = new JButton(r.name);
								resource.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										try {
											FindCourse rc = new FindCourse();
											rc.download(r);
										} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
											JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
											e1.printStackTrace();
										}
									}
								});
								se.add(resource);
								se.add(Box.createRigidArea(new Dimension(5,0)));
							}
						}
						all.add(se);
				}
				contentPane.add(all);
				if(courseProfessor) {
				JButton courseAnalysis = new JButton("Analisi statistiche corso");
				courseAnalysis.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisFrame.setVisible(false);
						CourseStatistics mc = new CourseStatistics(thisFrame, ses, cor);
					}
				});
				contentPane.add(courseAnalysis);
				JButton modifyCourse = new JButton("Modifica corso");
				modifyCourse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisFrame.setVisible(false);
						ModifyCourse mc = new ModifyCourse(thisFrame, ses, cor);
					}
				});
				contentPane.add(modifyCourse);
				JButton visualizeAsStudent = new JButton("Visualizza corso come studente");
				visualizeAsStudent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							CourseContentManagement.viewAsStudent(hp, cor.name);
						} catch (MalformedURLException | RemoteException | NotBoundException e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(visualizeAsStudent);
				}
				}
			}
			else if(ses.info().userType==3) {
				Box all = Box.createVerticalBox();
				for(Section s : c.sections) {
					Box se = Box.createHorizontalBox();
					JLabel section = new JLabel(s.title);
					se.add(section);
					se.add(Box.createRigidArea(new Dimension(5,0)));
					for(Resource r : s.resources) {
						JLabel respurceDescription = new JLabel(r.description);
						se.add(respurceDescription);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						if(r.type.equals("cartella")) {
							JButton resource = new JButton(r.name);
							resource.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									thisFrame.setVisible(false);
									FolderPage pc = new FolderPage(r, thisFrame);
								}
							});
							se.add(resource);
						}
						else {
							JButton resource = new JButton(r.name);
							resource.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										FindCourse rc = new FindCourse();
										rc.download(r);
									} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
										JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
										e1.printStackTrace();
									}
								}
							});
							se.add(resource);
							se.add(Box.createRigidArea(new Dimension(5,0)));
						}
					}
					all.add(se);
			}
				contentPane.add(all);
					
					Box anMod = Box.createHorizontalBox();
					
					JButton courseAnalysis = new JButton("Analisi statistiche corso");
					courseAnalysis.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							CourseStatistics mc = new CourseStatistics(thisFrame, ses, cor);
						}
					});
					anMod.add(courseAnalysis);
					JButton modifyCourse = new JButton("Modifica corso");
					modifyCourse.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							ModifyCourse mc = new ModifyCourse(thisFrame, ses, cor);
						}
					});
					anMod.add(modifyCourse);
					contentPane.add(anMod);
					
			}
		} catch (SQLException | ClassNotFoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
			e2.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane,"Errore");
			e.printStackTrace();
		}
		} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e3) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e3.printStackTrace();
		}
		
		setVisible(true);
	}
}
