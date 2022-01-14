package pl.edu.agh.kis.pz1;

public class Reader extends Thread {

    private RWLock lock;

    public Reader(RWLock lock, int i) {
        super("R" + i);
        this.lock = lock;

    }
    public String toString() { return getName(); }

    @Override
    public void run() {
        while (true) {
            Library.rand_sleep(2000);

            lock.reader_pre();
            Sampler.enterCS( currentThread() );

            Library.rand_sleep(1000);

            Sampler.leaveCS( currentThread() );
            lock.reader_post();

        }
    }
}
