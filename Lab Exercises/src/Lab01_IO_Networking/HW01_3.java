package Lab01_IO_Networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HW01_3 {
    public static void main(String[] args) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter("destinacija.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("izvor.txt")));
        String line = br.readLine();
        for(int i = line.length()-1; i >= 0; i--){
            bw.write(line.charAt(i));
        }
        bw.flush();
        bw.close();
    }
}
