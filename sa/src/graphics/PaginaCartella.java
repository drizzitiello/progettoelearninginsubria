package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.CourseContentManagement;
import courseContentManagement.FindCourse;
import courseContentManagement.Resource;

public class PaginaCartella extends MyFrame {

	private JPanel contentPane;
	private PaginaCartella thisFrame;

	/**
	 * Create the frame.
	 * @param r 
	 * @param pc 
	 */
	public PaginaCartella(Resource r, PaginaCorso pc) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		thisFrame=this;
		
		JButton backButton = new JButton("Indietro");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pc.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		Box ver = Box.createVerticalBox();
		
		CourseContentManagement ccm;
		try {
			ccm = new CourseContentManagement();
			ArrayList<Resource> resources = ccm.getFolderContent(r.resourceCode, r.name);
			for(Resource res : resources) {
				JButton risorsa = new JButton(res.name);
				risorsa.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							FindCourse rc = new FindCourse();
							rc.download(res);
						} catch (ClassNotFoundException | SQLException | MalformedURLException | RemoteException | NotBoundException e1) {
							JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
							e1.printStackTrace();
						}
					}
				});
				ver.add(risorsa);
			}
			contentPane.add(ver);
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			JOptionPane.showMessageDialog(contentPane,"Errore di connessione");
			e2.printStackTrace();
		}
		
		setVisible(true);
	}

}
