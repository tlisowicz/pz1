package lab2;

public class Dog {
    public String name;
    public String says;

    public Dog(String name, String says){
        this.name = name;
        this.says = says;
    }
    public boolean equals(Object o){
        if (!(o instanceof Dog)){return false;}
        Dog d = (Dog) o;
        return (this.name.equals(d.name) && this.says.equals(d.says));
    }
}
