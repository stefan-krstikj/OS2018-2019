package com.finki.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientListener extends Thread {

    DataInputStream inputStream;

    ClientListener(DataInputStream inputStream) {
        inputStream = inputStream;
    }

    @Override
    public void run() {

        try {
            System.out.println("CLIENT WORKER: Waiting message from server");

            String read = inputStream.readUTF();

            System.out.println("Received from server: ");
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

public class Client extends Thread {

    Socket socket;

    Client(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        System.out.println("CLIENT: Connected to server");

    }

    @Override
    public void run() {
        try {
            System.out.println("CLIENT: Starting listener");

            ClientListener listener = new ClientListener(new DataInputStream(socket.getInputStream()));
            listener.start();

            System.out.println("CLIENT: Sending message to server");
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("Hello from client");
            outputStream.flush();

            listener.join();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
