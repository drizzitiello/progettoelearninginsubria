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

import gestioneContenutiCorso.Course;
import socketDb.SocketDb;

public class Notifier {
	
	static String systemMail ="";
	static String systemMailPwd ="";
	static SocketDb socket;
	
	public static boolean sendMail(String usr, String pwd, String course, String subject, String body) throws Exception {
		socket=SocketDb.getInstanceDb();
		Course c = getCourse(course);
		Integer codCourse=c.courseCode;
		if(codCourse!=null) {
			ArrayList<String> l=getUsersMail(codCourse);
			for(String s : l) {
				send_professor_email(usr, pwd, s, subject, body);
			}
			return true;
		}
		return false;
	}

	public static void sendSystemMail(String too, String subject, String body) throws AddressException, MessagingException {
		String to = checkValidityMail(too);
		
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
	
	public static Course getCourse(String nomeCorso) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		Course c=new Course();
		Object[] params = {nomeCorso};
		ArrayList<Map<String, Object>> cc= socket.function("getCorso", params);
		if(cc.size()>0) {
		for(Map<String, Object> m : cc) {
			c.setYear((int) m.get("anno_attivazione"));
			c.setCourseCode((int) m.get("codice_corso"));
			c.setCreator((int) m.get("creatore"));
			c.setDescription((String) m.get("descrizione"));
			c.setFaculty((String) m.get("facolta"));
			c.setName((String) m.get("nome"));
			c.setWeight((int) m.get("peso"));
		}
		return c;
		}
		else {
			return null;
		}
	}
	
	public static Course getCorso(int courseName) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		Course c=new Course();
		Object[] params = {courseName};
		ArrayList<Map<String, Object>> cc= socket.function("getCorso", params);
		if(cc.size()>0) {
		for(Map<String, Object> m : cc) {
			c.setYear((int) m.get("anno_attivazione"));
			c.setCourseCode((int) m.get("codice_corso"));
			c.setCreator((int) m.get("creatore"));
			c.setDescription((String) m.get("descrizione"));
			c.setFaculty((String) m.get("facolta"));
			c.setName((String) m.get("nome"));
			c.setWeight((int) m.get("peso"));
		}
		return c;
		}
		else {
			return null;
		}
	}
	
	public static ArrayList<String> getUsersMail(int courseCode) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		ArrayList<String> email=new ArrayList<String>();
		Object[] s= {courseCode};
		ArrayList<Map<String, Object>> emailObj = socket.function("getEmailUtenti", s);
		for(Map<String, Object> m : emailObj) {
			email.add((String) m.get("email"));
		}
		return email;
	}
	
	public static void send_professor_email(String usr, String pwd, String too, String subject, String body) throws SendFailedException, MessagingException{
		String to = checkValidityMail(too);
		
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
	
	private static String checkValidityMail(String to) {
		return to;
	}
	
}
