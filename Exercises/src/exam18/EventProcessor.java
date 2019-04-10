package exam18;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventProcessor extends Thread{

    public static Random random = new Random();
    static List<EventGenerator> scheduled = new ArrayList<>();
    public static Semaphore processReady = new Semaphore(0);
    public static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        List<EventProcessor> processors = new ArrayList<>();
        // TODO: kreirajte 20 Processor i startuvajte gi vo pozadina
        for (int i = 0; i < 20; i++) {
            EventProcessor p = new EventProcessor();
            processors.add(p);
            //TODO: startuvajte go vo pozadina
            p.start();
        }

        for (int i = 0; i < 100; i++) {
            EventGenerator eventGenerator = new EventGenerator();
            //TODO: startuvajte go eventGenerator-ot
            eventGenerator.start();
        }


        for (int i = 0; i < 20; i++) {
            EventProcessor p = processors.get(i);
            // TODO: Cekajte 20000ms za Processor-ot p da zavrsi
            Thread.sleep(20000);
            // TODO: ispisete go statusot od izvrsuvanjeto
            if(p.isAlive()){
                System.out.println("Terminate!");
                p.interrupt();
            }
            else{
                System.out.println("Successfully processed");
            }
        }
    }

    public void run(){
        try {
            process();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void process() throws InterruptedException {
        // TODO: pocekajte 5 novi nastani

        processReady.acquire(5);
        lock.lock();
        System.out.println("processing event");
        lock.unlock();
    }

}


class EventGenerator extends Thread{

    public Integer duration;

    public EventGenerator() throws InterruptedException {
        this.duration = EventProcessor.random.nextInt(1000);
    }


    /**
     * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
     */
    public static void generate() {
        System.out.println("Generating event: ");
        EventProcessor.processReady.release();
    }

    public void run(){
        generate();
    }
}