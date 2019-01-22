package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleClient {
	public static void main(String[] args) throws IOException {
		InetAddress addr = InetAddress.getByName(null);
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, 8080);
		try {
			System.out.println("socket = " + socket);
			BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())), true);
			
			} 
		finally {
			System.out.println("closing...");
			socket.close(); 
			}
		}
	}
