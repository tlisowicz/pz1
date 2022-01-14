package pl.edu.agh.kis.pz1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {

    private Lock L = new ReentrantLock();
    private Condition R = L.newCondition();
    private Condition W = L.newCondition();
    private int numR = 0;
    private int numW = 0;

    public void reader_pre() {
        L.lock();
        while (numW > 0)
            R.awaitUninterruptibly();
        ++numR;
        L.unlock();
    }

    public void reader_post() {
        L.lock();
        --numR;
        if (numR == 0)
            W.signal();
        L.unlock();
    }

    public void writer_pre() {
        L.lock();
        while (numW > 0 || numR > 0)
            W.awaitUninterruptibly();
        ++numW;
        L.unlock();
    }

    public void writer_post() {
        L.lock();
        --numW;
        R.signalAll();
        W.signal();
        L.unlock();
    }
}
