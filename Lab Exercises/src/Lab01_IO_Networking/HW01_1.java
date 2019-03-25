package Lab01_IO_Networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class HW01_1 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        br.close();
        File file = new File(line);
        File[] files = file.listFiles();
        float avg = 0;
        int count = 0;
        for(File f : files){
            if(f.getName().endsWith(".txt")){
                avg += f.getTotalSpace();
                count++;
            }
        }

        System.out.println(avg / count);
    }
}
