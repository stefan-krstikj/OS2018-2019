package Java_Networking.SimpleListenerSystem2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private ServerSocket serverSocket;

    public TCPServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("SERVER: Initialized");

    }

    public void run() {
        System.out.println("SERVER: Listening for messages");
        while(true){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("SERVER: Incoming connection from " + socket);
            } catch (IOException e) {
                e.printStackTrace();
            }


            // greet the incoming client

            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Hello, client -  Server");
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // begin listening for client messages
            TCPServerWorker tcpServerWorker = new TCPServerWorker(socket);
            tcpServerWorker.start();
        }
    }
}

class TCPServerWorker extends Thread{
    private Socket socket;

    public TCPServerWorker(Socket socket){
        this.socket = socket;
        System.out.println("SERVER WORKER: Initialized");
    }

    public void run(){
        while(true){
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String msg = dataInputStream.readUTF();
                System.out.println("CLIENT: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}