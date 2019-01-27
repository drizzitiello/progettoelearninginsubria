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

public class ModificaCorso extends MyFrame {

	private JPanel contentPane;
	private ModificaCorso thisframe;

	public ModificaCorso(PaginaCorso pc, Session ses, Course cor) {
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
		
		JLabel nome = new JLabel("Nome: ");
		contentPane.add(nome);
		
		JTextField nomeCorso = new JTextField(cor.name);
		contentPane.add(nomeCorso);
		nomeCorso.setColumns(10);
		
		JLabel anno = new JLabel("Anno: ");
		contentPane.add(anno);
		
		JTextField annoo = new JTextField(String.valueOf(cor.activation_year));
		contentPane.add(annoo);
		annoo.setColumns(10);
		
		JLabel codCorso = new JLabel("Codice corso: "+String.valueOf(cor.courseCode));
		contentPane.add(codCorso);
		
		JLabel creatore = new JLabel("Creatore: "+String.valueOf(cor.creator));
		contentPane.add(creatore);
		
		JLabel descrizione = new JLabel("Descrizione: "+cor.description);
		if(ses.getUser().getInfo().userType==3) {
			descrizione.setText("Descrizione: ");
		}
		contentPane.add(descrizione);
		
		JTextField descr = new JTextField(cor.description);
		descr.setColumns(10);
		if(ses.getUser().getInfo().userType==3) {
			contentPane.add(descr);
		}
		
		JLabel laurea = new JLabel("Facolta: ");
		contentPane.add(laurea);
		
		JTextField facolta = new JTextField(cor.faculty);
		contentPane.add(facolta);
		facolta.setColumns(10);
		
		JLabel pesoCFU = new JLabel("CFU: ");
		contentPane.add(pesoCFU);
		
		JTextField peso = new JTextField(String.valueOf(cor.weight));
		contentPane.add(peso);
		peso.setColumns(10);
		
		JButton modifica = new JButton("Modifica");
		modifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setYear(Integer.parseInt(annoo.getText()));
				if(ses.getUser().getInfo().userType==3) {
					cor.setDescription(descr.getText());
				}
				else {
					cor.setDescription(descrizione.getText());
				}
				cor.setFaculty(facolta.getText());
				cor.setName(nomeCorso.getText());
				cor.setWeight(Integer.parseInt(peso.getText()));
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
		contentPane.add(modifica);
		
		FindCourse rc;
		try {
			rc = new FindCourse();
			Content c=null;
			try {
				c = rc.getContentCourse(cor);
				
				for(Section s : c.sections) {
					
					JTextField sezione = new JTextField(s.title);
					contentPane.add(sezione);
					nomeCorso.setColumns(10);
					JButton modificaSezione = new JButton("Modifica sezione");
					modificaSezione.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ModificaSezione ms = new ModificaSezione(thisframe, ses, cor, s.sectionCode);
						}
					});
					contentPane.add(modificaSezione);
					for(Resource r : s.resources) {
						JTextField risorsa = new JTextField(r.name);
						contentPane.add(risorsa);
						nomeCorso.setColumns(10);
						JButton modificaRisorsa = new JButton("Modifica risorsa");
						modificaRisorsa.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								ModificaRisorsa ms = new ModificaRisorsa(thisframe, r.resourceCode);
							}
						});
						contentPane.add(modificaRisorsa);
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
		
		JButton creaSezione = new JButton("Crea sezione");
		creaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaSezione ms = new CreaSezione(thisframe, ses, cor);
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}
	
	public ModificaCorso(GestisciCorsi gc, Session ses, Course cor) {
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
		
		JLabel nome = new JLabel("Nome: ");
		contentPane.add(nome);
		
		JTextField nomeCorso = new JTextField(cor.name);
		contentPane.add(nomeCorso);
		nomeCorso.setColumns(10);
		
		JLabel anno = new JLabel("Anno: ");
		contentPane.add(anno);
		
		JTextField annoo = new JTextField(String.valueOf(cor.activation_year));
		contentPane.add(annoo);
		annoo.setColumns(10);
		
		JLabel codCorso = new JLabel("Codice corso: "+String.valueOf(cor.courseCode));
		contentPane.add(codCorso);
		
		JLabel creatore = new JLabel("Creatore: "+String.valueOf(cor.creator));
		contentPane.add(creatore);
		
		JLabel descrizione = new JLabel("Descrizione: "+cor.description);
		if(ses.getUser().getInfo().userType==3) {
			descrizione.setText("Descrizione: ");
		}
		contentPane.add(descrizione);
		
		JTextField descr = new JTextField(cor.description);
		descr.setColumns(10);
		if(ses.getUser().getInfo().userType==3) {
			contentPane.add(descr);
		}
		
		JLabel laurea = new JLabel("Facolta: ");
		contentPane.add(laurea);
		
		JTextField facolta = new JTextField(cor.faculty);
		contentPane.add(facolta);
		facolta.setColumns(10);
		
		JLabel pesoCFU = new JLabel("CFU: ");
		contentPane.add(pesoCFU);
		
		JTextField peso = new JTextField(String.valueOf(cor.weight));
		contentPane.add(peso);
		peso.setColumns(10);
		
		JButton modifica = new JButton("Modifica");
		modifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setYear(Integer.parseInt(annoo.getText()));
				if(ses.getUser().getInfo().userType==3) {
					cor.setDescription(descr.getText());
				}
				else {
					cor.setDescription(descrizione.getText());
				}
				cor.setFaculty(facolta.getText());
				cor.setName(nomeCorso.getText());
				cor.setWeight(Integer.parseInt(peso.getText()));
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
		contentPane.add(modifica);
		
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
			
			JTextField sezione = new JTextField(s.title);
			contentPane.add(sezione);
			nomeCorso.setColumns(10);
			JButton modificaSezione = new JButton("Modifica sezione");
			modificaSezione.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModificaSezione ms = new ModificaSezione(thisframe, ses, cor, s.sectionCode);
				}
			});
			contentPane.add(modificaSezione);
			for(Resource r : s.resources) {
				JTextField risorsa = new JTextField(r.name);
				contentPane.add(risorsa);
				nomeCorso.setColumns(10);
				JButton modificaRisorsa = new JButton("Modifica risorsa");
				modificaRisorsa.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaRisorsa ms = new ModificaRisorsa(thisframe, r.resourceCode);
					}
				});
				contentPane.add(modificaRisorsa);
			}
		}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton creaSezione = new JButton("Crea sezione");
		creaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaSezione ms = new CreaSezione(thisframe, ses, cor);
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
