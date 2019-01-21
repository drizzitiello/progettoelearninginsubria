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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import gestioneContenutiCorso.Corso;
import socketDb.SocketDb;

public class Notifier {
	static String systemMail ="";
	static String systemMailPwd ="";
	static SocketDb socket;
	public static boolean sendEmail(String usr, String pwd, String cor, String subject, String body) throws Exception {
		socket=SocketDb.getInstanceDb();
		Corso c = getCorso(cor);
		Integer codCorso=c.codCorso;
		if(codCorso!=null) {
			ArrayList<String> l=getEmailUtenti(codCorso);
			for(String s : l) {
				send_docente_email(usr, pwd, s, subject, body);
			}
			return true;
		}
		return false;
	}

	public static void sendSystemMail(String too, String subject, String body) throws AddressException, MessagingException {
		String to = controlloValiditaEmail(too);
		
		String password=systemMailPwd;
		String from=systemMail;
	    	       
	    String host = "smtp.office365.com";
	   
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
	    
	    Transport.send(msg,from,password);
	}
	public static Corso getCorso(String nomeCorso) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		Corso c=new Corso();
		Object[] params = {nomeCorso};
		ArrayList<Map<String, Object>> cc= socket.function("getCorso", params);
		if(cc.size()>0) {
		for(Map<String, Object> m : cc) {
			c.setAnno((int) m.get("anno_attivazione"));
			c.setCodCorso((int) m.get("codice_corso"));
			c.setCreatore((int) m.get("creatore"));
			c.setDescrizione((String) m.get("descrizione"));
			c.setLaurea((String) m.get("facolta"));
			c.setNome((String) m.get("nome"));
			c.setPeso((int) m.get("peso"));
		}
		return c;
		}
		else {
			return null;
		}
	}
	public static Corso getCorso(int nomeCorso) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		Corso c=new Corso();
		Object[] params = {nomeCorso};
		ArrayList<Map<String, Object>> cc= socket.function("getCorso", params);
		if(cc.size()>0) {
		for(Map<String, Object> m : cc) {
			c.setAnno((int) m.get("anno_attivazione"));
			c.setCodCorso((int) m.get("codice_corso"));
			c.setCreatore((int) m.get("creatore"));
			c.setDescrizione((String) m.get("descrizione"));
			c.setLaurea((String) m.get("facolta"));
			c.setNome((String) m.get("nome"));
			c.setPeso((int) m.get("peso"));
		}
		return c;
		}
		else {
			return null;
		}
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
	public static void send_docente_email(String usr, String pwd, String too, String subject, String body) throws SendFailedException, MessagingException{
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
