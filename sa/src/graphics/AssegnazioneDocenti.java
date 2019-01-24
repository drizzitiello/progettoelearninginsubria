package graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseManagement.CourseManagement;
import notifier.Notifier;
import session.Session;
import socketDb.SocketDb;
import user.User;

public class AssegnazioneDocenti extends MyFrame {

	private JPanel contentPane;
	private AssegnazioneDocenti thisFrame;

	/**
	 * Create the frame.
	 * @param ses 
	 */
	public AssegnazioneDocenti(HomePage hp, Session ses) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisFrame=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hp.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel seleziona = new JLabel("Seleziona corso");
		contentPane.add(seleziona);
		
		ArrayList<Map<String, Object>> hm;
		try {
			hm = SocketDb.getInstanceDb().function("getcorsi", new Object[] {});
			String[] corsi = new String[hm.size()];
			int i=0;
			for(Map<String, Object> m : hm) {
				String corso = (String) m.get("nome");
				corsi[i] = corso;
				i++;
			}
			JComboBox selezionaCorso = new JComboBox(corsi);
			selezionaCorso.setMaximumRowCount(100);
			contentPane.add(selezionaCorso);
			
			JLabel sel = new JLabel("Seleziona docente");
			contentPane.add(sel);
			
			JComboBox selezionaDocente;
			ArrayList<Map<String, Object>> hm2;
			try {
				hm2 = SocketDb.getInstanceDb().function("get_docenti", new Object[] {});
				String[] docenti = new String[hm2.size()];
				int k=0;
				for(Map<String, Object> m : hm2) {
					String docente = (String) m.get("nome");
					docente += " "+(String) m.get("cognome");
					docenti[k] = docente;
					k++;
				}
				selezionaDocente = new JComboBox(docenti);
				selezionaDocente.setMaximumRowCount(100);
				contentPane.add(selezionaDocente);
				
				JLabel matricola = new JLabel("Matricola docente: ");
				contentPane.add(matricola);
				
				JLabel numero = new JLabel("");
				contentPane.add(numero);
				
				selezionaDocente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							ArrayList<Map<String, Object>> hm;
							String nomeCognome = selezionaDocente.getSelectedItem().toString();
							StringTokenizer st = new StringTokenizer(nomeCognome, " ");  
						    String nome = st.nextToken();
						    String cognome = st.nextToken();
							hm = SocketDb.getInstanceDb().function("get_matricola_docente", 
									new Object[] {nome, cognome});
							int matr = (int) hm.get(0).get("matricola");
							String num = ""+matr;
							numero.setText(""+num);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				
				JButton assegnaCorsi = new JButton("Assegna");
				assegnaCorsi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							User doc = new User();
							doc.createFromStudentNumber(Integer.parseInt(numero.getText()));
							Course c = Notifier.getCourse(selezionaCorso.getSelectedItem().toString());
							CourseManagement gc = new CourseManagement();
							gc.coursesAssignment(doc, c);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(assegnaCorsi);
				
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			
			/*JTextField docente = new JTextField();
			contentPane.add(docente);
			docente.setColumns(10);*/
			
			
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
		setVisible(true);
	}

}
