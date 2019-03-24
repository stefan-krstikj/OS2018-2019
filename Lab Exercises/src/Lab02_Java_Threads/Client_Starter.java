package Lab02_Java_Threads;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

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
    }
}

class ClientStarer {

    private int ID;
    //todo: init other required variables here
    private Socket socket;
    private Scanner scanner;

    ClientStarer(int id, String host, int port) throws IOException {
        this.ID = id;
        // todo: Connect to server and send client ID
        this.socket = new Socket(host, port);
        // todo: Listen for incoming messages
        this.scanner = new Scanner(System.in);
    }

    // todo: Implement the sending message mechanism
    void sendMessage(int idReceiver, String message) throws IOException {
        PrintWriter pw = new PrintWriter(this.socket.getOutputStream(), true);
        pw.println(message);
        pw.flush();

    }

    // todo: end communication - send END to server
    private void endCommunication() throws IOException {

    }

    // todo: listen for icoming messages from the server.
    // It should start a separate thread to handle listening
    // and not block the execution
    // Should start a new ClientStarterWorkerThread
    private void listen() {

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //todo: Initialize and start 3 clients

        ClientStarer client1  = null;
        ClientStarer client2 = null;
        ClientStarer client3 = null;

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

class TCPServer {

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
    TCPServer(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    // todo: Handle server listening
    // todo: For each connection, start a separate
    // todo: thread (ServerWorkerThread) to handle the communication
    void listen() throws IOException {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew Connection from: " + clientAddress);

        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while((data = br.readLine()) != null){
            System.out.println("\r\tMessage from " + clientAddress + ": " + data);
        }
    }

    public static void main(String[] args) throws IOException {
        // todo: Start server
        TCPServer server = new TCPServer(9876);
        server.listen();
        System.out.println("Running server: " + server.server.getInetAddress() + " Port=" + server.server.getLocalPort());
    }
}