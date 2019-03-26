import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateNumbers {
    public static void main(String[] args) throws IOException {
        //File file = new File("output.txt");
        FileWriter fw = new FileWriter("output.txt");
        fw.append("1100\n");
        Random random = new Random();
        int counter = 0;
        for(int i = 0; i < 1100; i++){
            int numb = random.nextInt(10);
            if(numb == 3)
                counter++;
            fw.append( numb+ "\n");
        }
        System.out.println("Isprintano: " + counter + " pati!");
        fw.flush();
        fw.close();
    }
}
