package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Section;
import notifier.Notifier;
import session.Session;

public class CreateSection extends MyFrame {

	private JPanel contentPane;
	private CreateSection thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 */
	public CreateSection(ModifyCourse mc, Session ses, Course cor) {
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
		
		JLabel tit = new JLabel("Titolo: ");
		contentPane.add(tit);
		
		JTextField title = new JTextField();
		contentPane.add(title);
		title.setColumns(10);
		
		JLabel sez = new JLabel("Codice sezione: ");
		contentPane.add(sez);

		JTextField sectionCode = new JTextField();
		contentPane.add(sectionCode);
		sectionCode.setColumns(10);
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);

		JTextField description = new JTextField();
		contentPane.add(description);
		description.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);

		JTextField visibility = new JTextField();
		contentPane.add(visibility);
		visibility.setColumns(10);
		
		JLabel courseCo = new JLabel("Codice corso: ");
		contentPane.add(courseCo);
		
		JTextField courseCode = new JTextField();
		contentPane.add(courseCode);
		courseCode.setColumns(10);

		JLabel son = new JLabel("Figlio di: ");
		contentPane.add(son);
		
		JTextField sonOf = new JTextField();
		contentPane.add(sonOf);
		sonOf.setColumns(10);
		
		JButton createSection = new JButton("Crea sezione");
		createSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pubblic=false;
				if(visibility.getText().equals("pubblica")) {
					pubblic=true;
				}
				Integer sonn = null;
				if(!sonOf.getText().equals("")) {
					sonn=Integer.parseInt(sonOf.getText());
				}
				Section s = new Section(title.getText(),  description.getText(), 
						pubblic, Integer.parseInt(sectionCode.getText()), ses.getUser().getInfo().student_number, 
						Integer.parseInt(courseCode.getText()), sonn);
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						gc.createSection(s);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, "Errore di connessione al databse");
						e1.printStackTrace();
					}
					try {
						Notifier n = new Notifier();
						n.sendMail(ses.info().email, "pwd?", cor.name,
								"Aggiornamento contenuti corso "+cor.name, "Aggiunta sezione "+title.getText());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(contentPane, "Errore durante l'invio dell'email");
						e2.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e3) {
					JOptionPane.showMessageDialog(contentPane, "Errore di connessione");
					e3.printStackTrace();
				}
			}
		});
		contentPane.add(createSection);
		
		setVisible(true);
	}

}
