package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;
import notifier.Notifier;

public class CreaRisorsa extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaRisorsa(Sessione ses, Corso cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextField nome = new JTextField();
		contentPane.add(nome);
		nome.setColumns(10);

		JTextField descrizione = new JTextField();
		contentPane.add(descrizione);
		descrizione.setColumns(10);

		JTextField path = new JTextField();
		contentPane.add(path);
		path.setColumns(10);

		JTextField codSezione = new JTextField();
		contentPane.add(codSezione);
		codSezione.setColumns(10);

		JTextField codRisorsa = new JTextField();
		contentPane.add(codRisorsa);
		codRisorsa.setColumns(10);
		
		JTextField visibilita = new JTextField();
		contentPane.add(visibilita);
		visibilita.setColumns(10);

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
				int tipoUtente=1;
				switch(tipo.getText()) {
				case "Studente":
					tipoUtente=1;
					break;
				case "Docente":
					tipoUtente=2;
					break;
				case "Amministratore":
					tipoUtente=3;
					break;
				default:
					break;
				}
				Risorse r = new Risorse(nome.getText(),  descrizione.getText(), path.getText(), 
						Integer.parseInt(codSezione.getText()), Integer.parseInt(codRisorsa.getText()), 
						pubblica, tipoUtente);
				GestioneContenutoCorso gc = new GestioneContenutoCorso();
				try {
					Notifier.sendEmail(ses.info().email, "pwd?", cor.nome, 
							"Aggiornamento contenuti corso "+cor.nome, "Aggiunta risorsa "+nome.getText());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					gc.createRisorsa(r);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(creaRisorsa);
		
		setVisible(true);
	}

}
