package pl.edu.agh.kis.pz1;

public class Writer extends Thread {

    private RWLock lock;

    public Writer(RWLock lock, int i) {
        super("W" + i);
        this.lock = lock;
    }


    @Override
    public void run() {
        while (true) {

            Library.rand_sleep(4000);

            lock.writer_pre();
            Sampler.enterCS(currentThread());

            Library.rand_sleep(1000);

            Sampler.leaveCS( currentThread() );
            lock.writer_post();
        }
    }
}
