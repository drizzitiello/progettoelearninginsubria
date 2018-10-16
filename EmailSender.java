package notifier;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class EmailSender{
		
	public static void send_uninsubria_email(String usr, String pwd, String to, String subject, String body) throws SendFailedException, MessagingException{
		String password=pwd;
		String username=usr;
	    	       
	    String host = "smtp.office365.com";
	    String from=username;
	   
		Properties props = System.getProperties();
	    props.put("mail.smtp.host",host);
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port",587);
	    
	    Session session = Session.getInstance(props);
	    
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
	    msg.setSubject(subject);
	    msg.setText(body);
	    
	    Transport.send(msg,username,password);
	    System.out.println("\nMail was sent successfully.");   
	}
	
	public static void main(String[] argv) {
	    try {
			String password="";
			String username="";
			String subject="";
			String to="";
			String body="";
		    
		    final JTextField uf= new JTextField("name@studenti.uninsubria.it");
		    final JPasswordField pf = new JPasswordField();
		    final JTextField tf= new JTextField();
		    final JTextField sf= new JTextField("email subject");
		    final JTextArea bf= new JTextArea(null,"textual content of the email", 10,20);
		      
		    Object[] message = {
		  	    "Username / From:", uf,
		        "Password:", pf,
		        "To:", tf,
		        "Subject:", sf,
		        "Body:",bf
		    };


		    int option = JOptionPane.showOptionDialog( null, message, "Send email", 
		    		JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null,new String[]{"Send", "Cancel"}, "Send");
		    if (option== JOptionPane.YES_OPTION){ 
		        password= new String( pf.getPassword());
		        username= new String(uf.getText());
		        to=new String(tf.getText());
		        subject=new String(sf.getText());
		        body=new String(bf.getText());
		        send_uninsubria_email(username, password, to, subject, body);
		    }
		    
		} catch (MessagingException e) {
		    System.err.println("SMTP SEND FAILED:");
		    System.err.println(e.getMessage());
			
		}
	}
	
}
