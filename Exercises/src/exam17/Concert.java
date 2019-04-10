package exam17;

import StateFiles.*;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Date;
import java.util.HashSet;

public class Concert {
    static Lock lock;
    static int barCount;

    static Semaphore tenor;
    static Semaphore baritone;
    static Semaphore performer;

    static Semaphore tenorHere;
    static Semaphore barHere;

    static Semaphore tenorHereToPerform;
    static Semaphore barHereToPerform;

    static Semaphore readyToForm;
    static Semaphore readyToPerform;

    public static void init() {
        lock = new ReentrantLock();
        barCount = 0;

        tenor = new Semaphore(3);
        baritone = new Semaphore(3);
        performer = new Semaphore(1);

        tenorHere = new Semaphore(0);
        barHere = new Semaphore(0);

        readyToForm = new Semaphore(0);
        readyToPerform = new Semaphore(0);

        tenorHereToPerform = new Semaphore(0);
        barHereToPerform = new Semaphore(0);
    }

    public static class Performer extends TemplateThread {

        public Performer(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            performer.acquire();

            barHereToPerform.acquire(3);
            tenorHereToPerform.acquire(3);
            readyToPerform.release(6);
            state.perform();


            barHereToPerform.acquire(3);
            tenorHereToPerform.acquire(3);
            state.vote();

            performer.release();
            baritone.release(3);
            tenor.release(3);
            barCount = 0;
        }

    }

    public static class Baritone extends TemplateThread {

        public Baritone(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            baritone.acquire();

            lock.lock();
            barCount++;
            if(barCount == 3){
                barCount = 0;
                lock.unlock();

                barHere.acquire(2);
                tenorHere.acquire(3);
                readyToForm.release(6);
            }else{
                lock.unlock();
                barHere.release();
            }


            readyToForm.acquire();
            state.formBackingVocals();

            barHereToPerform.release();
            readyToPerform.acquire();
            state.perform();
            barHereToPerform.release();
        }

    }

    public static class Tenor extends TemplateThread {

        public Tenor(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            tenor.acquire();
            tenorHere.release();

            readyToForm.acquire();
            state.formBackingVocals();

            tenorHereToPerform.release();
            readyToPerform.acquire();
            state.perform();
            tenorHereToPerform.release();
        }

    }

    static ConcertState state = new ConcertState();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numScenarios = 5;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numScenarios; i++) {
                Tenor t = new Tenor(numRuns);
                Baritone b = new Baritone(numRuns);
                threads.add(t);
                if (i % 3 == 0) {
                    Performer p = new Performer(numRuns);
                    threads.add(p);
                }
                threads.add(b);
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
class ConcertState extends AbstractState {

    private static final String VOTE_SHOULD_CALLED_ONCE = "The vote() method should be called only once per performance.";
    private static final String GROUP_FORMING_NOT_PARALLEL = "The group forming is not in parallel!";
    private static final String INCOMPLETE_PERFORMANCE = "The previous performance is incomplete.";
    private static final String GROUPS_ARE_NOT_PRESENT = "Not all backing groups are present.";
    private static final String MAXIMUM_3_BARITONES = "Maximum 3 Baritones for performance are allowed.";
    private static final String MAXIMUM_3_TENORS = "Maximum 3 Tenors for performance are allowed.";
    private static final String MAXIMUM_1_PERFORMER = "Maximum 1 Performer for performance is allowed.";
    private static final int MAXIMUM_1_PERFORMER_POINTS = 5;
    private static final int MAXIMUM_3_TENORS_POINTS = 5;
    private static final int MAXIMUM_3_BARITONES_POINTS = 5;
    private static final int GROUPS_ARE_NOT_PRESENT_POINTS = 5;
    private static final int INCOMPLETE_PERFORMANCE_POINTS = 5;
    private static final int GROUP_FORMING_NOT_PARALLEL_POINTS = 5;
    private static final int VOTE_SHOULD_CALLED_ONCE_POINTS = 5;

    int numParticipants = 0;
    private BoundCounterWithRaceConditionCheck baritone;
    private BoundCounterWithRaceConditionCheck tenor;
    private BoundCounterWithRaceConditionCheck performer;

    public ConcertState() {
        baritone = new BoundCounterWithRaceConditionCheck(0, 3,
                MAXIMUM_3_BARITONES_POINTS, MAXIMUM_3_BARITONES, null, 0, null);
        tenor = new BoundCounterWithRaceConditionCheck(0, 3,
                MAXIMUM_3_TENORS_POINTS, MAXIMUM_3_TENORS, null, 0, null);
        performer = new BoundCounterWithRaceConditionCheck(0, 1,
                MAXIMUM_1_PERFORMER_POINTS, MAXIMUM_1_PERFORMER, null, 0, null);
    }

    public void formBackingVocals() {

        Switcher.forceSwitch(3);
        if (getThread() instanceof Concert.Baritone) {
            log(baritone.incrementWithMax(false), "Baritone for backing group");
        } else if (getThread() instanceof Concert.Tenor) {
            log(tenor.incrementWithMax(false), "Tenor for backing group");
        }
    }

    public void perform() {
        synchronized (this) {
            // first check
            if (numParticipants == 0) {
                if (baritone.getValue() == 3 && tenor.getValue() == 3) {
                    baritone.setValue(0);
                    tenor.setValue(0);
                } else {
                    log(new PointsException(GROUPS_ARE_NOT_PRESENT_POINTS,
                            GROUPS_ARE_NOT_PRESENT), null);
                }
            }
            numParticipants++;
        }
        Switcher.forceSwitch(3);
        if (getThread() instanceof Concert.Baritone) {
            log(baritone.incrementWithMax(false), "Baritone performed");
        } else if (getThread() instanceof Concert.Tenor) {
            log(tenor.incrementWithMax(false), "Tenor performed");
        } else {
            log(performer.incrementWithMax(false), "Performer performed");
        }
    }

    public void vote() {
        synchronized (this) {
            if (numParticipants == 7) {
                reset();
                log(null, "Voting started.");
            } else if (numParticipants != 0) {
                log(new PointsException(INCOMPLETE_PERFORMANCE_POINTS,
                        INCOMPLETE_PERFORMANCE), null);
                reset();
            } else {
                log(new PointsException(VOTE_SHOULD_CALLED_ONCE_POINTS,
                        VOTE_SHOULD_CALLED_ONCE), null);
            }
        }
    }

    private synchronized void reset() {
        baritone.setValue(0);
        tenor.setValue(0);
        performer.setValue(0);
        numParticipants = 0;
    }

    @Override
    public synchronized void finalize() {
        if (baritone.getMax() == 1 && tenor.getMax() == 1) {
            logException(new PointsException(GROUP_FORMING_NOT_PARALLEL_POINTS,
                    GROUP_FORMING_NOT_PARALLEL));
        }
    }

}
