package Threads_Syncronization;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class VoterStarter extends Thread{
    Scanner scanner;
    Socket socket;
    PrintWriter pw;

    public VoterStarter(String host, int port) throws IOException {
        socket = new Socket(host, port);
        pw = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
    }

    public void run(){
        // TODO: Voting system

    }

    public void listenForChoices(){

    }

    public static void main(String[] args) throws IOException {
        VoterStarter vs = new VoterStarter("localhost", 9876);
    }
}

class VoterServer {
    static Map<String, Integer> votingListWithPts;
    static Map<Integer, String> votingNumberedList;
    ServerSocket serverSocket;
    Semaphore clients;
    Lock lock;

    public VoterServer(int port) throws IOException, InterruptedException {
        serverSocket = new ServerSocket(9876);
        clients = new Semaphore(100);
        lock = new ReentrantLock();
        votingListWithPts = new HashMap<>();
        votingNumberedList = new HashMap<>();
        fillUpVotersList();
        listen();
    }

    public void fillUpVotersList(){
        votingListWithPts.put("Stefan Kr", 0);
        votingListWithPts.put("Dimitar D", 0);
        votingListWithPts.put("Mihail", 0);
        votingListWithPts.put("Panco", 0);
        votingListWithPts.put("Trajko", 0);
        votingListWithPts.put("Gjorgji", 0);

        votingNumberedList.put(1, "Stefan Kr");
        votingNumberedList.put(2, "Dimitar D");
        votingNumberedList.put(3, "Mihail");
        votingNumberedList.put(4, "Panco");
        votingNumberedList.put(5, "Trajko");
        votingNumberedList.put(6, "Gjorgji");

    }
    public void listen() throws InterruptedException, IOException {
        while(true){
            Socket s = serverSocket.accept();
            clients.acquire();
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(printCandidates());
            Scanner sc = new Scanner(s.getInputStream());
            int votingChoiceNumb = sc.nextInt();
            String votingChoiceName = votingNumberedList.get(votingChoiceNumb);
            lock.lock();
            int newNumb = votingListWithPts.get(votingChoiceName) + 1;
            votingListWithPts.put(votingChoiceName, newNumb);
            lock.unlock();
        }
    }

    public String printCandidates(){
        String out = "";

        for(Map.Entry<Integer, String> entry : votingNumberedList.entrySet()){
            out += entry.getKey() + ". " + entry.getValue() + "\n";
        }

        return out;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        VoterServer vs = new VoterServer(9876);
    }
}


