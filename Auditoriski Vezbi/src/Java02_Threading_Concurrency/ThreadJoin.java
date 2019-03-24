package Java02_Threading_Concurrency;

public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        Count c = new Count();
        Count2 c2 = new Count2();
        c.start();
        c.join();
        c2.start();


        c2.join();

        //c.getResult();
       // c2.getResult();

    }
}

class Count extends Thread{
    private long result;

    public void run(){
        result = count();
    }

    public void getResult(){
        System.out.println("Count: " + result);
    }

    public long count(){
        long r = 0;
        for(r = 0; r < 1000000; r++);
        System.out.println("Count finished");

        return r;
    }
}

class Count2 extends Thread{
    private long result;

    public void run(){
        result = count();
    }

    public void getResult(){
        System.out.println("Count2: " + result);
    }

    public long count(){
        long r = 0;
        for(r = 0; r < 500000; r++);
        System.out.println("Count 2 finished");
        return r;
    }
}