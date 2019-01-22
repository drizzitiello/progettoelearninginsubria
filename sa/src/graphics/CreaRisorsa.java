package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Session;
import gestioneContenutiCorso.Course;
import gestioneContenutiCorso.CourseContentManagement;
import gestioneContenutiCorso.Resource;
import gestioneContenutiCorso.Section;
import notifier.Notifier;

public class CreaRisorsa extends MyFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaRisorsa(Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel no = new JLabel("Nome: ");
		contentPane.add(no);
		
		JTextField nome = new JTextField();
		contentPane.add(nome);
		nome.setColumns(10);
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);

		JTextField descrizione = new JTextField();
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JLabel percorso = new JLabel("Percorso: ");
		contentPane.add(percorso);

		JTextField path = new JTextField();
		contentPane.add(path);
		path.setColumns(10);
		
		JLabel sez = new JLabel("Codice sezione: ");
		contentPane.add(sez);

		JTextField codSezione = new JTextField();
		contentPane.add(codSezione);
		codSezione.setColumns(10);
		
		JLabel ris = new JLabel("Codice risorsa: ");
		contentPane.add(ris);

		JTextField codRisorsa = new JTextField();
		contentPane.add(codRisorsa);
		codRisorsa.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);
		
		JTextField visibilita = new JTextField();
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JLabel type = new JLabel("Tipo: ");
		contentPane.add(type);

		JTextField tipo = new JTextField();
		contentPane.add(tipo);
		tipo.setColumns(10);
		
		JButton creaRisorsa = new JButton("Crea risorsa");
		creaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pubblica=false;
				if(visibilita.getText().equals("pubblica")) {
					pubblica=true;
				}
				Resource r = new Resource(nome.getText(),  descrizione.getText(), path.getText(), 
						Integer.parseInt(codSezione.getText()), Integer.parseInt(codRisorsa.getText()), 
						pubblica, tipo.getText());
				CourseContentManagement gc = new CourseContentManagement();
				try {
					Notifier.sendMail(ses.info().email, "pwd?", cor.name, 
							"Aggiornamento contenuti corso "+cor.name, "Aggiunta risorsa "+nome.getText());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					gc.createResource(r);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(creaRisorsa);
		
		setVisible(true);
	}

}
