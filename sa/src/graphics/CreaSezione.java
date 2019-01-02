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
import gestioneContenutiCorso.Sezione;
import notifier.Notifier;

public class CreaSezione extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreaSezione(Sessione ses, Corso cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextField titolo = new JTextField();
		contentPane.add(titolo);
		titolo.setColumns(10);

		JTextField codSezione = new JTextField();
		contentPane.add(codSezione);
		codSezione.setColumns(10);

		JTextField descrizione = new JTextField();
		contentPane.add(descrizione);
		descrizione.setColumns(10);

		JTextField visibilita = new JTextField();
		contentPane.add(visibilita);
		visibilita.setColumns(10);

		JTextField matricola = new JTextField();
		contentPane.add(matricola);
		matricola.setColumns(10);
		
		JTextField codCorso = new JTextField();
		contentPane.add(codCorso);
		codCorso.setColumns(10);

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
				Sezione s = new Sezione(titolo.getText(),  descrizione.getText(), 
						pubblica, Integer.parseInt(codSezione.getText()), Integer.parseInt(matricola.getText()), 
						Integer.parseInt(codCorso.getText()), Integer.parseInt(figlioDi.getText()));
				GestioneContenutoCorso gc = new GestioneContenutoCorso();
				try {
					Notifier.sendEmail(ses.info().email, "pwd?", cor.nome,
							"Aggiornamento contenuti corso "+cor.nome, "Aggiunta sezione "+titolo.getText());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					gc.createSezione(s);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
