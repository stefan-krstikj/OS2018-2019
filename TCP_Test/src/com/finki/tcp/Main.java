package com.finki.tcp;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        TCPServer server = new TCPServer(9876);
        server.start();

        Client client1 = new Client("localhost", 9876);
        Client client2 = new Client("localhost", 9876);
        Client client3 = new Client("localhost", 9876);

        client1.start();
        client2.start();
        client3.start();
    }
}
