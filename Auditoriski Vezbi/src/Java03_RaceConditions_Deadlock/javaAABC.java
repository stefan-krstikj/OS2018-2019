package Java03_RaceConditions_Deadlock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

// AABCAABC so toa sto sekoj thread pecati posebna bukva
// klasata se vika ToggleThreads kaj profesorot
// ne raboti poradi nekoja pricina?


public class javaAABC {
    private static Semaphore sA = null;
    private static Semaphore sB = null;
    private static Semaphore sC = null;

    private static ReentrantLock lock = new ReentrantLock();

    public static void init(){
        sA = new Semaphore(1);
        sB = new Semaphore(0);
        sC = new Semaphore(0);
    }

    public static void main(String[] args){
        init();
        ToggleA tA = new ToggleA(5, 2);
        ToggleB tB = new ToggleB(5);
        ToggleC tC = new ToggleC(5);

        tA.start();
        tB.start();
        tC.start();
    }

    public static class ToggleA extends Thread{

        public int numRuns = 6;
        public int numRepeats = 0;

        public ToggleA(int n, int rep){
            this.numRuns = n;
            this.numRepeats = rep;
        }

        public void run(){
            while(numRuns > 0){
                try {
                    sA.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                numRepeats--;
                System.out.print("A");
                if(numRepeats == 1){
                    sA.release();
                }else{
                    sB.release();
                    numRepeats = 2;
                }
                lock.unlock();
                sB.release();
            }
        }
    }
    public static class ToggleB extends Thread{
        public int numRuns = 6;

        public ToggleB(int n){
            this.numRuns = n;
        }
        public void run(){
            while(numRuns > 0){
                try {
                    sB.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("B");
                sC.release();
                numRuns--;
            }
        }
    }
    public static class ToggleC extends Thread{
        public int numRuns = 6;

        public ToggleC(int n){
            this.numRuns = n;
        }
        public void run(){
            while(numRuns > 0){
                try {
                    sC.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("C");
                sA.release();
                numRuns--;
            }
        }
    }
}
