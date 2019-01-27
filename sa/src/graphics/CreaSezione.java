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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Section;
import notifier.Notifier;
import session.Session;

public class CreaSezione extends MyFrame {

	private JPanel contentPane;
	private CreaSezione thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaSezione(ModificaCorso mc, Session ses, Course cor) {
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
				mc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
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
						pubblica, Integer.parseInt(codSezione.getText()), ses.getUser().getInfo().student_number, 
						Integer.parseInt(codCorso.getText()), figlio);
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						gc.createSection(s);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione al databse");
						e1.printStackTrace();
					}
					try {
						Notifier n = new Notifier();
						n.sendMail(ses.info().email, "pwd?", cor.name,
								"Aggiornamento contenuti corso "+cor.name, "Aggiunta sezione "+titolo.getText());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(contentPane, "Errore durante l'invio dell'email");
						e2.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e3) {
					JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
					e3.printStackTrace();
				}
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
