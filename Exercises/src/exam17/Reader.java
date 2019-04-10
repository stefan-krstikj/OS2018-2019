package exam17;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Reader extends Thread{

    public static Random random = new Random();
    static Lock lock = new ReentrantLock();
    static Semaphore dataReady = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        // TODO: kreirajte Reader i startuvajte go negovoto pozadinsko izvrsuvanje
        Reader reader = new Reader();
        reader.start();

        for (int i = 0; i < 100; i++) {
            Writer writer = new Writer();
            //TODO: startuvajte go writer-ot
            writer.start();
        }


        // TODO: Cekajte 10000ms za Reader-ot da zavrsi
        reader.join(10000);
        // TODO: ispisete go statusot od izvrsuvanjeto
        if(reader.isAlive()){
            System.out.println("Terminated reading");
            reader.interrupt();
        }else{
            System.out.println("Finished reading");
        }
    }

    /**
     * Ne smee da bide izvrsuva paralelno so write() metodot
     */
    public static void read() {
        System.out.println("reading");
    }


    public void run() {
        int pendingReading=100;
        while (pendingReading>0) {
            pendingReading--;
            try {
                // TODO: cekanje na nov zapisan podatok
                dataReady.acquire();

                // TODO: read() metodot ne smee da se izvrsuva paralelno so write() od Writer klasata
                lock.lock();
                read();
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("Done reading!");
    }
}


class Writer extends Thread{

    public Integer duration;

    public Writer() throws InterruptedException {
        this.duration = Reader.random.nextInt(1000);
    }


    /**
     * Ne smee da bide povikan paralelno
     */
    public static void write() {
        System.out.println("writting");
    }

    public void run(){
        try {
            Thread.sleep(this.duration);
            Reader.lock.lock();
            write();
            Reader.dataReady.release();
            Reader.lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

