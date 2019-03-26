package Lab01_IO_Networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket serverSocket;

    public TCPServer() throws Exception{
        this.serverSocket = new ServerSocket(9876);
    }

    public void listen() throws Exception{
        Socket client = this.serverSocket.accept();
        String clientAdd = client.getInetAddress().getHostAddress();
        System.out.println("\r\tNew incoming connection from: " + clientAdd);
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String in = "";
        while((in = br.readLine()) != null){
            System.out.println("Message: " + in);
        }

    }

    public static void main(String[] args) throws Exception {
        TCPServer tcpServer = new TCPServer();
        System.out.println("Initialized TCPServer: " + tcpServer.serverSocket.getInetAddress() + " Port=" + tcpServer.serverSocket.getLocalPort());
        tcpServer.listen();
    }
}
