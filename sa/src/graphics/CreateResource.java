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
import courseContentManagement.Resource;
import notifier.Notifier;
import session.Session;

public class CreateResource extends MyFrame {

	private JPanel contentPane;
	private CreateResource thisframe;

	/**
	 * Create the frame.
	 * @param cor 
	 * @param ses 
	 * @param codSezione 
	 */
	public CreateResource(ModifySection ms, Session ses, Course cor, int codSezione) {
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
				ms.setVisible(true);
				thisframe.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		JLabel no = new JLabel("Nome: ");
		contentPane.add(no);
		
		JTextField name = new JTextField();
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel descr = new JLabel("Descrizione: ");
		contentPane.add(descr);

		JTextField description = new JTextField();
		contentPane.add(description);
		description.setColumns(10);
		
		JLabel path = new JLabel("Percorso: ");
		contentPane.add(path);

		JTextField pth = new JTextField();
		contentPane.add(pth);
		pth.setColumns(10);
		
		JLabel res = new JLabel("Codice risorsa: ");
		contentPane.add(res);

		JTextField resourceCode = new JTextField();
		contentPane.add(resourceCode);
		resourceCode.setColumns(10);
		
		JLabel vis = new JLabel("Visibilita: ");
		contentPane.add(vis);
		
		JTextField visibility = new JTextField();
		contentPane.add(visibility);
		visibility.setColumns(10);
		
		JLabel type = new JLabel("Tipo: ");
		contentPane.add(type);

		JTextField tp = new JTextField();
		contentPane.add(tp);
		tp.setColumns(10);
		
		JButton createResource = new JButton("Crea risorsa");
		createResource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pubblic=false;
				if(visibility.getText().equals("pubblica")) {
					pubblic=true;
				}
				Resource r = new Resource(name.getText(),  description.getText(), pth.getText(), 
						codSezione, Integer.parseInt(resourceCode.getText()), 
						pubblic, tp.getText());
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						Notifier n = new Notifier();
						n.sendMail(ses.info().email, "pwd?", cor.name, 
								"Aggiornamento contenuti corso "+cor.name, "Aggiunta risorsa "+name.getText());
						try {
							gc.createResource(r);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane, "Errore di connessione al databse");
							e1.printStackTrace();
						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(contentPane, "Errore durante l'invio delle email");
						e2.printStackTrace();
					}
				} catch (MalformedURLException | RemoteException | NotBoundException e3) {
					e3.printStackTrace();
				}
			}
		});
		contentPane.add(createResource);
		
		setVisible(true);
	}

}
