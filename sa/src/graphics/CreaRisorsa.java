package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Resource;
import notifier.Notifier;
import session.Session;

public class CreaRisorsa extends MyFrame {

	private JPanel contentPane;
	private CreaRisorsa thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaRisorsa(ModificaSezione ms, Session ses, Course cor) {
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
				ms.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
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
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						Notifier n = new Notifier();
						n.sendMail(ses.info().email, "pwd?", cor.name, 
								"Aggiornamento contenuti corso "+cor.name, "Aggiunta risorsa "+nome.getText());
						try {
							gc.createResource(r);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e3) {
					e3.printStackTrace();
				}
			}
		});
		contentPane.add(creaRisorsa);
		
		setVisible(true);
	}

}
