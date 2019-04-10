package Java_Networking.SimpleListenerSystem;

import java.io.IOException;

public class MainStart {
    public static void main(String[] args) throws IOException {
        TCPServerFT server = new TCPServerFT(9876);
        server.start();

        TCPClientFT client1 = new TCPClientFT("localhost", 9876);
        client1.start();


    }
}
