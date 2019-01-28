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

import courseContentManagement.Content;
import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;
import courseContentManagement.Section;
import courseManagement.CourseManagement;
import session.Session;

public class ModifyCourse extends MyFrame {

	private JPanel contentPane;
	private ModifyCourse thisframe;

	public ModifyCourse(CoursePage pc, Session ses, Course cor) {
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
				pc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel name = new JLabel("Nome: ");
		contentPane.add(name);
		
		JTextField courseName = new JTextField(cor.name);
		contentPane.add(courseName);
		courseName.setColumns(10);
		
		JLabel year = new JLabel("Anno: ");
		contentPane.add(year);
		
		JTextField yearr = new JTextField(String.valueOf(cor.activation_year));
		contentPane.add(yearr);
		yearr.setColumns(10);
		
		JLabel courseCode = new JLabel("Codice corso: "+String.valueOf(cor.courseCode));
		contentPane.add(courseCode);
		
		JLabel creator = new JLabel("Creatore: "+String.valueOf(cor.creator));
		contentPane.add(creator);
		
		JLabel description = new JLabel("Descrizione: "+cor.description);
		if(ses.getUser().getInfo().userType==3) {
			description.setText("Descrizione: ");
		}
		contentPane.add(description);
		
		JTextField descr = new JTextField(cor.description);
		descr.setColumns(10);
		if(ses.getUser().getInfo().userType==3) {
			contentPane.add(descr);
		}
		
		JLabel faculty = new JLabel("Facolta: ");
		contentPane.add(faculty);
		
		JTextField faclty = new JTextField(cor.faculty);
		contentPane.add(faclty);
		faclty.setColumns(10);
		
		JLabel cfuWeight = new JLabel("CFU: ");
		contentPane.add(cfuWeight);
		
		JTextField weight = new JTextField(String.valueOf(cor.weight));
		contentPane.add(weight);
		weight.setColumns(10);
		
		JButton modify = new JButton("Modifica");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setYear(Integer.parseInt(yearr.getText()));
				if(ses.getUser().getInfo().userType==3) {
					cor.setDescription(descr.getText());
				}
				else {
					cor.setDescription(description.getText());
				}
				cor.setFaculty(faclty.getText());
				cor.setName(courseName.getText());
				cor.setWeight(Integer.parseInt(weight.getText()));
				CourseManagement gc;
				try {
					gc = new CourseManagement();
					gc.modifyCourse(cor);
				} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modify);
		
		FindCourse rc;
		try {
			rc = new FindCourse();
			Content c=null;
			try {
				c = rc.getContentCourse(cor);
				
				for(Section s : c.sections) {
					
					JTextField section = new JTextField(s.title);
					contentPane.add(section);
					courseName.setColumns(10);
					JButton modifySection = new JButton("Modifica sezione");
					modifySection.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ModifySection ms = new ModifySection(thisframe, ses, cor, s.sectionCode);
						}
					});
					contentPane.add(modifySection);
					for(Resource r : s.resources) {
						JTextField resource = new JTextField(r.name);
						contentPane.add(resource);
						courseName.setColumns(10);
						JButton modifyResource = new JButton("Modifica risorsa");
						modifyResource.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								ModifyResource ms = new ModifyResource(thisframe, r.resourceCode);
							}
						});
						contentPane.add(modifyResource);
					}
				}
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e1.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton createSection = new JButton("Crea sezione");
		createSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateSection ms = new CreateSection(thisframe, ses, cor);
			}
		});
		contentPane.add(createSection);
		
		setVisible(true);
	}
	
	public ModifyCourse(ManageCourses gc, Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisframe=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel name = new JLabel("Nome: ");
		contentPane.add(name);
		
		JTextField courseName = new JTextField(cor.name);
		contentPane.add(courseName);
		courseName.setColumns(10);
		
		JLabel year = new JLabel("Anno: ");
		contentPane.add(year);
		
		JTextField yearr = new JTextField(String.valueOf(cor.activation_year));
		contentPane.add(yearr);
		yearr.setColumns(10);
		
		JLabel courseCode = new JLabel("Codice corso: "+String.valueOf(cor.courseCode));
		contentPane.add(courseCode);
		
		JLabel creator = new JLabel("Creatore: "+String.valueOf(cor.creator));
		contentPane.add(creator);
		
		JLabel description = new JLabel("Descrizione: "+cor.description);
		if(ses.getUser().getInfo().userType==3) {
			description.setText("Descrizione: ");
		}
		contentPane.add(description);
		
		JTextField descr = new JTextField(cor.description);
		descr.setColumns(10);
		if(ses.getUser().getInfo().userType==3) {
			contentPane.add(descr);
		}
		
		JLabel faculty = new JLabel("Facolta: ");
		contentPane.add(faculty);
		
		JTextField fculty = new JTextField(cor.faculty);
		contentPane.add(fculty);
		fculty.setColumns(10);
		
		JLabel cfuWeight = new JLabel("CFU: ");
		contentPane.add(cfuWeight);
		
		JTextField weight = new JTextField(String.valueOf(cor.weight));
		contentPane.add(weight);
		weight.setColumns(10);
		
		JButton modify = new JButton("Modifica");
		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setYear(Integer.parseInt(yearr.getText()));
				if(ses.getUser().getInfo().userType==3) {
					cor.setDescription(descr.getText());
				}
				else {
					cor.setDescription(description.getText());
				}
				cor.setFaculty(fculty.getText());
				cor.setName(courseName.getText());
				cor.setWeight(Integer.parseInt(weight.getText()));
				CourseManagement gc;
				try {
					gc = new CourseManagement();
					gc.modifyCourse(cor);
				} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modify);
		
		FindCourse rc;
		try {
			rc = new FindCourse();
			Content c=null;
			try {
				c = rc.getContentCourse(cor);
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e1.printStackTrace();
			}
		for(Section s : c.sections) {
			
			JTextField section = new JTextField(s.title);
			contentPane.add(section);
			courseName.setColumns(10);
			JButton modifySection = new JButton("Modifica sezione");
			modifySection.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModifySection ms = new ModifySection(thisframe, ses, cor, s.sectionCode);
				}
			});
			contentPane.add(modifySection);
			for(Resource r : s.resources) {
				JTextField resource = new JTextField(r.name);
				contentPane.add(resource);
				courseName.setColumns(10);
				JButton modifyResource = new JButton("Modifica risorsa");
				modifyResource.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModifyResource ms = new ModifyResource(thisframe, r.resourceCode);
					}
				});
				contentPane.add(modifyResource);
			}
		}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton createSection = new JButton("Crea sezione");
		createSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateSection ms = new CreateSection(thisframe, ses, cor);
			}
		});
		contentPane.add(createSection);
		
		setVisible(true);
	}

}
