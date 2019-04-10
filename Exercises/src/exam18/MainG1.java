package exam18;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


class Reader extends Thread {
    final String matrixFile;
    int[][] matrix;

    Reader(String matrixFile) {
        this.matrixFile = matrixFile;
    }

    /**
     * This method should execute in background
     */
    @Override
    public void run() {
        // todo: complete this method according to the text description

        try {
            // todo: The variable in should provide the readLine() method
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(matrixFile)));
            int n = Integer.parseInt(in.readLine().trim());
            this.matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(in.readLine().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Writer extends Thread {

    private final String outputPath;
    private final int[][] matrix;

    Writer(String outputPath, int[][] matrix) {
        this.outputPath = outputPath;
        this.matrix = matrix;
    }


    @Override
    public void run() {
        int n = matrix.length;
        try {
            FileWriter fw = new FileWriter(outputPath);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // todo: write the element matrix[i][j]
                    fw.write(matrix[i][j]);
                    if(n != j+1)
                        fw.write(" ");
                    else
                        fw.write("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Transformer extends Thread {
    final int[][] matrix;
    final int row;
    final int column;
    static Semaphore transformerSemaphore = new Semaphore(15);

    int result;

    Transformer(int[][] matrix, int row, int column) {
        this.matrix = matrix;
        this.row = row;
        this.column = column;
    }

    @Override
    public void run() {
        // todo: allow maximum 15 parallel executions
        try {
            transformerSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int n = matrix.length;
        for (int k = 0; k < n; k++) {
            result += matrix[row][k] * matrix[k][column];
        }
        transformerSemaphore.release();
    }
}

class FileScanner extends Thread {

    final File directoryToScan;
    final List<File> matrixFiles = new ArrayList<>();

    FileScanner(File directoryToScan) {
        this.directoryToScan = directoryToScan;
    }

    public void run() {
        try {
            List<FileScanner> scanners = new ArrayList<>();

            if(!directoryToScan.exists()){
                System.out.println("Directory does not exist");
                return;
            }

            // todo: find *.mat files and add them in the matrixFiles list
            for(File f : directoryToScan.listFiles()) {
                if(f.isDirectory()){
                    File subDirectory = f;
                    FileScanner fs = new FileScanner(subDirectory);
                    scanners.add(fs);
                }
                else if(f.getName().endsWith(".mat")) {
                    File matFile = f;
                    matrixFiles.add(matFile);
                }
            }

            // todo: for each sub-directory, create a new instance of FileScanner


            for (FileScanner scanner : scanners) {
                //todo: invoke the scanning of the subDirectory in background
                scanner.start();
            }
            for(FileScanner scanner : scanners){
                //todo: wait for the scanner to finish
                scanner.join();
            }

            System.out.println("Done scanning: " + directoryToScan.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


public class MainG1 {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        List<Transformer> transformers = new ArrayList<>();

        Reader reader = new Reader("data/matrix.mat");
        // todo: execute file reading in background
        reader.start();
        // todo: wait for the matrix to be read
        reader.join();
        // todo: transform the matrix
        int n = reader.matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Transformer t = new Transformer(reader.matrix, i, j);
                transformers.add(t);
                // todo: start the background execution
                t.start();
            }
        }


        // todo: wait for all transformers to finish
        for(Transformer t : transformers)
            t.join();

        int[][] result = new int[n][n];
        for (Transformer t : transformers) {
            result[t.row][t.column] = t.result;
        }

        Writer writer = new Writer("data/out.bin", result);
        // todo: execute file writing in background
        writer.start();

        FileScanner scanner = new FileScanner(new File("data"));
        // todo: execute file scanning in background
        scanner.start();

        // todo: wait for the scanner to finish and show the results
        scanner.join();
        for (File matrixFile : scanner.matrixFiles) {
            System.out.println(matrixFile.getAbsolutePath());
        }

    }
}