package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketWorkerThread extends Thread {

	private Socket socket;
	private String dirOutput;

	public SocketWorkerThread(Socket socket, Map<Integer, Socket> socketMap, String dir) {
		this.socket = socket;
		this.dirOutput = dir;
		System.out.println("New Connection from:"+socket.getInetAddress().getHostAddress()+" port:"+socket.getPort());
		socketMap.put(socket.getLocalPort(), socket);
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.startsWith("###")) {
					writer = new PrintWriter(new File(this.dirOutput+"/"+line.replace("#", "")));
				} else if (line.equals("!!!END!!!")){
					writer.flush();
					writer.close();
				} else {
					writer.println(line);
					writer.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
