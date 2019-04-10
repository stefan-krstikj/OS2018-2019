package Java_Networking.FileTransfer;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    private Socket socket;
    private String sourceDir;

    public TCPClient(String host, int port, String sourceDir) throws IOException {
        this.socket = new Socket(host, port);
        this.sourceDir = sourceDir;
    }


    public void sendFiles(File dir) throws IOException {
        for(File f : dir.listFiles()){
            if(f.isDirectory()){
                sendFiles(f);
            }
            else{
                sendFile(f);
            }
        }
    }

    public void sendFile(File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new FileReader(file));

        dataOutputStream.writeUTF("###" + file.getName() + "###");
        String line = "";
        while((line = br.readLine()) != null){
            dataOutputStream.writeUTF(br + "\n");
        }
        dataOutputStream.writeUTF("!!!END!!!");
        dataOutputStream.flush();
        dataOutputStream.close();
    }
}
