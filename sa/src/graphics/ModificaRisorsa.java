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

import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;

public class ModificaRisorsa extends MyFrame {

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
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Risorse r = null;
		GestioneContenutoCorso gc = new GestioneContenutoCorso();
		try {
			r=gc.getRisorsa(codRisorsa);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		final int codSezione=r.codSezione;
		
		JLabel no = new JLabel("Nome: ");
		contentPane.add(no);
		
		JTextField nome = new JTextField(r.nome);
		contentPane.add(nome);
		nome.setColumns(10);
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);
		
		JTextField descrizione = new JTextField(r.descrizione);
		contentPane.add(descrizione);
		descrizione.setColumns(10);
		
		JLabel percorso = new JLabel("Percorso: ");
		contentPane.add(percorso);
		
		JTextField path = new JTextField(r.path);
		contentPane.add(path);
		path.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);
		
		JTextField visibilita = new JTextField(String.valueOf(r.visibilita));
		contentPane.add(visibilita);
		visibilita.setColumns(10);
		
		JLabel type = new JLabel("Tipo: ");
		contentPane.add(type);
		
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
					Risorse risorsa = new Risorse(nome.getText(),  descrizione.getText(), path.getText(), 
							codSezione, codRisorsa, 
							pubblica, tipo.getText());
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
