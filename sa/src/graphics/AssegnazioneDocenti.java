package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import Utente.Utente;
import gestioneContenutiCorso.Corso;
import gestioneCorsi.GestioneCorsi;
import notifier.Notifier;

public class AssegnazioneDocenti extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public AssegnazioneDocenti(Sessione ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel seleziona = new JLabel("Seleziona corso");
		contentPane.add(seleziona);
		
		JTextField corso = new JTextField();
		contentPane.add(corso);
		corso.setColumns(10);
		
		JLabel sel = new JLabel("Seleziona docente");
		contentPane.add(sel);
		
		JTextField docente = new JTextField();
		contentPane.add(docente);
		docente.setColumns(10);
		
		JButton cercaCorsi = new JButton("Assegna");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Utente doc = new Utente();
					doc.createFromMatricola(Integer.parseInt(docente.getText()));
					Corso c = Notifier.getCorso(corso.getText());
					GestioneCorsi gc = new GestioneCorsi();
					gc.assegnamentoCorsi(doc, c);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setVisible(true);
	}

}
