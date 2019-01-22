package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.ReperisciCorso;

public class GestisciCorsi extends MyFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public GestisciCorsi(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ReperisciCorso rc = new ReperisciCorso();
		try {
			ArrayList<Corso> ali = rc.getCorsi();
			for(Corso c : ali) {
				JLabel codiceCorso = new JLabel("Codice corso: "+((Integer) c.codCorso).toString());
				contentPane.add(codiceCorso);
				JLabel nome = new JLabel("Nome: "+c.nome);
				contentPane.add(nome);
				JLabel annoAttivazione = new JLabel("Anno attivazione: "+String.valueOf(c.anno_attivazione));
				contentPane.add(annoAttivazione);
				JLabel facolta = new JLabel("Facolta: "+c.facolta);
				contentPane.add(facolta);
				JLabel descrizione = new JLabel("Descrizione: "+c.descrizione);
				contentPane.add(descrizione);
				JLabel peso = new JLabel("CFU: "+((Integer) c.peso).toString());
				contentPane.add(peso);
				JButton modificaCorso = new JButton("Modifica Corso");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaCorso ac = new ModificaCorso(ses, c);
					}
				});
				contentPane.add(modificaCorso);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		JButton aggiungiCorso = new JButton("Aggiungi corso");
		aggiungiCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AggiungiCorso ac = new AggiungiCorso(ses, pwd);
			}
		});
		contentPane.add(aggiungiCorso);
		setVisible(true);
	}

}
