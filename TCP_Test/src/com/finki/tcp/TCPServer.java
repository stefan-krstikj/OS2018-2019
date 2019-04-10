

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

class ServerWorker extends Thread {
    public int randomID;
    Socket clientConnection;

    ServerWorker(Socket clientConnection) {
        this.clientConnection = clientConnection;
        randomID = new Random().nextInt();
    }

    @Override
    public void run() {
        try {
            System.out.println("SERVER WORKER: Listening message from client");

            DataInputStream inputStream = new DataInputStream(clientConnection.getInputStream());
            String received = inputStream.readUTF();
            System.out.println("Here in worker thread:" + randomID + " - Received message: " + received);

            DataOutputStream outputStream = new DataOutputStream(clientConnection.getOutputStream());
            outputStream.writeUTF("Hello from server worker " + randomID);
            outputStream.flush();

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class TCPServer extends Thread {

    ServerSocket server;

    TCPServer(int portNum) throws IOException {
        server = new ServerSocket(portNum);
        System.out.println("SERVER: Server started");
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("SERVER: Server listening connection");
                Socket clientConnection = server.accept();

                System.out.println("SERVER: Server accepted connection");

                ServerWorker serverWorker = new ServerWorker(clientConnection);
                serverWorker.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
