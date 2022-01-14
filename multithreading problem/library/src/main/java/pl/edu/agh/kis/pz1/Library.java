package pl.edu.agh.kis.pz1;

import java.util.Random;

/**
 * This class shares the resource, that can be red by reader or can be written to by a writer
 */
public class Library {

    private static Random rand = new Random();

    /**
     * main method, showing working algorithm
     */
    public static void rand_sleep( int maxms )
    {
        int ms = rand.nextInt() % maxms;

        int amt = (ms + maxms + 1)/2;

        try { Thread.sleep(amt); } catch(Exception e) { }
    }

    public static void main(String [] args) {

        int numberOfWriters = 3;
        int numberOfReaders = 10;

        RWLock lock = new RWLock();
        for (int i = 0; i < numberOfReaders; ++i) {
            new Reader(lock, i).start();
        }

        for (int i = 0; i < numberOfWriters; ++i) {
            new Writer(lock, i).start();
        }

        new Sampler().start();
    }
}
