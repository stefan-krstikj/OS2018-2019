package Java_Networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

class ClientStarterWorkerThread extends Thread {

    private int ID;
    private DataInputStream inputStream;

    public ClientStarterWorkerThread(int clientID, DataInputStream inputStream) {
        this.ID = clientID;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        // todo: Handle listening to messages
        while(true){
            try {
                String in = inputStream.readUTF();
                System.out.println("Message: " + in);
                if(in.equals("END"))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientStarer {

    private int ID;
    //todo: init other required variables here
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;

    ClientStarer(int id, String host, int port) throws IOException {
        this.ID = id;
        // todo: Connect to server and send client ID
        socket = new Socket(host, port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        dos.writeUTF(id + "");
        dos.flush();
        // todo: Listen for incoming messages
        listen();
    }

    // todo: Implement the sending message mechanism
    void sendMessage(int idReceiver, String message) throws IOException {
        dos.writeUTF(message + ":" + idReceiver);
        dos.flush();
    }

    // todo: end communication - send END to server
    private void endCommunication() throws IOException {
        dos.writeUTF("END");
        dos.flush();
    }

    // todo: listen for incoming messages from the server.
    // It should start a separate thread to handle listening
    // and not block the execution
    // Should start a new ClientStarterWorkerThread
    private void listen() {
        ClientStarterWorkerThread clientStarterWorkerThread = new ClientStarterWorkerThread(this.ID, this.dis);
        clientStarterWorkerThread.start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //todo: Initialize and start 3 clients

        ClientStarer client1 = new ClientStarer(1, "localhost", 9876);
        ClientStarer client2 = new ClientStarer(2, "localhost", 9876);
        ClientStarer client3 = new ClientStarer(3, "localhost", 9876);

        // Simulate chat
        client1.sendMessage(2, "Hello from client 1");
        Thread.sleep(1000);
        client2.sendMessage(3, "Hello from client 2");
        Thread.sleep(1000);
        client1.sendMessage(3, "Hello from client 1");
        Thread.sleep(1000);
        client3.sendMessage(1, "Hello from client 3");
        Thread.sleep(1000);
        client3.sendMessage(2, "Hello from client 3");

        // Exit the chatroom
        client1.endCommunication();
        client2.endCommunication();
        client3.endCommunication();
    }
}

class TCPServerThreaded {

    private ServerSocket server;
    private HashMap<Integer, Socket> activeConnections;

    // todo: Get the required connection
    public Socket getConnection(int id) {
        return activeConnections.get(id);
    }

    // todo: Add connected client to the hash map
    void addConnection(int id, Socket connection) {
        activeConnections.put(id, connection);
    }

    synchronized void endConnection(int id){
        activeConnections.remove(id);
    }

    //todo: Initialize server
    TCPServerThreaded(int port) throws IOException {
        server = new ServerSocket(port);
        activeConnections = new HashMap<>();
        System.out.println("Server initialized\tAddress: " + server.getInetAddress() + ":"+ server.getLocalPort());
        listen();
    }

    // todo: Handle server listening
    // todo: For each connection, start a separate
    // todo: thread (ServerWorkerThread) to handle the communication
    void listen() throws IOException {
        System.out.println("Server listening!");
        while(true){
            Socket s = server.accept();
            System.out.println("Incoming conenction: " + s);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int id = (dis.readInt());
            ServerWorkerThread serverWorkerThread = new ServerWorkerThread(id, s, this);
            addConnection(id, s);
            serverWorkerThread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        // todo: Start server
        TCPServerThreaded tcpServerThreaded = new TCPServerThreaded(9876);

    }
}

class ServerWorkerThread extends Thread{
    int id;
    Socket socket;
    TCPServerThreaded tcpServerThreaded;
    DataInputStream dis;
    DataOutputStream dos;

    ServerWorkerThread(int id, Socket socket, TCPServerThreaded tcp) throws IOException {
        this.id = id;
        this.socket = socket;
        this.tcpServerThreaded = tcp;

        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream( socket.getInputStream());
    }

    public void run(){
        while(true){
            try {
                String in = dis.readUTF();

                String[] split = in.split(":");
                int id = Integer.parseInt(split[1]);
                System.out.println(id);
                Socket s = tcpServerThreaded.getConnection(id);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                this.dos.writeUTF(split[0]);
                this.dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}