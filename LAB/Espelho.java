import java.util.Scanner;
public class Espelho{
    public static void main(String[] args) {
        int a, b;
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        b = scanner.nextInt();
        scanner.close();
        for(int i = a; i <= b; i++) // Printar todos numeros de a - b
        {
            System.out.print(i);
        }   
        for(int i = b; i >= a; i--)
        {
            if(i > 99)
            {
                System.out.print(i % 100);
                int j = i / 10;
                System.out.print(j % 10);
                System.out.print(j / 10);
            }
            else if(i > 9)
            {
                System.out.print(i % 10);
                System.out.print(i / 10);
            }
            else System.out.print(i);
        }
    }
}