package Java_Networking.SimpleListenerSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerFT extends Thread{
    private ServerSocket serverSocket;

    public TCPServerFT(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("SERVER: Server started...");
    }

    public void run(){
        while(true){
            System.out.println("SERVER: Listening for messages");

            try {
                Socket socket = serverSocket.accept();
                System.out.println("SERVER: Accepted " + socket);

                TCPServerFTWorker worker = new TCPServerFTWorker(socket);
                worker.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class TCPServerFTWorker extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;

    public TCPServerFTWorker(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        System.out.println("SERVER WORKER ACCEPTED " + socket);
    }

    public void run(){

            try {
                String msg = dataInputStream.readUTF();

                System.out.println("Message: " + msg);

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Hello from Server worker");
                dataOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}