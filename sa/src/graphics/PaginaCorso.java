
package graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

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
	 * @param corso 
	 * @throws ClassNotFoundException 
	 */
	public PaginaCorso(Session ses, String corso, boolean visualComeStudente) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ac=new ArrayList<Component>();
		
		thisFrame=this;
		
		user=ses.getUser();
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(thisFrame, 
		            "Are you sure you want to close this window?", "Close Window?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	try {
						gc.deleteSession(user, cor);
						ses.destroy();
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
		            System.exit(0);
		        }
		    }
		});
		
		
		try {
			gc = new CourseManagement();
			gc.createSession(ses.getUser(), Notifier.getCourse(corso));
			try {
				cor= Notifier.getCourse(corso);
				FindCourse rc = new FindCourse();
				c = rc.getContenutCourse(cor);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
		JLabel titoloCorso = new JLabel(cor.name);
		contentPane.add(titoloCorso);
		
		JLabel descrizioneCorso = new JLabel(cor.description);
		contentPane.add(descrizioneCorso);
		
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
			if(gc.studenteEnrolledInTheCourse(ses.getUser(), cor)||visualComeStudente) {
				for(Section s : c.sections) {
					if(s.visibility) {
					JLabel sezione = new JLabel(s.title);
					contentPane.add(sezione);
					JButton accessoRisorse = new JButton("Cerca");
					accessoRisorse.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean set=true;
							for(Component c : ac) {
								Container parent = c.getParent();
								System.out.println(c);
								parent.remove(c);
								contentPane.revalidate();
								validate();
								repaint();
								accessoRisorse.add(c);
								c.setVisible(false);
								set=false;
							}
							if(!set) {ac.clear();}
							if(set) {
								for(Component c : accessoRisorse.getComponents()) {
									contentPane.add(c);
									c.setVisible(true);
									ac.add(c);
									System.out.println("agg");
								}
							}
						}
					});
					contentPane.add(accessoRisorse);
					for(Resource r : s.resources) {
						if(r.visibility) {
						JLabel descrizioneRisorsa = new JLabel(r.description);
						descrizioneRisorsa.setVisible(false);
						accessoRisorse.add(descrizioneRisorsa);
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
						risorsa.setVisible(false);
						accessoRisorse.add(risorsa);
					}
					}
					}
				}
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
				if(docenteCorso||gc.studenteEnrolledInTheCourse(ses.getUser(), cor)) {//se docente iscritto. funziona ma mettere a posto
				for(Section s : c.sections) {
					JLabel sezione = new JLabel(s.title);
					contentPane.add(sezione);
					JButton accessoRisorse = new JButton("Cerca");
					accessoRisorse.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							boolean set=true;
							for(Component c : ac) {
								Container parent = c.getParent();
								System.out.println(c);
								parent.remove(c);
								contentPane.revalidate();
								validate();
								repaint();
								accessoRisorse.add(c);
								c.setVisible(false);
								set=false;
							}
							if(!set) {ac.clear();}
							if(set) {
								for(Component c : accessoRisorse.getComponents()) {
									contentPane.add(c);
									c.setVisible(true);
									ac.add(c);
									System.out.println("agg");
								}
							}
						}
					});
					contentPane.add(accessoRisorse);
					for(Resource r : s.resources) {
						JLabel descrizioneRisorsa = new JLabel(r.description);
						descrizioneRisorsa.setVisible(false);
						accessoRisorse.add(descrizioneRisorsa);
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
						risorsa.setVisible(false);
						accessoRisorse.add(risorsa);
					}
				}
				if(docenteCorso) {
				JButton analisiCorso = new JButton("Analisi statistiche corso");
				analisiCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						StatisticheCorso mc = new StatisticheCorso(ses, cor);
					}
				});
				contentPane.add(analisiCorso);
				JButton modificaCorso = new JButton("Modifica corso");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaCorso mc = new ModificaCorso(ses, cor);
					}
				});
				contentPane.add(modificaCorso);
				JButton visualizzaComeStudente = new JButton("Visualizza corso come studente");
				visualizzaComeStudente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CourseContentManagement.viewAsStudent(cor.name);
					}
				});
				contentPane.add(visualizzaComeStudente);
				}
				}
			}
			//visualComeStudente&&
			else if(ses.info().userType==3) {
					for(Section s : c.sections) {
						JLabel sezione = new JLabel(s.title);
						contentPane.add(sezione);
						JButton accessoRisorse = new JButton("Cerca");
						accessoRisorse.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								boolean set=true;
								for(Component c : ac) {
									Container parent = c.getParent();
									System.out.println(c);
									parent.remove(c);
									contentPane.revalidate();
									validate();
									repaint();
									accessoRisorse.add(c);
									c.setVisible(false);
									set=false;
								}
								if(!set) {ac.clear();}
								if(set) {
									for(Component c : accessoRisorse.getComponents()) {
										contentPane.add(c);
										c.setVisible(true);
										ac.add(c);
										System.out.println("agg");
									}
								}
							}
						});
						contentPane.add(accessoRisorse);
						for(Resource r : s.resources) {
							JLabel descrizioneRisorsa = new JLabel(r.description);
							descrizioneRisorsa.setVisible(false);
							accessoRisorse.add(descrizioneRisorsa);
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
							risorsa.setVisible(false);
							accessoRisorse.add(risorsa);
						}
					}
					JButton analisiCorso = new JButton("Analisi statistiche corso");
					analisiCorso.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							StatisticheCorso mc = new StatisticheCorso(ses, cor);
						}
					});
					contentPane.add(analisiCorso);
					JButton modificaCorso = new JButton("Modifica corso");
					modificaCorso.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ModificaCorso mc = new ModificaCorso(ses, cor);
						}
					});
					contentPane.add(modificaCorso);
					
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
	
	 protected void finalize() {   
		    try {
		    	gc.deleteSession(user, cor);
				super.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

}
