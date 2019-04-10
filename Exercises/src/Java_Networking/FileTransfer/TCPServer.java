package Java_Networking.FileTransfer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket serverSocket;
    private String dirOutput;

    public TCPServer(int port, String dirOutput) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server initialized");
        this.dirOutput = dirOutput;
        listen();
    }

    public void listen() throws IOException {
        while (true){
            Socket socket = serverSocket.accept();
            TCPServerWorker tcpServerWorker = new TCPServerWorker(new InputStreamReader(socket.getInputStream()), dirOutput);
            tcpServerWorker.start();
        }
    }


}

class TCPServerWorker extends Thread{
    private BufferedReader bufferedReader;
    private PrintWriter pw;
    private String outputDir;

    public TCPServerWorker(InputStreamReader is, String outputDir) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(is);
        this.outputDir = outputDir;
    }

    public void run(){
        try {
            String line = "";
            while((line =  bufferedReader.readLine()) != null){
                if(line.startsWith("###")){
                    String fileName = line.replace("#", "");
                    pw = new PrintWriter(outputDir + "\\" + fileName);
                }
                else if(line.startsWith("!!!")){
                    pw.flush();
                    pw.close();
                }
                else{
                    pw.println(line);
                    pw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
