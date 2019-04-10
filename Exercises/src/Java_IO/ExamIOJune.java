package Java_IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExamIOJune {
    public static void main(String[] args){

    }

    public void manage(String in, String out) throws IOException {
        File input = new File(in);
        File output = new File(out);
        File resources = new File("resources");
        File writable = new File("writable-content.txt");
        writable.createNewFile();
        FileOutputStream fos = new FileOutputStream(writable);

        if(!input.exists()){
            System.out.println("ne postoi");
            return;
        }

        if(!output.createNewFile()){
            output.delete();
            output.createNewFile();
        }

        for(File f : input.listFiles()){
            if(f.getName().endsWith(".dat") && f.canWrite()){
                f.renameTo(new File(output + "\\" + f.getName()));
            }
            else if(f.getName().endsWith(".dat") && !f.canWrite()){
                // todo dodadi vo kraj na fos
                FileInputStream fis = new FileInputStream(f);

                while(fis.read() != -1){
                    fos.write(fis.read());
                }
            }
            else if(f.isHidden()){
                System.out.println("Zbunet sum");
                f.delete();
            }
        }

    }
}
