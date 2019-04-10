package Java_Networking.SimpleNetwork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        while(sc.hasNext()){
            System.out.println("Client " + ID + ": " + sc.nextLine());
        }
        sc.close();
    }
}

class ClientStarer {

    private int ID;
    //todo: init other required variables here
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    ClientStarer(int id, String host, int port) throws IOException {
        this.ID = id;
        // todo: Connect to server and send client ID
        socket = new Socket(host, port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        PrintWriter pw = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
        pw.write(id);
        dataOutputStream.flush();
        // todo: Listen for incoming messages
        listen();
    }

    // todo: Implement the sending message mechanism
    void sendMessage(int idReceiver, String message) throws IOException {
        dataOutputStream.writeUTF(message + ":" + idReceiver + "\n");
        dataOutputStream.flush();
    }

    // todo: end communication - send END to server
    private void endCommunication() throws IOException {
        dataOutputStream.writeUTF("END\n");
        dataOutputStream.flush();
    }

    // todo: listen for incoming messages from the server.
    // It should start a separate thread to handle listening
    // and not block the execution
    // Should start a new ClientStarterWorkerThread
    private void listen() {
        ClientStarterWorkerThread clientStarterWorkerThread = new ClientStarterWorkerThread(this.ID, this.dataInputStream);
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
    TCPServerThreaded(int port) throws IOException, InterruptedException {
        server = new ServerSocket(port);
        activeConnections = new HashMap<>();
        System.out.println("Started new Server... " + server.getInetAddress() + ":" + server.getLocalPort());
        listen();
    }

    // todo: Handle server listening
    // todo: For each connection, start a separate
    // todo: thread (ServerWorkerThread) to handle the communication
    void listen() throws IOException, InterruptedException {
        System.out.println("listening");
        List<Thread> tmpList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Socket socket = server.accept();
            Scanner sc = new Scanner(socket.getInputStream());
            String[] msg = sc.nextLine().split(":");
            addConnection(Integer.parseInt(msg[1]), socket);
            System.out.println("Putting " + msg[1] + " to the list\tCurrent state: " + activeConnections.toString());
            ServerWorkerThread serverWorkerThread = new ServerWorkerThread(Integer.parseInt(msg[1]), socket, this);
            serverWorkerThread.start();
            tmpList.add(serverWorkerThread);
        }
        for(Thread t :tmpList){
            t.join();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // todo: Start server
        TCPServerThreaded tcpServerThreaded = new TCPServerThreaded(9876);

    }
}

class ServerWorkerThread extends Thread{
    TCPServerThreaded tcpServerThreaded;
    int id;
    Socket socket;

    DataInputStream dis;
    DataOutputStream dos;

    ServerWorkerThread(int id, Socket socket, TCPServerThreaded tcp) throws IOException {
        this.id = id;
        this.socket = socket;
        this.tcpServerThreaded = tcp;

        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void run(){
        while(true){
            Scanner sc = new Scanner(dis);
            while(sc.hasNextLine()){
                String []in = sc.nextLine().split(":");
                System.out.println("Asking for connection " + in[1]);
                Socket destination = tcpServerThreaded.getConnection(Integer.parseInt(in[1]));
                try {
                    PrintWriter pw = new PrintWriter(destination.getOutputStream());
                    pw.println(in[0].trim());
                    pw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}