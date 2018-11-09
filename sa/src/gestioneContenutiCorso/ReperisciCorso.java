package gestioneContenutiCorso;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import socketDb.SocketDb;

public class ReperisciCorso {
	SocketDb socket;
	public List<String> getCorsi() throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		String sql = "{call getCorsi}";
		ResultSet obj=socket.function(sql,null);
		ArrayList<String> corsi = new ArrayList<String>();
		while(obj.next()) {
			corsi.add(obj.getString(0));
		}
		return corsi;
	}
	public Contenuto getContenutoCorso(Corso c) throws ClassNotFoundException, SQLException{
		Contenuto cont=new Contenuto();
		socket=SocketDb.getInstanceDb();
		String sql,sql2;
		int i=0;
		sql = "{call getContenutoCorso1(?)}";
		String[] s= {String.valueOf(c.codCorso)};
		ResultSet obj=socket.function(sql,s);
		while(obj.next()) {
			int codSezione=(int) obj.getInt(i);
			i++;
			String descr=(String) obj.getString(i);
			i++;
			String titolo=(String) obj.getString(i);
			i++;
			Boolean visibilita=(Boolean) obj.getBoolean(i);
			i++;
			Sezione sez=cont.addSection(titolo, descr, visibilita, codSezione);
			sql2 = "{call getContenutoCorso1}";
			String[] s2= {String.valueOf(codSezione)};
			ResultSet obj2=socket.function(sql2,s2);
			int k=0;
			while(obj2.next()) {
				String nome=(String) obj2.getString(k);
				k++;
				String descr2=(String) obj2.getString(k);
				k++;
				String path=(String) obj2.getString(k);
				k++;
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
