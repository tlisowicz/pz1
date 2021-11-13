import java.util.Scanner;
public class Problem610A {

    public static void main(String [] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Length of stick:");
        int n = Integer.parseInt(scanner.nextLine()), n1=0, n2 = n - n1, n1_prev = 0;
        while (n % 2 != 0){
            System.out.println("Length of stick must be an even number:");
            n = Integer.parseInt(scanner.nextLine());
        }
        int Combinations = 0;
        for (n1 = 0; n1 != n2; Combinations++, n1_prev++){
            n1++;
            n2 = (n/2)-n1;
            if ( n2 == n1_prev || n2 == n1 ){ break;}
        }
        System.out.println(Combinations);
    }
}
