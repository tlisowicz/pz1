package lab2;

public class Main {
    public static void main(String [] args){
        Y y = new Y();
        Dog d1 = new Dog("rex", "Ruff");
        Dog d2 = new Dog("scruffy", "Wurf");
        System.out.println(d1.name + ' '+ d1.says + " " + d2.name + " " + d2.says);
        Dog d3 = d1;
        Dog d4 = new Dog("rex", "Ruff");
        System.out.println(d1 == d2);
        System.out.println(d1 == d3);
        System.out.println(d1 == d4);
        System.out.println(d2 == d3);
        System.out.println(d2 == d4);
        System.out.println(d3 == d4);
        System.out.println(d1.equals(d3));
        System.out.println(d1.equals(d2));
        System.out.println(d1.equals(d4));
        System.out.println(d2.equals(d3));
        System.out.println(d2.equals(d4));
        System.out.println(d3.equals(d4));



    }

}
//Metoda mask() od Y jest wołana
// nie możemy być pewni że wszystko jest dobrze zainicjowane