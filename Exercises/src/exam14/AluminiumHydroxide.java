package exam14;
import StateFiles.*;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AluminiumHydroxide {

    static Semaphore hydrogen;
    static Semaphore oxygen;
    static Semaphore aluminium;


    static Semaphore oHere;
    static Semaphore ohHere;
    static Semaphore ohReady;

    static Semaphore ready;
    static Semaphore done;
    static Semaphore next;

    public static void init() {
        hydrogen = new Semaphore(3);
        oxygen = new Semaphore(3);
        aluminium = new Semaphore(1);

        oHere = new Semaphore(0);
        ohHere = new Semaphore(0);

        ohReady = new Semaphore(0);
        ready = new Semaphore(0);

        done = new Semaphore(0);
        next = new Semaphore(0);
    }

    public static class Hydrogen extends TemplateThread {

        public Hydrogen(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            hydrogen.acquire();
            oHere.acquire();
            ohReady.release();
            state.bondOH();
            ohHere.release();
            ready.acquire();
            state.bondAlOH3();
            done.release();
            next.acquire();
            hydrogen.release();
        }

    }

    public static class Oxygen extends TemplateThread {

        public Oxygen(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            oxygen.acquire();
            oHere.release();
            ohReady.acquire();
            state.bondOH();

            ohHere.release();
            ready.acquire();
            state.bondAlOH3();
            done.release();
            next.acquire();
            oxygen.release();
        }

    }

    public static class Aluminium extends TemplateThread {

        public Aluminium(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            aluminium.acquire();

            ohHere.acquire(6);
            ready.release(6);
            state.bondAlOH3();
            done.acquire(6);
            next.release(6);
            state.validate();
            aluminium.release();
        }

    }

    static AluminiumHydroxideState state = new AluminiumHydroxideState();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numScenarios = 300;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numScenarios; i++) {
                Oxygen o = new Oxygen(numRuns);
                Hydrogen h = new Hydrogen(numRuns);
                threads.add(o);
                if (i % 3 == 0) {
                    Aluminium al = new Aluminium(numRuns);
                    threads.add(al);
                }
                threads.add(h);
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

class AluminiumHydroxideState extends AbstractState {

    private static final String DONE_SHOULD_CALLED_ONCE = "The validate() method should be called only once per molecule.";
    private static final String OH_BONDING_NOT_PARALLEL = "The OH bonding is not in parallel!";
    private static final String MOLECULE_NOT_BOUNDED_COMPLITELY = "The previous molecule is not bonded completely.";
    private static final String OH_3_GROUP_IS_NOT_PRESENT = "(OH)3 group is not present.";
    private static final String MAXIMUM_3_OXYGEN = "Maximum 3 Oxygen atoms for bonding are allowed.";
    private static final String MAXIMUM_3_HYDROGEN = "Maximum 3 TribeMember atoms for bonding are allowed.";
    private static final String MAXIMUM_1_ALUMINIUM = "Maximum 1 Aluminium atom for bonding is allowed.";
    private static final int MAXIMUM_1_ALUMINIUM_POINTS = 5;
    private static final int MAXIMUM_3_HYDROGEN_POINTS = 5;
    private static final int MAXIMUM_3_OXYGEN_POINTS = 5;
    private static final int OH_3_GROUP_IS_NOT_PRESENT_PONTS = 5;
    private static final int MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS = 10;
    private static final int OH_BONDING_NOT_PARALLEL_POINTS = 5;
    private static final int DONE_SHOULD_CALLED_ONCE_POINTS = 5;

    int numAtoms = 0;
    private BoundCounterWithRaceConditionCheck O;
    private BoundCounterWithRaceConditionCheck H;
    private BoundCounterWithRaceConditionCheck Al;

    public AluminiumHydroxideState() {
        O = new BoundCounterWithRaceConditionCheck(0, 3,
                MAXIMUM_3_OXYGEN_POINTS, MAXIMUM_3_OXYGEN, null, 0, null);
        H = new BoundCounterWithRaceConditionCheck(0, 3,
                MAXIMUM_3_HYDROGEN_POINTS, MAXIMUM_3_HYDROGEN, null, 0, null);
        Al = new BoundCounterWithRaceConditionCheck(0, 1,
                MAXIMUM_1_ALUMINIUM_POINTS, MAXIMUM_1_ALUMINIUM, null, 0, null);
    }

    public void bondOH() {

        Switcher.forceSwitch(3);
        if (getThread() instanceof AluminiumHydroxide.Oxygen) {
            log(O.incrementWithMax(false), "Oxygen for OH group");
        } else if (getThread() instanceof AluminiumHydroxide.Hydrogen) {
            log(H.incrementWithMax(false), "TribeMember for OH group");
        }
    }

    public void bondAlOH3() {
        synchronized (this) {
            // first check
            if (numAtoms == 0) {
                if (O.getValue() == 3 && H.getValue() == 3) {
                    O.setValue(0);
                    H.setValue(0);
                } else {
                    log(new PointsException(OH_3_GROUP_IS_NOT_PRESENT_PONTS,
                            OH_3_GROUP_IS_NOT_PRESENT), null);
                }
            }
            numAtoms++;
        }
        Switcher.forceSwitch(3);
        if (getThread() instanceof AluminiumHydroxide.Oxygen) {
            log(O.incrementWithMax(false), "Oxygen for Al(OH)3");
        } else if (getThread() instanceof AluminiumHydroxide.Hydrogen) {
            log(H.incrementWithMax(false), "TribeMember for Al(OH)3");
        } else {
            log(Al.incrementWithMax(false), "Aluminium for Al(OH)3");
        }
    }

    public void validate	() {
        synchronized (this) {
            if (numAtoms == 7) {
                reset();
                log(null, "Al(OH)3 molecule is formed.");
            } else if (numAtoms != 0) {
                log(new PointsException(MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS,
                        MOLECULE_NOT_BOUNDED_COMPLITELY), null);
                reset();
            } else {
                log(new PointsException(DONE_SHOULD_CALLED_ONCE_POINTS,
                        DONE_SHOULD_CALLED_ONCE), null);
            }
        }
    }

    private synchronized void reset() {
        O.setValue(0);
        H.setValue(0);
        Al.setValue(0);
        numAtoms = 0;
    }

    @Override
    public synchronized void finalize() {
        if (O.getMax() == 1 && H.getMax() == 1) {
            logException(new PointsException(OH_BONDING_NOT_PARALLEL_POINTS,
                    OH_BONDING_NOT_PARALLEL));
        }
    }

}