package Java_IO;

import java.io.*;
import java.util.List;
import java.util.*;

public class ExamIO {
    public static void moveWritableTxtFiles(String from, String to){
        File fileSource = new File(from);
        File fileDestination = new File(to);
        if(!fileSource.exists()){
            System.out.println("Does not exist");
            return;
        }

        if(!fileDestination.exists()){
            fileDestination.mkdir();
        }

        for(File f : fileSource.listFiles()){
            if(f.canWrite() && f.getName().endsWith(".txt")){
                String destString = fileDestination.getAbsolutePath() + "\\" + f.getName();
                f.renameTo(new File(destString));
            }
        }
    }

    public void copyLargeTxtFiles(String from, String to, long size){
        File fileSrc = new File(from);
        File fileTo = new File(to);

        if(!fileSrc.exists()){
            System.out.println("File does not exist");
            return;
        }

        for(File f : fileSrc.listFiles()){
            if(f.length() > size){
                String newName = to + "\\" + f.getName();
                f.renameTo(new File(newName));
            }
        }
    }

    public  void serializeData(String destination, List<byte[]> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(destination);
        for(byte[] b : data){
            for(byte byt : b){
                fos.write((int) byt);
            }
        }
    }

    public byte[] deseralizeDataAtPosition(String source, long position, long elementLength) throws IOException {
        byte[] out = new byte[(int) elementLength];

        RandomAccessFile randomAccessFile = new RandomAccessFile(source, "r");
        randomAccessFile.seek(position*elementLength);
        int counter = 0;
        while (counter < elementLength){
            out[counter] = randomAccessFile.readByte();
            counter++;
        }

        return out;
    }


    public static void deserealizeData(String source, List<byte[]> data, long elementLength) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        byte[] buffer = new byte[(int) elementLength];
        int counter = 0;
        while(fis.read(buffer) != -1){
            data.add(buffer);
        }
    }

    public static void invertLargeFile(String source, String destination) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(source, "r");
        FileOutputStream fos = new FileOutputStream(destination);

        long length = randomAccessFile.length()-1;
        while(length >= 0){
            randomAccessFile.seek(length);
            char bukva = (char) randomAccessFile.read();
            fos.write(bukva);
            length--;
        }
        randomAccessFile.close();
        fos.flush();
        fos.close();
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String from = sc.nextLine();
        String to = sc.nextLine();

        moveWritableTxtFiles(from, to);
    }


}
