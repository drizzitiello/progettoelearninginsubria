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
import gestioneContenutiCorso.Section;
import notifier.Notifier;

public class CreaSezione extends MyFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaSezione(Session ses, Course cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel tit = new JLabel("Titolo: ");
		contentPane.add(tit);
		
		JTextField titolo = new JTextField();
		contentPane.add(titolo);
		titolo.setColumns(10);
		
		JLabel sez = new JLabel("Codice sezione: ");
		contentPane.add(sez);

		JTextField codSezione = new JTextField();
		contentPane.add(codSezione);
		codSezione.setColumns(10);
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);

		JTextField descrizione = new JTextField();
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);

		JTextField visibilita = new JTextField();
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JLabel creat = new JLabel("Creatore: ");
		contentPane.add(creat);

		JTextField creatore = new JTextField();
		contentPane.add(creatore);
		creatore.setColumns(10);
		
		JLabel codCo = new JLabel("Codice corso: ");
		contentPane.add(codCo);
		
		JTextField codCorso = new JTextField();
		contentPane.add(codCorso);
		codCorso.setColumns(10);

		JLabel fig = new JLabel("Figlio di: ");
		contentPane.add(fig);
		
		JTextField figlioDi = new JTextField();
		contentPane.add(figlioDi);
		figlioDi.setColumns(10);
		
		JButton creaSezione = new JButton("Crea sezione");
		creaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pubblica=false;
				if(visibilita.getText().equals("pubblica")) {
					pubblica=true;
				}
				Integer figlio = null;
				if(!figlioDi.getText().equals("")) {
					figlio=Integer.parseInt(figlioDi.getText());
				}
				Section s = new Section(titolo.getText(),  descrizione.getText(), 
						pubblica, Integer.parseInt(codSezione.getText()), Integer.parseInt(creatore.getText()), 
						Integer.parseInt(codCorso.getText()), figlio);
				CourseContentManagement gc = new CourseContentManagement();
				try {
					Notifier.sendMail(ses.info().email, "pwd?", cor.name,
							"Aggiornamento contenuti corso "+cor.name, "Aggiunta sezione "+titolo.getText());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					gc.createSection(s);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
