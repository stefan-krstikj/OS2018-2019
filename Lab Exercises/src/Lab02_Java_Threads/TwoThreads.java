package Lab02_Java_Threads;


public class TwoThreads {
    public static class ThreadAB implements Runnable{
        private String string1, string2;
        public ThreadAB(String string1, String string2){
            this.string1 = string1;
            this.string2 = string2;
        }

        public void run(){
            System.out.println(string1 + "\n" + string2);
        }
    }
    public static class Thread1 extends Thread {
        public void run() {
            System.out.println("A");
            System.out.println("B");
        }
    }

    public static class Thread2 extends Thread {
        public void run() {
            System.out.println("1");
            System.out.println("2");
        }
    }

    public static void main(String[] args) {
        ThreadAB t1 = new ThreadAB("A", "B");
        ThreadAB t2 = new ThreadAB("1", "2");

        Thread th1 = new Thread(t1);
        Thread th2 = new Thread(t2);

        th1.start();
        th2.start();
    }

}