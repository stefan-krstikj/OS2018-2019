package Lab01_IO_Networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

public class HW01_2 {
    public static void main(String[] args) throws IOException{
        InputStream in = new FileInputStream("izvor.txt");
        OutputStream  out = new FileOutputStream ("destinacija.txt");

        Stack<Integer> upsideDown = new Stack<>();
        int c = 0;
        while((c = in.read()) != -1){
            upsideDown.push(c);
        }


        while(!upsideDown.isEmpty()){
            out.write(upsideDown.pop());
        }

        out.flush();
        out.close();
    }
}
