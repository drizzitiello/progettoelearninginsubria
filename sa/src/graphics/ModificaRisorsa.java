package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.CourseContentManagement;
import courseContentManagement.Resource;

public class ModificaRisorsa extends MyFrame {

	private JPanel contentPane;
	private ModificaRisorsa thisframe;

	/**
	 * Create the frame.
	 * @param i 
	 */
	public ModificaRisorsa(ModificaCorso mc, int codRisorsa) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisframe=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		Resource r = null;
		CourseContentManagement gc;
		try {
			gc = new CourseContentManagement();
			try {
				r=gc.getResource(codRisorsa);
				final int codSezione=r.sectionCode;
				
				JLabel no = new JLabel("Nome: ");
				contentPane.add(no);
				
				JTextField nome = new JTextField(r.name);
				contentPane.add(nome);
				nome.setColumns(10);
				
				JLabel descr = new JLabel("Descrizione: ");
				contentPane.add(descr);
				
				JTextField descrizione = new JTextField(r.description);
				contentPane.add(descrizione);
				descrizione.setColumns(10);
				
				JLabel percorso = new JLabel("Percorso: ");
				contentPane.add(percorso);
				
				JTextField path = new JTextField(r.path);
				contentPane.add(path);
				path.setColumns(10);
				
				JLabel vis = new JLabel("Visibilita: ");
				contentPane.add(vis);
				
				JTextField visibilita = new JTextField(String.valueOf(r.visibility));
				contentPane.add(visibilita);
				visibilita.setColumns(10);
				
				JLabel type = new JLabel("Tipo: ");
				contentPane.add(type);
				
				JTextField tipo = new JTextField(r.type);
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
							Resource risorsa = new Resource(nome.getText(),  descrizione.getText(), path.getText(), 
									codSezione, codRisorsa, 
									pubblica, tipo.getText());
							gc.modifyResource(risorsa);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(modificaRisorsa);
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e1.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton cancellaQuestaRisorsa = new JButton("Cancella questa risorsa");
		cancellaQuestaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						gc.cancelResource(codRisorsa);
						mc.setVisible(true);
						thisframe.setVisible(false);
					} catch (ClassNotFoundException | SQLException e1) {
						JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
						e1.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e2) {
					JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
					e2.printStackTrace();
				}
			}
		});
		contentPane.add(cancellaQuestaRisorsa);
		
		setVisible(true);
	}

}
