package Java01_IO_Networking;

import java.io.*;

public class DirectoryManipulation {
    public static void main(String[] args) throws IOException {
        String path1 = "C:/Users/Stefa/Desktop/store/todo.txt";
        String path2 = "C:/Users/Stefa/Desktop/store/todo3.txt";

        BufferedReader br = new BufferedReader(new FileReader(path1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path2));
        String line = "";
        String out = "";
        while((line = br.readLine()) != null){
            out+= line + "\n";
        }

        bw.write(out);
        bw.flush();
        bw.close();
    }
}
