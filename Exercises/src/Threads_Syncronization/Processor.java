package Threads_Syncronization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Processor extends Thread{

    public static Random random = new Random();
    static List<EventGenerator> scheduled = new ArrayList<>();
    public static Semaphore generatedEvent = new Semaphore(0);
    public static Lock lock = new ReentrantLock();


    public static void main(String[] args) throws InterruptedException {
        // TODO: create the Processor and start it in the background
        Processor processor = new Processor();
        processor.start();


        for (int i = 0; i < 100; i++) {
            EventGenerator eventGenerator = new EventGenerator();
            register(eventGenerator);
            // TODO: start the eventGenerator
            eventGenerator.start();
        }


        // TODO: wait for 20.000 ms for the Processor to finish
        Thread.sleep(20000);
        // TODO: write out the execution status
        if(processor.isAlive()){
            System.out.println("Scheduling interrupted");
            processor.interrupt();
        }
        else
            System.out.println("Finished scheduling");
    }

    public static void register(EventGenerator generator) {
        scheduled.add(generator);
    }

    /**
     * Cannot be executed in parallel with the generate() method
     */
    public static void process() {
        System.out.println("processing event");
    }


    public void run() {

        while (!scheduled.isEmpty()) {
            // TODO: wait for a new event
            try {
                generatedEvent.acquire();
                lock.lock();
                process();
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: invoke its process() method

        }

        System.out.println("Done scheduling!");
    }
}


class EventGenerator extends Thread{

    public Integer duration;
    static Semaphore generateSemaphore;

    public EventGenerator() throws InterruptedException {
        this.duration = Processor.random.nextInt(1000);
        generateSemaphore=new Semaphore(5);
    }


    /**
     * Cannot be invoked in parallel by more than 5 generators
     */
    public static void generate() {
        System.out.println("Generating event: ");
    }

    public void run(){
        try {
            Thread.sleep(this.duration);
            generateSemaphore.acquire();
            Processor.lock.lock();
            generate();
            Processor.lock.unlock();
            generateSemaphore.release();
            Processor.generatedEvent.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
