package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer {
	
	private String dirOutput;
	private ServerSocket server = null;
	private static Map<Integer,Socket> sockets;
	
	public TcpServer(int port, String output) throws IOException {
		this.server = new ServerSocket(port);
		this.dirOutput =output; 
		sockets = new HashMap<>();
	}
	
	public void start() throws IOException {
		while (true) {
			SocketWorkerThread client = new SocketWorkerThread(server.accept(),sockets,this.dirOutput);
			client.start();
		}
	}
	
	public static void main(String[] args) throws IOException {
		TcpServer server = new TcpServer(9876,"D:/Data/output/");
		server.start();
	}

}
