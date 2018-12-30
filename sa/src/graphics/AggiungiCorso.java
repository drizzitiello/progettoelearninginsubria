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
import gestioneCorsi.GestioneCorsi;
import socketDb.SocketDb;

public class AggiungiCorso extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public AggiungiCorso(Sessione ses, String pwd) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel codice = new JLabel("Codice corso: ");
		contentPane.add(codice);
		JTextField codiceCorso = new JTextField();
		contentPane.add(codiceCorso);
		codiceCorso.setColumns(10);
		
		JLabel nome = new JLabel("Nome: ");
		contentPane.add(nome);
		JTextField insNome = new JTextField();
		contentPane.add(insNome);
		insNome.setColumns(10);
		
		JLabel annoAttivazione = new JLabel("Anno di attivazione: ");
		contentPane.add(annoAttivazione);
		JTextField annoAttiv = new JTextField();
		contentPane.add(annoAttiv);
		annoAttiv.setColumns(10);
		
		JLabel facolta = new JLabel("Facolta di riferimento: ");
		contentPane.add(facolta);
		JTextField fac = new JTextField();
		contentPane.add(fac);
		fac.setColumns(10);
		
		JLabel descrizione = new JLabel("Descrizione");
		contentPane.add(descrizione);
		JTextField desc = new JTextField();
		contentPane.add(desc);
		desc.setColumns(10);
		
		JLabel peso = new JLabel("CFU: ");
		contentPane.add(peso);
		JTextField pes = new JTextField();
		contentPane.add(pes);
		pes.setColumns(10);
		
		JButton aggiungiCorso = new JButton("AggiungiCorso");
		aggiungiCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GestioneCorsi gc = new GestioneCorsi();
					Corso corso = new Corso();
					corso.setCodCorso(Integer.parseInt(codiceCorso.getText()));
	                corso.setNome(insNome.getText());
	                corso.setAnno(Integer.parseInt(annoAttiv.getText()));
	                corso.setLaurea(fac.getText());
	                corso.setDescrizione(desc.getText());
	                corso.setPeso(Integer.parseInt(pes.getText()));
	                corso.setCreatore(ses.getUtente().getInfo().matricola);
					gc.creazioneCorso(corso);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(aggiungiCorso);
		setVisible(true);
	}

}
