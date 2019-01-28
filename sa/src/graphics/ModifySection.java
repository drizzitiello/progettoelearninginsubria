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

import courseContentManagement.Course;
import courseContentManagement.CourseContentManagement;
import courseContentManagement.Section;
import session.Session;

public class ModifySection extends MyFrame {

	private JPanel contentPane;
	private ModifySection thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 * @param string 
	 */
	public ModifySection(ModifyCourse mc, Session ses, Course cor, int sectionCode) {
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
				s=gc.getSection(sectionCode);
				final int creator=s.creator;
				final int courseCode=s.courseCode;
				final Integer sonOf=s.sonOf;
				
				
				JLabel descr = new JLabel("Descrizione: ");
				contentPane.add(descr);
				
				JTextField description = new JTextField(s.description);
				contentPane.add(description);
				description.setColumns(10);
				
				JLabel tit = new JLabel("Titolo: ");
				contentPane.add(tit);
				
				JTextField title = new JTextField(s.title);
				contentPane.add(title);
				title.setColumns(10);
				
				JLabel vis = new JLabel("Visibilita: ");
				contentPane.add(vis);
				
				JTextField visibility = new JTextField(String.valueOf(s.visibility));
				contentPane.add(visibility);
				visibility.setColumns(10);
				
				JButton modifySection = new JButton("Modifica sezione");
				modifySection.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							boolean pubblic=false;
							if(visibility.getText().equals("pubblica")) {
								pubblic=true;
							}
							Section sezione = new Section(title.getText(),description.getText(),
									pubblic, sectionCode, creator, courseCode, sonOf);
							gc.modifySection(sezione);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(modifySection);
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e1.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton createResource = new JButton("Crea risorsa");
		createResource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateResource ms = new CreateResource(thisframe, ses, cor, sectionCode);
			}
		});
		contentPane.add(createResource);
		
		JButton cancelThisSection = new JButton("Cancella questa sezione");
		cancelThisSection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CourseContentManagement gc = new CourseContentManagement();
					gc.cancelSection(sectionCode);
					mc.setVisible(true);
					thisframe.setVisible(false);
				} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
					JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(cancelThisSection);
		
		setVisible(true);
	}

}
