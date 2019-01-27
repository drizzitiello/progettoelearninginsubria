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
import courseManagement.CourseManagement;
import session.Session;

public class AggiungiCorso extends MyFrame {

	private JPanel contentPane;
	private AggiungiCorso thisframe;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public AggiungiCorso(GestisciCorsi gc, Session ses, String pwd) {
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
				gc.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
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
					CourseManagement gc = new CourseManagement();
					Course corso = new Course();
					corso.setCourseCode(Integer.parseInt(codiceCorso.getText()));
	                corso.setName(insNome.getText());
	                corso.setYear(Integer.parseInt(annoAttiv.getText()));
	                corso.setFaculty(fac.getText());
	                corso.setDescription(desc.getText());
	                corso.setWeight(Integer.parseInt(pes.getText()));
	                corso.setCreator(ses.getUser().getInfo().student_number);
					gc.createCourse(corso);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(aggiungiCorso);
		setVisible(true);
	}

}
