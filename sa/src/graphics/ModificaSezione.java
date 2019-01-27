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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Section;
import session.Session;

public class ModificaSezione extends MyFrame {

	private JPanel contentPane;
	private ModificaSezione thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 * @param string 
	 */
	public ModificaSezione(ModificaCorso mc, Session ses, Course cor, int codSezione) {
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
		
		Section s = null;
		CourseContentManagement gc;
		try {
			gc = new CourseContentManagement();
			try {
				s=gc.getSection(codSezione);
				final int matricola=s.studentNumber;
				final int codCorso=s.courseCode;
				final Integer figlioDi=s.sonOf;
				
				
				JLabel descr = new JLabel("Descrizione: ");
				contentPane.add(descr);
				
				JTextField descrizione = new JTextField(s.description);
				contentPane.add(descrizione);
				descrizione.setColumns(10);
				
				JLabel tit = new JLabel("Titolo: ");
				contentPane.add(tit);
				
				JTextField titolo = new JTextField(s.title);
				contentPane.add(titolo);
				titolo.setColumns(10);
				
				JLabel vis = new JLabel("Visibilita: ");
				contentPane.add(vis);
				
				JTextField visibilita = new JTextField(String.valueOf(s.visibility));
				contentPane.add(visibilita);
				visibilita.setColumns(10);
				
				JButton modificaSezione = new JButton("Modifica sezione");
				modificaSezione.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							boolean pubblica=false;
							if(visibilita.getText().equals("pubblica")) {
								pubblica=true;
							}
							Section sezione = new Section(titolo.getText(),descrizione.getText(),
									pubblica, codSezione, matricola, codCorso, figlioDi);
							gc.modifySection(sezione);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(modificaSezione);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			e2.printStackTrace();
		}
		
		JButton creaRisorsa = new JButton("Crea risorsa");
		creaRisorsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreaRisorsa ms = new CreaRisorsa(thisframe, ses, cor);
			}
		});
		contentPane.add(creaRisorsa);
		
		JButton cancellaQuestaSezione = new JButton("Cancella questa sezione");
		cancellaQuestaSezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CourseContentManagement gc = new CourseContentManagement();
					gc.cancelSection(codSezione);
					mc.setVisible(true);
					thisframe.setVisible(false);
				} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cancellaQuestaSezione);
		
		setVisible(true);
	}

}
