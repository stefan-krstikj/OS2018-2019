import java.util.concurrent.Semaphore;

public class testingThreads implements Runnable{
    static int counter = 1;
    static Semaphore semaphore = new Semaphore(1);

    public void run(){

//        try {
//            semaphore.acquire();
//            while (counter <= 100) {
//                System.out.println(Thread.currentThread().getName() + ": " + counter++);
//            }
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }

        synchronized (testingThreads.class) {
            while (counter <= 100) {
                System.out.println(Thread.currentThread().getName() + ": " + counter++);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){
        testingThreads t1 = new testingThreads();
        testingThreads t2 = new testingThreads();
        Thread th1 = new Thread(t1);
        Thread th2 = new Thread(t2);

        th1.start();
        th2.start();


    }
}
