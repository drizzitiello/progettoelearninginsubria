package graphics;

import java.awt.BorderLayout;
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

import Sessione.Sessione;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.Sezione;

public class ModificaSezione extends MyFrame {

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
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Sezione s = null;
		GestioneContenutoCorso gc = new GestioneContenutoCorso();
		try {
			s=gc.getSezione(codSezione);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		final int matricola=s.matricola;
		final int codCorso=s.codCorso;
		final Integer figlioDi=s.figlioDi;
		
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);
		
		JTextField descrizione = new JTextField(s.descrizione);
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JLabel tit = new JLabel("Titolo: ");
		contentPane.add(tit);
		
		JTextField titolo = new JTextField(s.titolo);
		contentPane.add(titolo);
		titolo.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);
		
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
							pubblica, codSezione, matricola, codCorso, figlioDi);
					gc.modificaSezione(sezione);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modificaSezione);
		
		JButton creaRisorsa = new JButton("Crea risorsa");
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
