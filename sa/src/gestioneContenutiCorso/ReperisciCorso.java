package gestioneContenutiCorso;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import socketDb.SocketDb;

public class ReperisciCorso {
	SocketDb socket;
	public ArrayList<Corso> getCorsi() throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "getCorsi";
		Object[] s= {};
		ArrayList<Map<String, Object>> obj=socket.function(sql, s);
		ArrayList<Corso> corsi = new ArrayList<Corso>();
		for(Map<String, Object> m : obj) {
			corsi.add(new Corso((int) m.get("codice_corso"), (String) m.get("nome"),
					(int) m.get("anno_attivazione"), (String) m.get("facolta"), 
					(String) m.get("descrizione"), (int) m.get("peso"), (int) m.get("creatore")));
		}
		return corsi;
	}
	public Contenuto getContenutoCorso(Corso c) throws ClassNotFoundException, SQLException{
		Contenuto cont=new Contenuto();
		socket=SocketDb.getInstanceDb();
		String sql,sql2;
		sql = "getContenutoCorso";
		Object[] s= {c.codCorso};
		ArrayList<Map<String, Object>> obj=socket.function(sql,s);
		for(Map<String, Object> m : obj) {
			int codSezione=(int) m.get("codice_sezione");
			String titolo=(String) m.get("titolo");
			String descr=(String) m.get("descrizione");
			Boolean visibilita=(Boolean) m.get("is_pubblica");
			int matricola=(int) m.get("matricola");
			int codCorso=(int) m.get("cod_corso");
			Integer figlioDi=(Integer) m.get("figlio_di");
			Sezione sez=cont.addSection(titolo, descr, visibilita, codSezione, matricola, codCorso, figlioDi);
			sql2 = "getContenutoCorso1";
			Object[] s2= {codSezione};
			ArrayList<Map<String, Object>> obj2=socket.function(sql2,s2);
			for(Map<String, Object> ms : obj2) {
				String nome=(String) ms.get("nome");
				String descr2=(String) ms.get("descrizione");
				String path2=(String) ms.get("percorso");
				int codSezione2=(int) ms.get("codice_sezione");
				int codRisorsa=(int) ms.get("codice_risorsa");
				Boolean visibilita2=(Boolean) ms.get("is_pubblica");
				String tipo=(String) ms.get("tipo");
				sez.addResource(nome, descr2, path2, codSezione2, codRisorsa, visibilita2, tipo);
			}
		}
		return cont;
	}
	public void download(Risorse r) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		try {
			String extension = "";
			int i = r.path.lastIndexOf('.');
			if (i > 0) {
			    extension = r.path.substring(i+1);
			}
			BufferedInputStream in =new BufferedInputStream(new URL(r.path).openStream());
			Files.copy(in, Paths.get("C:/Users/macri/Desktop/nini."+extension),StandardCopyOption.REPLACE_EXISTING);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
