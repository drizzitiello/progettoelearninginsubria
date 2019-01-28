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

public class ModifyResource extends MyFrame {

	private JPanel contentPane;
	private ModifyResource thisframe;

	/**
	 * Create the frame.
	 * @param i 
	 */
	public ModifyResource(ModifyCourse mc, int resourceCode) {
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
				r=gc.getResource(resourceCode);
				final int sectionCode=r.sectionCode;
				
				JLabel no = new JLabel("Nome: ");
				contentPane.add(no);
				
				JTextField name = new JTextField(r.name);
				contentPane.add(name);
				name.setColumns(10);
				
				JLabel descr = new JLabel("Descrizione: ");
				contentPane.add(descr);
				
				JTextField description = new JTextField(r.description);
				contentPane.add(description);
				description.setColumns(10);
				
				JLabel path = new JLabel("Percorso: ");
				contentPane.add(path);
				
				JTextField pth = new JTextField(r.path);
				contentPane.add(pth);
				pth.setColumns(10);
				
				JLabel vis = new JLabel("Visibilita: ");
				contentPane.add(vis);
				
				JTextField visibility = new JTextField(String.valueOf(r.visibility));
				contentPane.add(visibility);
				visibility.setColumns(10);
				
				JLabel type = new JLabel("Tipo: ");
				contentPane.add(type);
				
				JTextField tp = new JTextField(r.type);
				contentPane.add(tp);
				tp.setColumns(10);
				
				JButton modifyResource = new JButton("Modifica risorsa");
				modifyResource.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							boolean pubblic=false;
							if(visibility.getText().equals("pubblica")) {
								pubblic=true;
							}
							Resource resource = new Resource(name.getText(),  description.getText(), pth.getText(), 
									sectionCode, resourceCode, 
									pubblic, tp.getText());
							gc.modifyResource(resource);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
							e1.printStackTrace();
						}
					}
				});
				contentPane.add(modifyResource);
			} catch (ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(contentPane,"Errore di connessione al database");
				e1.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		JButton cancelThisResource = new JButton("Cancella questa risorsa");
		cancelThisResource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseContentManagement gc;
				try {
					gc = new CourseContentManagement();
					try {
						gc.cancelResource(resourceCode);
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
		contentPane.add(cancelThisResource);
		
		setVisible(true);
	}

}
