package Java_Networking.SimpleListenerSystem2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {
    private Socket socket;

    public TCPClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        System.out.println("CLIENT: Connected to server");
    }
    public void run() {
        System.out.println("CLIENT Sending greeting");

        try {
            DataOutputStream dataOutputStream = dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("HELLO FROM CLIENT 1");
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TCPClientSendMessages sendMessages = new TCPClientSendMessages(new DataOutputStream(socket.getOutputStream()));
            sendMessages.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TCPCLientListener tcpcLientListener = new TCPCLientListener(new DataInputStream(socket.getInputStream()));
            tcpcLientListener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

class TCPCLientListener extends Thread{
    private DataInputStream dataInputStream;

    public TCPCLientListener(DataInputStream dataInputStream){
        this.dataInputStream = dataInputStream;
    }

    public void run(){
        while(true){
            try {
                String msg = dataInputStream.readUTF();
                System.out.println("CLIENT RECEIVED: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class TCPClientSendMessages extends Thread{
    private DataOutputStream dataOutputStream;

    public TCPClientSendMessages(DataOutputStream dataOutputStream){
        this.dataOutputStream = dataOutputStream;
        System.out.println("SEND MESSAGING SYSTEM INITIALIZED");
    }

    public void run(){
            while(true){
                Scanner sc = new Scanner(System.in);
                String msg = sc.nextLine();
                System.out.println("\tSM Log: " + msg);
                try {
                    dataOutputStream.writeUTF("CLIENT: " + msg);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

}