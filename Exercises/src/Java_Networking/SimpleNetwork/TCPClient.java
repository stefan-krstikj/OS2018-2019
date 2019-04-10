package Java_Networking.SimpleNetwork;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private Socket socket;
    private Scanner scanner;
    private int port;
    private PrintWriter pw;

    public void listen(){

    }

    public TCPClient(InetAddress address, int serverPort) throws IOException {
        socket = new Socket(address.getHostAddress(), serverPort);
        this.port = serverPort;
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        String input;
        pw = new PrintWriter(socket.getOutputStream());
        while(true){
            input = scanner.nextLine();
            pw.println(input);
            pw.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        TCPClient client = new TCPClient(InetAddress.getByName("localhost"), 9876);
        client.start();
    }
}
