package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.Sezione;

public class ModificaSezione extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 * @param string 
	 */
	public ModificaSezione(Sessione ses, Corso cor, int codSezione) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Sezione s = null;
		GestioneContenutoCorso gc = new GestioneContenutoCorso();
		try {
			s=gc.getSezione(codSezione);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
		JTextField codSez = new JTextField(String.valueOf(s.codSezione));
		contentPane.add(codSez);
		codSez.setColumns(10);
		
		JTextField descrizione = new JTextField(s.descrizione);
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JTextField matricola = new JTextField(String.valueOf(s.matricola));
		contentPane.add(matricola);
		matricola.setColumns(10);
		
		JTextField titolo = new JTextField(s.titolo);
		contentPane.add(titolo);
		titolo.setColumns(10);
		
		JTextField codCorso = new JTextField(String.valueOf(s.codCorso));
		contentPane.add(codCorso);
		codCorso.setColumns(10);
		
		JTextField figlioDi = new JTextField(String.valueOf(s.figlioDi));
		contentPane.add(figlioDi);
		figlioDi.setColumns(10);
		
		JTextField visibilita = new JTextField(String.valueOf(s.visibilita));
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JButton modificaSezione = new JButton("Modifica sezione");
		modificaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean pubblica=false;
					if(visibilita.getText().equals("pubblica")) {
						pubblica=true;
					}
					Sezione sezione = new Sezione(titolo.getText(),descrizione.getText(),
							pubblica,Integer.parseInt(codSez.getText()), 
							Integer.parseInt(matricola.getText()), Integer.parseInt(codCorso.getText()),
							Integer.parseInt(figlioDi.getText()));
					gc.modificaSezione(sezione);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modificaSezione);
		
		JButton creaRisorsa = new JButton("Crea sezione");
		creaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaRisorsa ms = new CreaRisorsa(ses, cor);
			}
		});
		contentPane.add(creaRisorsa);
		
		JButton cancellaQuestaSezione = new JButton("Cancella questa sezione");
		cancellaQuestaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestioneContenutoCorso gc = new GestioneContenutoCorso();
				try {
					gc.cancellaSezione(codSezione);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				//torna indietro
			}
		});
		contentPane.add(cancellaQuestaSezione);
		
		setVisible(true);
	}

}
