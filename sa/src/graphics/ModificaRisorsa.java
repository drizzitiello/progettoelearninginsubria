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

import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;

public class ModificaRisorsa extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param i 
	 */
	public ModificaRisorsa(int codRisorsa) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Risorse r = null;
		GestioneContenutoCorso gc = new GestioneContenutoCorso();
		try {
			r=gc.getRisorsa(codRisorsa);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
		JTextField codiceRisorsa = new JTextField(String.valueOf(r.codRisorsa));
		contentPane.add(codiceRisorsa);
		codiceRisorsa.setColumns(10);
		
		JTextField nome = new JTextField(r.nome);
		contentPane.add(nome);
		nome.setColumns(10);
		
		JTextField descrizione = new JTextField(r.descrizione);
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JTextField path = new JTextField(r.path);
		contentPane.add(path);
		path.setColumns(10);
		
		JTextField codSezione = new JTextField(String.valueOf(r.codSezione));
		contentPane.add(codSezione);
		codSezione.setColumns(10);
		
		JTextField visibilita = new JTextField(String.valueOf(r.visibilita));
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JTextField tipo = new JTextField(r.tipo);
		contentPane.add(tipo);
		tipo.setColumns(10);
		
		JButton modificaRisorsa = new JButton("Modifica risorsa");
		modificaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
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
					Risorse risorsa = new Risorse(nome.getText(),  descrizione.getText(), path.getText(), 
							Integer.parseInt(codSezione.getText()), Integer.parseInt(codiceRisorsa.getText()), 
							pubblica, tipoUtente);
					gc.modificaRisorsa(risorsa);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(modificaRisorsa);
		
		JButton cancellaQuestaRisorsa = new JButton("Cancella questa risorsa");
		cancellaQuestaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestioneContenutoCorso gc = new GestioneContenutoCorso();
				try {
					gc.cancellaRisorsa(codRisorsa);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				//torna indietro
			}
		});
		contentPane.add(cancellaQuestaRisorsa);
		
		setVisible(true);
	}

}
