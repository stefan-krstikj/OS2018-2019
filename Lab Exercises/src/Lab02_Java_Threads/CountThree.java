package Lab02_Java_Threads;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class CountThree {

    public static int NUM_RUNS = 3;
    /**
     * Promenlivata koja treba da go sodrzi brojot na pojavuvanja na elementot 3
     */
    static int count = 0;
    /**
     * TODO: definirajte gi potrebnite elementi za sinhronizacija
     */
    Semaphore semaphore = new Semaphore(1);

    public void init() {
    }

    class Counter extends Thread {

        public void count(int[] data) throws InterruptedException {
            // da se implementira
            int c = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i] == 3) {
                    c++;
                }
            }

            semaphore.acquire();
            count+=c;
            semaphore.release();
        }
        private int[] data;

        public Counter(int[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                count(data);
            } catch (InterruptedException e) {

            }

        }
    }

    public static void main(String[] args) {
        try {
            CountThree environment = new CountThree();
            environment.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() throws Exception {

        init();

        HashSet<Thread> threads = new HashSet<Thread>();
        Scanner s = new Scanner(System.in);
        int total=s.nextInt();

        int[] data = new int[total];
        for (int j = 0; j < total; j++) {
            data[j] = s.nextInt();
        }

        for (int i = 0; i < NUM_RUNS; i++) {
            Counter c = new Counter(data);
            threads.add(c);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println(count);


    }
}

