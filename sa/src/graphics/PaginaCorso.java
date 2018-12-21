package graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Sessione.Sessione;
import gestioneContenutiCorso.Contenuto;
import gestioneContenutiCorso.Corso;
import gestioneContenutiCorso.ReperisciCorso;
import gestioneContenutiCorso.Risorse;
import gestioneContenutiCorso.Sezione;
import notifier.Notifier;
import socketDb.SocketDb;

public class PaginaCorso extends JFrame {

	private JPanel contentPane;
	private Corso cor;
	private Contenuto c;
	private ArrayList<Component> ac;

	/**
	 * Create the frame.
	 * @param corso 
	 */
	public PaginaCorso(Sessione ses, String corso) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ac=new ArrayList<Component>();
		
	
			try {
				cor= new Corso();
				cor.codCorso=Notifier.getCorso(corso);
				ReperisciCorso rc = new ReperisciCorso();
				c = rc.getContenutoCorso(cor);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		
		JLabel titoloCorso = new JLabel(cor.nome);
		contentPane.add(titoloCorso);
		
		JLabel descrizioneCorso = new JLabel(cor.descrizione);
		contentPane.add(descrizioneCorso);
		
		String elencoDocenti="";
		//getDocenti
		
		JLabel docentiCorso = new JLabel(elencoDocenti);
		contentPane.add(docentiCorso);
		
		boolean registrato=true;
		Object[] params = {};
		
		if(ses.getUtente().getInfo().tipoUtente!=3&&registrato) {
		
		for(Sezione s : c.sezioni) {
			JLabel sezione = new JLabel(s.titolo);
			contentPane.add(sezione);
			JButton accessoRisorse = new JButton("Cerca");
			accessoRisorse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean set=true;
					for(Component c : ac) {
						Container parent = c.getParent();
						System.out.println(c);
						parent.remove(c);
						contentPane.revalidate();
						validate();
						repaint();
						accessoRisorse.add(c);
						c.setVisible(false);
						set=false;
					}
					if(!set) {ac.clear();}
					if(set) {
						for(Component c : accessoRisorse.getComponents()) {
							contentPane.add(c);
							c.setVisible(true);
							ac.add(c);
							System.out.println("agg");
						}
					}
				}
			});
			contentPane.add(accessoRisorse);
			for(Risorse r : s.risorse) {
				JLabel descrizioneRisorsa = new JLabel(r.descrizione);
				descrizioneRisorsa.setVisible(false);
				accessoRisorse.add(descrizioneRisorsa);
				JButton risorsa = new JButton(r.nome);
				risorsa.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ReperisciCorso rc = new ReperisciCorso();
						try {
							rc.download(r);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					}
				});
				risorsa.setVisible(false);
				accessoRisorse.add(risorsa);
			}
		}
		if(ses.getUtente().getInfo().tipoUtente==2) {
			
			if(ses.getUtente().getInfo().tipoUtente==3){
				JButton modificaCorso = new JButton("Modifica corso");
				modificaCorso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ModificaCorso mc = new ModificaCorso(ses, cor);
					}
				});
				contentPane.add(modificaCorso);
			}
			
			JButton analisiCorso = new JButton("Analisi statistiche corso");
			analisiCorso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					StatisticheCorso mc = new StatisticheCorso(ses, cor);
				}
			});
			contentPane.add(analisiCorso);
		}
		
		setVisible(true);
	}
	}

}