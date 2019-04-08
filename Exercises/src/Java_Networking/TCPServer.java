package Java_Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket server;
    private int port;

    public TCPServer(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
    }

    public void listen() throws IOException {
        String data = null;
        Socket client = this.server.accept();
        System.out.println("Incoming connection: " + client.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while((data = in.readLine()) != null){
            System.out.println(data);
        }
    }

    public void start(){

    }

    public static void main(String[] args) throws IOException {
        TCPServer server = new TCPServer(9876);
        System.out.println("Starting server: " + server.server.getInetAddress() + " : " + server.server.getLocalPort());
        server.listen();
    }
}
