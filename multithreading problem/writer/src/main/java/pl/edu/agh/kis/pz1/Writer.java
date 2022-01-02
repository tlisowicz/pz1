package pl.edu.agh.kis.pz1;

public class Writer extends Thread {

    private String name;

    public Writer(String name) {
        this.name = name;
    }

    public synchronized void entersTheLibrary() {
        System.out.println(name + " enters the library.");
    }

    public synchronized void write() {
        System.out.println(name + " is writing...");
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
            write();
        } while (true);
    }
}
