package Java_Networking.VotingSystem;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class TCPServerVoter {
    ServerSocket serverSocket;
    HashMap<Integer, String> pretsedateli;
    HashMap<Integer, Integer> glasovi;
    Semaphore sto;
    static Semaphore lock=new Semaphore(1);


    public TCPServerVoter() throws IOException {
        serverSocket = new ServerSocket(9874);
        pretsedateli = new HashMap<>();
        glasovi = new HashMap<>();
        sto = new Semaphore(100);
        //lock= new Semaphore(1);

        new Slushac(this);
    }

    public void listen() throws IOException, InterruptedException {
        Socket clientSocket;
        while(true){
            sto.acquire();
            clientSocket = serverSocket.accept();
         System.out.println("new client recieved "+ clientSocket.getInetAddress().getAddress().toString()+":"+clientSocket.getPort());

            new ServerWorker(new DataInputStream(clientSocket.getInputStream()), this);
        }
    }

    public void addPretsedatel(int broj, String chovek) {
        pretsedateli.put(broj,chovek);
        glasovi.put(broj, 0);
    }

    public void printResult() {
        String s="";
        for (Map.Entry<Integer, String> n:pretsedateli.entrySet()) {
            s += n.getKey() + "\t"+n.getValue()+"\t"+glasovi.get(n.getKey())+"\n";
        }
        System.out.println(s);
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        TCPServerVoter server = new TCPServerVoter();
        server.addPretsedatel(0,"LIKI");
        server.addPretsedatel(1,"JOCO");
        server.addPretsedatel(2,"ZOKI");
        server.listen();
    }

}

class ServerWorker extends Thread{
    DataInputStream in;
    TCPServerVoter server;

    public ServerWorker(DataInputStream in, TCPServerVoter server) {
        this.in = in;
        this.server= server;
        start();
    }

    @Override
    public void run() {
        int line;
       try {
           line= in.readInt();
           TCPServerVoter.lock.acquire();
           if(server.pretsedateli.get(line) != null){
                    int a = server.glasovi.get(line) + 1;
                    server.glasovi.replace(line, a);
                    server.sto.release();

                }
           TCPServerVoter.lock.release();
           in.close();
           interrupt();

            } catch (IOException | InterruptedException e) {
                //e.printStackTrace();

            }

    }
}

class Slushac extends Thread {
    TCPServerVoter server;
    public Slushac(TCPServerVoter server) {
        this.server= server;
        start();
    }

    @Override
    public void run() {
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        String s;
        try {
            while ((s = buff.readLine()) != null)
                if (s.equals("A")) {
                    server.printResult();
                    continue;
                }
        } catch (Exception e) {
            System.out.println("EXCEPTION");
        }

    }

}


