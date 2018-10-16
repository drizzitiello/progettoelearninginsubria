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
import java.util.List;

import socketDb.SocketDb;

public class ReperisciCorso {
	SocketDb socket;
	public List<String> getCorsi() throws ClassNotFoundException, SQLException {
		socket=SocketDb.getInstanceDb();
		return socket.getCorsi();
	}
	public Contenuto getContenutoCorso(Corso c) throws ClassNotFoundException, SQLException{
		socket=SocketDb.getInstanceDb();
		return socket.getContenutoCorso(c);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
