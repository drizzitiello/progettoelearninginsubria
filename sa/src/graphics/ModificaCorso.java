package graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
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
import Utente.Utente;
import gestioneContenutiCorso.Contenuto;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.GestioneContenutoCorso;
import gestioneContenutiCorso.ReperisciCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;
import gestioneCorsi.GestioneCorsi;

public class ModificaCorso extends JFrame {

	private JPanel contentPane;

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
		
		JLabel codCorso = new JLabel("Codice corso: "+String.valueOf(cor.codCorso));
		contentPane.add(codCorso);
		
		JLabel creatore = new JLabel("Creatore: "+String.valueOf(cor.creatore));
		contentPane.add(creatore);
		
		JLabel descrizione = new JLabel("Descrizione: "+cor.descrizione);
		if(ses.getUtente().getInfo().tipoUtente==3) {
			descrizione.setText("Descrizione: ");
		}
		contentPane.add(descrizione);
		
		JTextField descr = new JTextField(cor.descrizione);
		descr.setColumns(10);
		if(ses.getUtente().getInfo().tipoUtente==3) {
			contentPane.add(descr);
		}
		
		JLabel laurea = new JLabel("Facolta: ");
		contentPane.add(laurea);
		
		JTextField facolta = new JTextField(cor.laurea);
		contentPane.add(facolta);
		facolta.setColumns(10);
		
		JLabel pesoCFU = new JLabel("CFU: ");
		contentPane.add(pesoCFU);
		
		JTextField peso = new JTextField(String.valueOf(cor.peso));
		contentPane.add(peso);
		peso.setColumns(10);
		
		JButton modifica = new JButton("Modifica");
		modifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cor.setAnno(Integer.parseInt(annoo.getText()));
				if(ses.getUtente().getInfo().tipoUtente==3) {
					cor.setDescrizione(descr.getText());
				}
				else {
					cor.setDescrizione(descrizione.getText());
				}
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
		contentPane.add(modifica);
		
		ReperisciCorso rc = new ReperisciCorso();
		Contenuto c=null;
			try {
				c = rc.getContenutoCorso(cor);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		for(Sezione s : c.sezioni) {
			
			//JTextField sezione = new JTextField(""+s.codSezione);
			JTextField sezione = new JTextField(s.titolo);
			contentPane.add(sezione);
			nomeCorso.setColumns(10);
			JButton modificaSezione = new JButton("Modifica sezione");
			modificaSezione.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ModificaSezione ms = new ModificaSezione(ses, cor, s.codSezione);
				}
			});
			contentPane.add(modificaSezione);
			for(Risorse r : s.risorse) {
				//JTextField risorsa = new JTextField(""+r.codRisorsa);
				JTextField risorsa = new JTextField(r.nome);
				contentPane.add(risorsa);
				nomeCorso.setColumns(10);
				JButton modificaRisorsa = new JButton("Modifica risorsa");
				modificaRisorsa.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaRisorsa ms = new ModificaRisorsa(r.codRisorsa);
					}
				});
				contentPane.add(modificaRisorsa);
			}
		}
		
		JButton creaSezione = new JButton("Crea sezione");
		creaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaSezione ms = new CreaSezione(ses, cor);
			}
		});
		contentPane.add(creaSezione);
		
		setVisible(true);
	}

}
