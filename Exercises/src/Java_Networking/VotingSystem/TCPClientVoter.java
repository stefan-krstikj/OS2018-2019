package Java_Networking.VotingSystem;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

class TCPClient{
    Socket socket;
    DataOutputStream out;

    TCPClient() throws IOException {
        socket = new Socket(InetAddress.getByName("localhost"), 9874);
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(int n) throws IOException {
        out.writeInt(n);
        out.flush();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TCPClient cl1 = new TCPClient();
        TCPClient cl2 = new TCPClient();
        TCPClient cl3 = new TCPClient();
        Random ran = new Random();
        int niza[]=new int[3];
        Arrays.fill(niza,0);
        for (int i = 0; i < 200; i++) {
            int randomot = ran.nextInt(3);
            niza[randomot]++;
            new TCPClient().sendMessage(randomot);
        }

        System.out.println(Arrays.toString(niza));
       /* cl1.sendMessage("1");
        cl2.sendMessage("1");
        cl3.sendMessage("3");*/


    }
}