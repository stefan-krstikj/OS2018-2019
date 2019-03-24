package Java02_Threading_Concurrency;

public class ThreadBasicTest {
    public static void main(String[] args){
        ThreadA1 ta = new ThreadA1();
        ThreadB1 tb = new ThreadB1();
        ta.start();
        tb.start();
        System.out.println("main done");
    }
}

class ThreadA1 extends Thread{
    public void run(){
        for(int i = 1; i <= 20; i++){
            System.out.println("A: " + i);

        }
        System.out.println("A done");
    }
}

class ThreadB1 extends Thread{
    public void run(){
        for(int i = -1; i >= -20; i--){
            System.out.println("B: " + i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("B done");
    }
}