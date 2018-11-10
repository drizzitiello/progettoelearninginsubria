package notifier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import socketDb.SocketDb;

public class Notifier {
	SocketDb socket;
	public void sendEmail(String usr, String pwd, String cor, String subject, String body) throws Exception {
		socket=SocketDb.getInstanceDb();
		int codCorso=getCorso(cor);
		ArrayList<String> l=getEmailUtenti(codCorso);
		for(String s : l) {
			send_uninsubria_email(usr, pwd, s, subject, body);
		}
	}
	private int getCorso(String cor) throws ClassNotFoundException, SQLException {
		String sql= "{call getCorsi(?)}";
		String[] params = {cor};
		ResultSet cc= socket.function("getCorsi", params);
		int codCorso=cc.getInt(0);
		return codCorso;
	}
	private ArrayList<String> getEmailUtenti(int codCorso) throws ClassNotFoundException, SQLException {
		String sql="{call getEmailUtenti(?)}";
		ResultSet emailObj;
		ArrayList<String> email=new ArrayList<String>();
		String[] s= {String.valueOf(codCorso)};
		emailObj= socket.function(sql,s);
		while(emailObj.next()) {
			email.add(emailObj.getString(0));
		}
		return email;
	}
	private static void send_uninsubria_email(String usr, String pwd, String to, String subject, String body) throws SendFailedException, MessagingException{
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
	}
}
