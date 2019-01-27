package graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courseContentManagement.Course;
import courseContentManagement.FindCourse;
import session.Session;

public class GestisciCorsi extends MyFrame {

	private JPanel contentPane;
	private GestisciCorsi thisFrame;

	/**
	 * Create the frame.
	 * @param pwd 
	 * @param ses 
	 */
	public GestisciCorsi(HomePage hp, Session ses, String pwd) {
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
				hp.setVisible(true);
				thisFrame.setVisible(false);
			}
		});
		contentPane.add(backButton);
		
		FindCourse rc;
		try {
			rc = new FindCourse();
			try {
				ArrayList<Course> ali = rc.getCourses();
				for(Course c : ali) {
					Box corso = Box.createVerticalBox();
					JLabel codiceCorso = new JLabel("  Codice corso: "+((Integer) c.courseCode).toString());
					corso.add(codiceCorso);
					JLabel nome = new JLabel("  Nome: "+c.name);
					corso.add(nome);
					JLabel annoAttivazione = new JLabel("  Anno attivazione: "+String.valueOf(c.activation_year));
					corso.add(annoAttivazione);
					JLabel facolta = new JLabel("  Facolta: "+c.faculty);
					corso.add(facolta);
					String descr = "  Descrizione: "+c.description;
					if (descr.length()>35) descr="  Descrizione: "+c.description.substring(0, 5)+"...";
					JLabel descrizione = new JLabel(descr);
					corso.add(descrizione);
					JLabel peso = new JLabel("  CFU: "+((Integer) c.weight).toString()+"  ");
					corso.add(peso);
					JButton modificaCorso = new JButton("Modifica Corso");
					modificaCorso.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							thisFrame.setVisible(false);
							ModificaCorso ac = new ModificaCorso(thisFrame, ses, c);
						}
					});
					corso.add(modificaCorso);
					contentPane.add(corso);
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}
		JButton aggiungiCorso = new JButton("Aggiungi corso");
		aggiungiCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AggiungiCorso ac = new AggiungiCorso(thisFrame, ses, pwd);
			}
		});
		contentPane.add(aggiungiCorso);
		setVisible(true);
	}
	
	public String addLinebreaks(String input, int maxLineLength) {
	    StringTokenizer tok = new StringTokenizer(input, " ");
	    StringBuilder output = new StringBuilder(input.length());
	    int lineLen = 0;
	    while (tok.hasMoreTokens()) {
	        String word = tok.nextToken();

	        if (lineLen + word.length() > maxLineLength) {
	            output.append("<br>");
	            lineLen = 0;
	        }
	        output.append(word);
	        output.append("\n");
	        lineLen += word.length();
	    }
	    output.append("</html>");
	    return output.toString();
	}

}
