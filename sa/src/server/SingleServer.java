package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleServer extends Thread {
	private Socket socket; 
	private BufferedReader in;
	private PrintWriter out;
	public SingleServer(Socket s) throws IOException {
		socket = s;
		in = new BufferedReader( new InputStreamReader(socket.getInputStream())); 
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
		start();
		}
	public void run() {
		System.out.println("Server");
	}
	}

