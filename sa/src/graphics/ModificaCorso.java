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

public class ModificaCorso extends JFrame {
	
	//codice corso, facolta e creatore ???

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
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel nome = new JLabel("Nome: ");
		contentPane.add(nome);
		
		JTextField nomeCorso = new JTextField(cor.nome);
		contentPane.add(nomeCorso);
		nomeCorso.setColumns(10);
		
		JLabel anno = new JLabel("Anno: ");
		contentPane.add(anno);
		
		JTextField annoo = new JTextField(String.valueOf(cor.anno_attivazione));
		contentPane.add(annoo);
		annoo.setColumns(10);
		
		JLabel codCorso = new JLabel("Codice corso: ");
		contentPane.add(codCorso);
		
		JTextField codice = new JTextField(String.valueOf(cor.codCorso));
		contentPane.add(codice);
		codice.setColumns(10);
		
		JLabel creatore = new JLabel("Creatore: ");
		contentPane.add(creatore);
		
		JTextField matrCreatore = new JTextField(String.valueOf(cor.creatore));
		contentPane.add(matrCreatore);
		matrCreatore.setColumns(10);
		
		JLabel descrizione = new JLabel("Descrizione: ");
		contentPane.add(descrizione);
		
		JTextField descr = new JTextField(cor.descrizione);
		contentPane.add(descr);
		descr.setColumns(10);
		
		JLabel laurea = new JLabel("Facolta: ");
		contentPane.add(laurea);
		
		JTextField facolta = new JTextField(cor.facolta);
		contentPane.add(facolta);
		facolta.setColumns(10);
		
		JLabel pesoCFU = new JLabel("CFU: ");
		contentPane.add(pesoCFU);
		
		JTextField peso = new JTextField(String.valueOf(cor.peso));
		contentPane.add(peso);
		peso.setColumns(10);
		
		JButton cercaCorsi = new JButton("Modifica");
		cercaCorsi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setAnno(Integer.parseInt(annoo.getText()));
				cor.setCodCorso(Integer.parseInt(codice.getText()));
				cor.setCreatore(Integer.parseInt(matrCreatore.getText()));
				cor.setDescrizione(descr.getText());
				cor.setLaurea(facolta.getText());
				cor.setNome(nomeCorso.getText());
				cor.setPeso(Integer.parseInt(peso.getText()));
				GestioneCorsi gc;
				try {
					gc = new GestioneCorsi();
					gc.modificaCorso(cor);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cercaCorsi);
		
		setVisible(true);
	}

}
