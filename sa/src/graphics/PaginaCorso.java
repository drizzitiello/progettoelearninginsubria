
package graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import socketDb.SocketDb;
import user.User;

public class PaginaCorso extends JFrame {

	private JPanel contentPane;
	private User user;
	private Course cor;
	private Content c;
	private ArrayList<Component> ac;
	private CourseManagement gc;
	private PaginaCorso thisFrame;

	/**
	 * Create the frame.
	 * @param hp 
	 * @param corso 
	 * @throws ClassNotFoundException 
	 */
	public PaginaCorso(HomePage hp, Session ses, String corso, boolean visualComeStudente) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
		        System.exit(0);
		    }
		});
		
		
		try {
			gc = new CourseManagement();
			gc.createSession(ses.getUser(), Notifier.getCourse(corso));
			try {
				cor= Notifier.getCourse(corso);
				FindCourse rc = new FindCourse();
				c = rc.getContentCourse(cor);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
		JLabel titoloCorso = new JLabel(cor.name);
		contentPane.add(titoloCorso);
		
		Box de = Box.createHorizontalBox();
		
		JLabel descrizioneCorso = new JLabel(cor.description);
		de.add(descrizioneCorso);
		contentPane.add(de);;
		
		ArrayList<User> u=new ArrayList<User>();
		try {
			u=gc.whoTeachCourse(cor);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		String elencoDocenti="Elenco docenti corso : ";
		for(User ut : u) {
			elencoDocenti=elencoDocenti+ut.getInfo().name+" "+ut.getInfo().surname+" ";
		}
		JLabel docentiCorso = new JLabel(elencoDocenti);
		contentPane.add(docentiCorso);
		
		
		try {
			if(ses.info().userType==1||visualComeStudente) {
			if(gc.studentEnrolledInTheCourse(ses.getUser(), cor)||visualComeStudente) {
				Box all = Box.createVerticalBox();
				for(Section s : c.sections) {
					if(s.visibility) {
					Box se = Box.createHorizontalBox();
					JLabel sezione = new JLabel(s.title);
					se.add(sezione);
					se.add(Box.createRigidArea(new Dimension(5,0)));
					for(Resource r : s.resources) {
						if(r.visibility) {
						JLabel descrizioneRisorsa = new JLabel(r.description);
						se.add(descrizioneRisorsa);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						JButton risorsa = new JButton(r.name);
						risorsa.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								FindCourse rc = new FindCourse();
								try {
									rc.download(r);
								} catch (ClassNotFoundException | SQLException e1) {
									e1.printStackTrace();
								}
							}
						});
						se.add(risorsa);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						}
					}
					all.add(se);
					}
			}
			contentPane.add(all);
			}
			else {
				JLabel nonIscritto = new JLabel("Non sei iscritto al corso");
				contentPane.add(nonIscritto);
				
				JButton iscriviti = new JButton("Iscriviti al corso");
				iscriviti.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							CourseManagement gc = new CourseManagement();
							gc.signUpForCourse(ses.getUser(), cor);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(iscriviti);
			}
			}
			else if(!visualComeStudente&&ses.info().userType==2) {
				boolean docenteCorso=false;
				for(User utente : gc.whoTeachCourse(cor)) {
					if(utente.getInfo().student_number==ses.info().student_number) {
						docenteCorso=true;
						break;
					}
				}
				if(docenteCorso||gc.studentEnrolledInTheCourse(ses.getUser(), cor)) {//se docente iscritto. funziona ma mettere a posto
					Box all = Box.createVerticalBox();
					for(Section s : c.sections) {
						Box se = Box.createHorizontalBox();
						JLabel sezione = new JLabel(s.title);
						se.add(sezione);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						for(Resource r : s.resources) {
							JLabel descrizioneRisorsa = new JLabel(r.description);
							se.add(descrizioneRisorsa);
							se.add(Box.createRigidArea(new Dimension(5,0)));
							JButton risorsa = new JButton(r.name);
							risorsa.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									FindCourse rc = new FindCourse();
									try {
										rc.download(r);
									} catch (ClassNotFoundException | SQLException e1) {
										e1.printStackTrace();
									}
								}
							});
							se.add(risorsa);
							se.add(Box.createRigidArea(new Dimension(5,0)));
						}
						all.add(se);
				}
				contentPane.add(all);
				if(docenteCorso) {
				JButton analisiCorso = new JButton("Analisi statistiche corso");
				analisiCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisFrame.setVisible(false);
						StatisticheCorso mc = new StatisticheCorso(thisFrame, ses, cor);
					}
				});
				contentPane.add(analisiCorso);
				JButton modificaCorso = new JButton("Modifica corso");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						thisFrame.setVisible(false);
						ModificaCorso mc = new ModificaCorso(thisFrame, ses, cor);
					}
				});
				contentPane.add(modificaCorso);
				JButton visualizzaComeStudente = new JButton("Visualizza corso come studente");
				visualizzaComeStudente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CourseContentManagement.viewAsStudent(hp, cor.name);
					}
				});
				contentPane.add(visualizzaComeStudente);
				}
				}
			}
			else if(ses.info().userType==3) {
				Box all = Box.createVerticalBox();
				for(Section s : c.sections) {
					Box se = Box.createHorizontalBox();
					JLabel sezione = new JLabel(s.title);
					se.add(sezione);
					se.add(Box.createRigidArea(new Dimension(5,0)));
					for(Resource r : s.resources) {
						JLabel descrizioneRisorsa = new JLabel(r.description);
						se.add(descrizioneRisorsa);
						se.add(Box.createRigidArea(new Dimension(5,0)));
						if(r.type.equals("cartella")) {
							JButton risorsa = new JButton(r.name);
							risorsa.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									thisFrame.setVisible(false);
									PaginaCartella pc = new PaginaCartella(r, thisFrame);
								}
							});
							se.add(risorsa);
						}
						else {
							JButton risorsa = new JButton(r.name);
							risorsa.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									FindCourse rc = new FindCourse();
									try {
										rc.download(r);
									} catch (ClassNotFoundException | SQLException e1) {
										e1.printStackTrace();
									}
								}
							});
							se.add(risorsa);
							se.add(Box.createRigidArea(new Dimension(5,0)));
						}
					}
					all.add(se);
			}
				contentPane.add(all);
					
					Box anMod = Box.createHorizontalBox();
					
					JButton analisiCorso = new JButton("Analisi statistiche corso");
					analisiCorso.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							StatisticheCorso mc = new StatisticheCorso(thisFrame, ses, cor);
						}
					});
					anMod.add(analisiCorso);
					JButton modificaCorso = new JButton("Modifica corso");
					modificaCorso.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							ModificaCorso mc = new ModificaCorso(thisFrame, ses, cor);
						}
					});
					anMod.add(modificaCorso);
					contentPane.add(anMod);
					
			}
		} catch (SQLException | ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		} catch (ClassNotFoundException | SQLException e3) {
			e3.printStackTrace();
		}
		
		setVisible(true);
	}
}
