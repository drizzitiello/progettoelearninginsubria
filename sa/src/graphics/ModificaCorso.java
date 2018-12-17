package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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

public class ModificaCorso extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public ModificaCorso(Sessione ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		setVisible(true);
	}

	public ModificaCorso(Sessione ses, Corso cor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel nome = new JLabel("Cerca corsi");
		contentPane.add(nome);
		
		JTextField nomeCorso = new JTextField();
		contentPane.add(nomeCorso);
		nomeCorso.setColumns(10);
		
		JLabel anno = new JLabel("Cerca corsi");
		contentPane.add(anno);
		
		JTextField annoo = new JTextField();
		contentPane.add(annoo);
		annoo.setColumns(10);
		
		JLabel codCorso = new JLabel("Cerca corsi");
		contentPane.add(codCorso);
		
		JTextField codice = new JTextField();
		contentPane.add(codice);
		codice.setColumns(10);
		
		JLabel creatore = new JLabel("Cerca corsi");
		contentPane.add(creatore);
		
		JTextField matrCreatore = new JTextField();
		contentPane.add(matrCreatore);
		matrCreatore.setColumns(10);
		
		JLabel descrizione = new JLabel("Cerca corsi");
		contentPane.add(descrizione);
		
		JTextField descr = new JTextField();
		contentPane.add(descr);
		descr.setColumns(10);
		
		JLabel laurea = new JLabel("Cerca corsi");
		contentPane.add(laurea);
		
		JTextField facolta = new JTextField();
		contentPane.add(facolta);
		facolta.setColumns(10);
		
		JLabel pesoCFU = new JLabel("Cerca corsi");
		contentPane.add(pesoCFU);
		
		JTextField peso = new JTextField();
		contentPane.add(peso);
		peso.setColumns(10);
		
		
		JButton cercaCorsi = new JButton("Cerca");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setAnno(anno.getText());
				cor.setCodCorso(codice.getText());
				cor.setCreatore(matrCreatore.getText());
				cor.setDescrizione(descr.getText());
				cor.setLaurea(facolta.getText());
				cor.setNome(nomeCorso.getText());
				cor.setPeso(peso.getText());
				GestioneCorsi gc;
				try {
					gc = new GestioneCorsi();
					gc.modificaCorso(cor);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setVisible(true);
	}

}
