package Java_Networking.SimpleListenerSystem2;

import java.io.IOException;

public class MainStarter {
    public static void main(String[] args) throws IOException {
        TCPServer tcpServer = new TCPServer(9876);
        tcpServer.start();

        TCPClient tcpClient = new TCPClient("localhost", 9876);
        tcpClient.start();
    }
}
