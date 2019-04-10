package Java_Networking.SimpleListenerSystem;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClientFT extends Thread {
    private Socket socket;

    public TCPClientFT(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        System.out.println("CLIENT: Connected to server");
    }

    public void run(){
        System.out.println("CLIENT: Listening to messages");

        TCPClientFTListener tcpClientFTListener = new TCPClientFTListener(socket);
        tcpClientFTListener.start();

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("Hello from client");
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tcpClientFTListener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TCPClientFTListener extends Thread{
    private Socket socket;

    public TCPClientFTListener(Socket socket){
        this.socket = socket;
        System.out.println("Starting TCPClient Listener");
    }

    public void run(){
        System.out.println("CLIENT WORKER: Waiting for incoming message");
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String msg = dataInputStream.readUTF();
            System.out.println("LISTENER RECEIVED: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}