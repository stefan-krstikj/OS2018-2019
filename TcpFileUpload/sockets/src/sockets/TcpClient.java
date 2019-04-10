package sockets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class TcpClient extends Thread{
	
	private String folderSrc;
	private Socket socket = null;
	
	public TcpClient(String folderSrc, String folderDest, int port) throws UnknownHostException, IOException {
		this.folderSrc = folderSrc;
		this.socket = new Socket(InetAddress.getByName("localhost"), port);
	}
	
	public void run() {
		try {
			sendFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendFiles() throws IOException {
		File f = new File(folderSrc);
		List<File> files = Arrays.asList(f.listFiles());
		for (File file:files) {
			if (file.isFile()) {
				sendFile(file);
			}
		}
		socket.close();
	}
	
	public void sendFile(File file) {
		BufferedReader reader = null;
		PrintWriter writer = null;
		String line = null;
		try {
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			writer.printf("###%s###\n",file.getName());
			writer.flush();
			while ((line=reader.readLine())!=null) {
				writer.println(line);
				writer.flush();
			}
			writer.println("!!!END!!!");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		TcpClient c1 = new TcpClient("D:/Data/client1_src_data/", "D:/Data/client1_dest_data/", 9876);
		TcpClient c2 = new TcpClient("D:/Data/client2_src_data/", "D:/Data/client2_dest_data/", 9876);
		
		c1.start();
		c2.start();
	}

}
