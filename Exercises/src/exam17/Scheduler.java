package exam17;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scheduler extends Thread{
    public static Random random = new Random();
    static List<Process> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // TODO: create 100 Process threads and register them

        for(int i = 0; i < 100; i++){
            Process p = new Process();
            register(p);
        }
        // TODO: create Scheduler and start its background execution
        Scheduler scheduler = new Scheduler();
        scheduler.start();
        // TODO: Wait for 20.000 ms for the Scheduler-ot to finish
        Thread.sleep(20000);
        // TODO: Print out the termination status
        if(scheduler.isAlive()){
            System.out.println("Interrupted");
            scheduler.interrupt();
        }else{
            System.out.println("Successfully finished");
        }

    }

    public static void register(Process process) {
        scheduled.add(process);
    }

    public Process next() {
        if (!scheduled.isEmpty()) {
            return scheduled.remove(0);
        }
        return null;
    }

    public void run() {
        try {
            while (!scheduled.isEmpty()) {
                Thread.sleep(100);
                System.out.print(".");

                // TODO: obtain the next process
                Process p = next();
                // TODO: invoke its execute() method
                p.execute();
                // TODO: wait until this process's background execution is completed
                p.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done scheduling!");
    }

}


class Process extends Thread{

    public Integer duration;

    public Process() throws InterruptedException {
        this.duration = Scheduler.random.nextInt(1000);
    }


    public void execute() throws InterruptedException {
        System.out.println("Executing[" + this + "]: " + duration);
        // TODO: start the background execution
        this.start();
    }

    public void run(){
        try {
            Thread.sleep(this.duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}