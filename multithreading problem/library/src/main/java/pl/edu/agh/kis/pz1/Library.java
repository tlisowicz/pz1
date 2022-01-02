package pl.edu.agh.kis.pz1;

/**
 * This class shares the resource, that can be red by reader or can be written to by a writer
 */
public class Library {

    private int resource;
    /**
     * main method, showing working algorithm
     */
    public static void main(String [] args) {

        Library library = new Library();

        Writer writer0 = new Writer("Tomasz");
        Writer writer1 = new Writer("Jakub");
        Writer writer2 = new Writer("Piotr");

        Reader reader0 = new Reader("Paulina");
        Reader reader1 = new Reader("Julia");
        Reader reader2 = new Reader("Wojciech");
        Reader reader3 = new Reader("Jan");
        Reader reader4 = new Reader("Karolina");
        Reader reader5 = new Reader("Karol");
        Reader reader6 = new Reader("Krzysztof");
        Reader reader7 = new Reader("Paweł");
        Reader reader8 = new Reader("Bartosz");
        Reader reader9 = new Reader("Dawid");

        writer0.start();
        writer1.start();
        writer2.start();

        reader0.start();
        reader1.start();
        reader2.start();
        reader3.start();
        reader4.start();
        reader5.start();
        reader6.start();
        reader7.start();
        reader8.start();
        reader9.start();
    }
}
