package Lab01_IO_Networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HW01_4 {
    public static void main(String[] args) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter("rezultati.csv"));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("rezultati.tsv")));

        String line = br.readLine();
        while((line = br.readLine()) != null){
            String[] split = line.split(",");
            String id = split[0];
            int ocenka1 = Integer.parseInt(split[1]);
            int ocenka2 = Integer.parseInt(split[2]);
            int ocenka3 = Integer.parseInt(split[3]);
            float avg = ocenka1 + ocenka2 + ocenka3;
            System.out.println(avg / 3);

            String writeLine = id + " " + ocenka1 + " " + ocenka2 + " " + ocenka3 + "\n";
            bw.write(writeLine);
        }

        bw.flush();
        bw.close();
    }
}
