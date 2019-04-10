package exam18;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TennisTournament {

    static Semaphore redSemaphore = new Semaphore(2);
    static Semaphore greenSemaphore = new Semaphore(2);
    static Semaphore matchStart = new Semaphore(0);
    static Lock lock = new ReentrantLock();
    static int igraciG = 0;
    static Semaphore redHere = new Semaphore(0);
    static Semaphore greenHere = new Semaphore(0);
    static Semaphore finishPlaying = new Semaphore(0);
    static Semaphore matchFinished = new Semaphore(0);

    public static class GreenPlayer  extends Thread{
        public void run(){
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            greenSemaphore.acquire();
            System.out.println("Green player ready");

            System.out.println("Green player enters field");

            lock.lock();
            igraciG++;
            if(igraciG == 2){
                igraciG = 0;
                lock.unlock();
                redHere.acquire(2);
                matchStart.release(4);
            }
            else{
                lock.unlock();
            }

            matchStart.acquire();
            System.out.println("Match started");

            lock.lock();
            igraciG++;
            if(igraciG == 2){
                igraciG = 0;
                lock.unlock();
                redHere.acquire(2);
                finishPlaying.release(4);
            }
            else{
                lock.unlock();
            }

            finishPlaying.acquire();
            System.out.println("Green player finished playing");
            // TODO: only one player calls the next line per match
            lock.lock();
            igraciG++;
            if(igraciG == 2){
                igraciG = 0;
                lock.unlock();
                redHere.acquire(2);
                System.out.println("Match finished");
                redSemaphore.release(2);
                greenSemaphore.release(2);
            }
            else{
                lock.unlock();
            }

        }
    }

    public static class RedPlayer extends Thread{

        public void run(){
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            redSemaphore.acquire();
            System.out.println("Red player ready");

            System.out.println("Red player enters field");
            redHere.release();
            matchStart.acquire();
            System.out.println("Match started");

            redHere.release();
            finishPlaying.acquire();
            System.out.println("Red player finished playing");
            // TODO: only one player calls the next line per match
            redHere.release();
        }

    }


    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }
        // start 30 red and 30 green players in background
        for(Thread t : threads) {
            //System.out.println("Thread start");
            t.start();
        }
        // after all of them are started, wait each of them to finish for 1_000 ms
        for(Thread t : threads){
            //System.out.println("Thread sleep");
            t.sleep(1000);
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("\tALIVE THREAD!\t");
            }
            else{
                System.out.println("\tALL GOOD\t");
            }
        }
        // after the waiting for each of the players is done, check the one that are not finished and terminate them

        System.err.println("Possible deadlock");
    }

}
