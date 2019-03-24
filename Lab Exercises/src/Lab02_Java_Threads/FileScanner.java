package Lab02_Java_Threads;
import java.io.*;
import java.io.File;


public class FileScanner implements Runnable{

    private String fileToScan; // pateka na direktorium
    //TODO: Initialize the start value of the counter
    private static Long counter = 0L; // broi kolku niski od klasata ke se kreiraat

    public FileScanner (String fileToScan) {
        this.fileToScan=fileToScan;
        //TODO: Increment the counter on every creation of FileScanner object
        counter++;
    }

    public static void printInfo(File file)  {

        /*
        * TODO: Print the info for the @argument File file, according to the requirement of the task
        * */
        if(file.isDirectory()){
            System.out.print("dir: ");
        }
        else{
            System.out.print("file: ");
        }
        System.out.println(file.getPath() + " " + file.length());
    }

    public static Long getCounter () {
        return counter;
    }


    public void run(){

        //TODO Create object File with the absolute path fileToScan.
        File file = new File(fileToScan);

        //TODO Create a list of all the files that are in the directory file.
        File [] files = null;
        if(file.isDirectory())
            files = file.listFiles();
        else{
            printInfo(file);
            return;
        }


        for (File f : files) {

            /*
            * TODO If the File f is not a directory, print its info using the function printInfo(f)
            * */

            /*
            * TODO If the File f is a directory, create a thread from type FileScanner and start it.
            * */

            FileScanner fs = null;
            try {
                fs = new FileScanner(f.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread t1 = new Thread(fs);
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {


            }

            //TODO: wait for all the FileScanner-s to finish

        }


    }

    public static void main (String [] args) throws InterruptedException {
        //for(int i = 0; i < 10000; i++) {
        String FILE_TO_SCAN = "C:\\Users\\StefanASUS\\IdeaProjects\\4 Semestar\\OS2018-2019\\fileScannerFolder";

            //TODO Construct a FileScanner object with the fileToScan = FILE_TO_SCAN
            FileScanner fileScanner = new FileScanner(FILE_TO_SCAN);

            //TODO Start the thread from type FileScanner
            Thread t1 = new Thread(fileScanner);
            t1.start();

            //TODO wait for the fileScanner to finish
            t1.join();

            //TODO print a message that displays the number of thread that were created
            System.out.println(fileScanner.getCounter());
        //}
    }
}

