package pl.edu.agh.kis.pz1;

import java.util.List;
import java.util.Vector;

public class Sampler extends Thread {
    private static List<Thread> in_cs = new Vector<>();

    public static void enterCS(Thread t) { in_cs.add(t); }
    public static void leaveCS(Thread t) { in_cs.remove(t); }

    @Override
    public void run() {
        while (true){
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(in_cs);
        }
    }
}
