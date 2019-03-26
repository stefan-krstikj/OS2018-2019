package Lab02_Java_Threads;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            System.out.println("Client " + this.ID + ": "  + sc.nextLine());
        }
    }
}

class ClientStarer {

    private int ID;
    //todo: init other required variables here
    private Socket socket;
    PrintWriter pw;

    ClientStarer(int id, String host, int port) throws IOException {
        this.ID = id;
        // todo: Connect to server and send client ID
        this.socket = new Socket(host, port);
        // todo: Listen for incoming messages
        pw = new PrintWriter(socket.getOutputStream());
        pw.println(id);
        pw.flush();
    }

    // todo: Implement the sending message mechanism
    void sendMessage(int idReceiver, String message) throws IOException {
        pw = new PrintWriter(socket.getOutputStream());
        pw.println(message + ":" + idReceiver);
        pw.flush();
    }

    // todo: end communication - send END to server
    private void endCommunication() throws IOException {
        pw = new PrintWriter(socket.getOutputStream());
        pw.println("END");
        pw.flush();
    }

    // todo: listen for icoming messages from the server.
    // It should start a separate thread to handle listening
    // and not block the execution
    // Should start a new ClientStarterWorkerThread
    private void listen() throws IOException {
        ClientStarterWorkerThread th = new ClientStarterWorkerThread(this.ID, new DataInputStream(socket.getInputStream()));
        th.start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //todo: Initialize and start 3 clients

        ClientStarer client1 = new ClientStarer(1, "127.0.0.1", 9876);
        ClientStarer client2 = new ClientStarer(2, "127.0.0.1", 9876);
        ClientStarer client3 = new ClientStarer(3, "127.0.0.1", 9876);

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
    int port;


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
        server = new ServerSocket(port);
        activeConnections = new HashMap<>();
    }

    // todo: Handle server listening
    // todo: For each connection, start a separate
    // todo: thread (ServerWorkerThread) to handle the communication
    void listen() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            Socket client = server.accept();
            Scanner sc = new Scanner(client.getInputStream());
            int id = sc.nextInt();
            System.out.println(id);
            ServerStarterThread sst = new ServerStarterThread(client, id, this);
            sst.start();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // todo: Start server
        TCPServer tcpServer = new TCPServer(9876);
        System.out.println("Startng server: " + tcpServer.server.getLocalPort() + " port=" + tcpServer.server.getLocalPort());
        tcpServer.listen();
    }

    class ServerStarterThread extends Thread{
        Socket client;
        int clientID;
        TCPServer server;

        public ServerStarterThread(Socket cl, int id, TCPServer server){

            this.client = cl;
            this.clientID = id;
            this.server = server;
            server.addConnection(clientID, client);
        }

        public void run(){
            try {
                Scanner sc = new Scanner(client.getInputStream());
                String line = sc.nextLine();
                if(line.equals("END")){
                    System.out.println("End!!");
                    return;
                }

                String[] split = line.split(":");
                this.clientID = Integer.parseInt(split[1]);
                PrintWriter pw = new PrintWriter(client.getOutputStream());
                pw.write(split[0]);
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}