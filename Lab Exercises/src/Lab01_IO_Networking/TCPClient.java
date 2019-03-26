package Lab01_IO_Networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SecureCacheResponse;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.SocketHandler;

public class TCPClient {
    private Socket socket;
    private Scanner scanner;

    public TCPClient(InetAddress serverAddress, int serverPort) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IOException{
        String in;
        while(true){
            in = scanner.nextLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(in);
            out.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        TCPClient tcpClient = new TCPClient(InetAddress.getByName("localhost"), 9876);
        System.out.println("Connecting to server: " + tcpClient.socket.getInetAddress());
        tcpClient.start();
    }
}
