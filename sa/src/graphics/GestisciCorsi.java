package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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

public class GestisciCorsi extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public GestisciCorsi(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		ReperisciCorso rc = new ReperisciCorso();
		try {
			//cambiare getCorsi e testo JLabel
			ArrayList<Corso> ali = rc.getCorsi();
			for(Corso c : ali) {
				JLabel codiceCorso = new JLabel(((Integer) c.codCorso).toString());
				contentPane.add(codiceCorso);
				JLabel nome = new JLabel(c.nome);
				contentPane.add(nome);
				JLabel annoAttivazione = new JLabel(c.anno);
				contentPane.add(annoAttivazione);
				JLabel facolta = new JLabel(c.laurea);
				contentPane.add(facolta);
				JLabel descrizione = new JLabel(c.descrizione);
				contentPane.add(descrizione);
				JLabel peso = new JLabel(((Integer) c.peso).toString());
				contentPane.add(peso);
				JButton modificaCorso = new JButton("Cerca");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaCorso ac = new ModificaCorso(ses);
					}
				});
				contentPane.add(modificaCorso);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		JButton aggiungiCorso = new JButton("Cerca");
		aggiungiCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AggiungiCorso ac = new AggiungiCorso(ses, pwd);
			}
		});
		contentPane.add(aggiungiCorso);
	}

}
