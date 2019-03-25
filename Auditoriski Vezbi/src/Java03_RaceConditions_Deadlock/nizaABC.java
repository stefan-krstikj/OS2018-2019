package Java03_RaceConditions_Deadlock;

import java.util.concurrent.Semaphore;

// ABCABCABC so toa sto sekoj thread pecati posebna bukva
// klasata se vika ToggleThreads kaj profesorot

public class nizaABC {
    private static Semaphore sA = null;
    private static Semaphore sB = null;
    private static Semaphore sC = null;

    public static void init(){
        sA = new Semaphore(1);
        sB = new Semaphore(0);
        sC = new Semaphore(0);
    }

    public static void main(String[] args){
        init();
        ToggleA tA = new ToggleA();
        ToggleB tB = new ToggleB();
        ToggleC tC = new ToggleC();

        tA.start();
        tB.start();
        tC.start();
    }

    public static class ToggleA extends Thread{

        public int numRuns = 6;
        public void run(){
            while(numRuns > 0){
                try {
                    sA.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("A");
                sB.release();
                numRuns--;
            }
        }
    }
    public static class ToggleB extends Thread{
        public int numRuns = 6;
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
