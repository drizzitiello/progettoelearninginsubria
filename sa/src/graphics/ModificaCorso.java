package graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Content;
import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;
import courseContentManagement.Section;
import courseManagement.CourseManagement;
import session.Session;
import user.User;

public class ModificaCorso extends MyFrame {

	private JPanel contentPane;

	public ModificaCorso(Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
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
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modifica);
		
		FindCourse rc = new FindCourse();
		Content c=null;
			try {
				c = rc.getContenutCourse(cor);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		for(Section s : c.sections) {
			
			JTextField sezione = new JTextField(s.title);
			contentPane.add(sezione);
			nomeCorso.setColumns(10);
			JButton modificaSezione = new JButton("Modifica sezione");
			modificaSezione.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModificaSezione ms = new ModificaSezione(ses, cor, s.sectionCode);
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
						System.out.println(r);
						System.out.println(r.resourceCode);
						System.out.println(r.resourceCode);
						ModificaRisorsa ms = new ModificaRisorsa(r.resourceCode);
					}
				});
				contentPane.add(modificaRisorsa);
			}
		}
		
		JButton creaSezione = new JButton("Crea sezione");
		creaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaSezione ms = new CreaSezione(ses, cor);
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
