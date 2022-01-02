package pl.edu.agh.kis.pz1;

public class Reader extends Thread {

    private String name;

    public Reader(String name) {
        this.name = name;
    }

    public synchronized void entersTheLibrary() {
        System.out.println(name + " enters the library.");
    }

    public synchronized void read() {
        System.out.println(name + " is reading...");
    }

    public synchronized void leave() {
        System.out.println(name + " is leaving library.");
    }


    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            entersTheLibrary();
            read();
        } while (true);
    }
}
