package notifier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
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
	static SocketDb socket;
	public static boolean sendEmail(String usr, String pwd, String cor, String subject, String body) throws Exception {
		socket=SocketDb.getInstanceDb();
		Integer codCorso=getCorso(cor);
		if(codCorso!=null) {
			ArrayList<String> l=getEmailUtenti(codCorso);
			for(String s : l) {
				send_uninsubria_email(usr, pwd, s, subject, body);
			}
			return true;
		}
		return false;
	}
	public static Integer getCorso(String nomeCorso) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		Object[] params = {nomeCorso};
		ArrayList<Map<String, Object>> cc= socket.function("getCorso", params);
		Integer codCorso = null;
		if(cc.size()>0) codCorso=(int) cc.get(0).get("codicecorso");
		return codCorso;
	}
	public static ArrayList<String> getEmailUtenti(int codCorso) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		ArrayList<String> email=new ArrayList<String>();
		Object[] s= {codCorso};
		ArrayList<Map<String, Object>> emailObj = socket.function("getEmailUtenti", s);
		for(Map<String, Object> m : emailObj) {
			email.add((String) m.get("email"));
		}
		return email;
	}
	public static void send_uninsubria_email(String usr, String pwd, String too, String subject, String body) throws SendFailedException, MessagingException{
		String to = controlloValiditaEmail(too);
		
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
	private static String controlloValiditaEmail(String to) {
		return to;
	}
}
