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
	public List<Integer> getCorsi() throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "getCorsi";
		Object[] s= {};
		ArrayList<Map<String, Object>> obj=socket.function(sql, s);
		ArrayList<Integer> corsi = new ArrayList<Integer>();
		for(Map<String, Object> m : obj) {
			corsi.add((Integer) m.get("codicecorso"));
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
			int codSezione=(int) m.get("codicesezione");
			String descr=(String) m.get("descrizione");
			String titolo=(String) m.get("titolo");
			Boolean visibilita=(Boolean) m.get("ispubblica");
			Sezione sez=cont.addSection(titolo, descr, visibilita, codSezione);
			sql2 = "getContenutoCorso1";
			Object[] s2= {codSezione};
			ArrayList<Map<String, Object>> obj2=socket.function(sql2,s2);
			for(Map<String, Object> ms : obj2) {
				String nome=(String) ms.get("nome");
				String descr2=(String) ms.get("descrizione");
				String path=(String) ms.get("percorso");
				sez.addResource(nome, descr2, path, codSezione);
			}
		}
		return cont;
	}
	public void download(Risorse r) throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		try {
			BufferedInputStream in =new BufferedInputStream(new URL(r.path).openStream());
			Files.copy(in, Paths.get(r.nome),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//or
		try {
			ReadableByteChannel rbc=Channels.newChannel(new URL(r.path).openStream());
			FileOutputStream fos=new FileOutputStream(r.nome);
			FileChannel fc=fos.getChannel();
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
